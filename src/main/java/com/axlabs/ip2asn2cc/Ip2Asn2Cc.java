package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.checker.ASNChecker;
import com.axlabs.ip2asn2cc.checker.IPv4Checker;
import com.axlabs.ip2asn2cc.checker.IPv6Checker;
import com.axlabs.ip2asn2cc.exception.RIRNotDownloadedException;
import com.axlabs.ip2asn2cc.model.FilterPolicy;
import com.axlabs.ip2asn2cc.model.IPv4Subnet;
import com.axlabs.ip2asn2cc.model.IPv6Subnet;
import com.axlabs.ip2asn2cc.rir.RIRDownloader;
import com.axlabs.ip2asn2cc.rir.RIRParser;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

public class Ip2Asn2Cc {

    private static final Logger LOG = LoggerFactory.getLogger(Ip2Asn2Cc.class);

    // The following RIR DBs have the format described here:
    // https://www.apnic.net/about-APNIC/corporate-documents/documents/resource-guidelines/rir-statistics-exchange-format
    private static final String ARIN_RIR_DB_URL = "ftp://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest";
    private static final String RIPE_RIR_DB_URL = "ftp://ftp.ripe.net/ripe/stats/delegated-ripencc-latest";
    private static final String AFRINIC_RIR_DB_URL = "ftp://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest";
    private static final String APNIC_RIR_DB_URL = "ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-latest";
    private static final String LACNIC_RIR_DB_URL = "ftp://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest";

    private static final List<String> listAllRIR = Arrays.asList(
            ARIN_RIR_DB_URL, RIPE_RIR_DB_URL,
            AFRINIC_RIR_DB_URL, APNIC_RIR_DB_URL,
            LACNIC_RIR_DB_URL);

    private final InetAddressValidator validator;

    private final IPv4Checker ipv4Checker;
    private final IPv6Checker ipv6Checker;
    private final ASNChecker asnChecker;

    private final List<File> listDownloadedFiles = Collections.synchronizedList(new ArrayList<File>());
    private final Config config;

    public Ip2Asn2Cc(final List<String> listCountryCodeRules) throws RIRNotDownloadedException {
        this(listCountryCodeRules, FilterPolicy.INCLUDE_COUNTRY_CODES, true, true);
    }

    public Ip2Asn2Cc(final List<String> listCountryCodeRules, final FilterPolicy filterPolicy) throws RIRNotDownloadedException {
        this(listCountryCodeRules, filterPolicy, true, true);
    }

    public Ip2Asn2Cc(final List<String> listCountryCodeRules, final FilterPolicy filterPolicy,
                     final Boolean includeIpv4LocalAddresses, final Boolean includeIpv6LocalAddresses) throws RIRNotDownloadedException {

        this.config = new Config(filterPolicy, includeIpv4LocalAddresses, includeIpv6LocalAddresses);
        this.validator = new InetAddressValidator();
        this.ipv4Checker = new IPv4Checker(this.validator);
        this.ipv6Checker = new IPv6Checker(this.validator);
        this.asnChecker = new ASNChecker();
        downloadListUrl();
        parseAllCountryCodes(listCountryCodeRules);
        deleteFiles();
    }

    public synchronized boolean checkIP(final String ipAddress) {
        LOG.debug("Check for: " + ipAddress);
        return ofNullable(ipAddress)
                .filter((address) -> validator.isValid(address))
                .map((address) -> this.ipv4Checker.checkIfIsInRange(address) || this.ipv6Checker.checkIfIsInRange(address))
                .map((checkResult) -> (this.config.getFilterPolicy() == FilterPolicy.INCLUDE_COUNTRY_CODES) ? checkResult : !checkResult)
                .orElse(false);
    }

    public synchronized boolean checkASN(final String asn) {
        LOG.debug("Check for: " + asn);
        return ofNullable(asn).map((asnAddress) -> this.asnChecker.checkIfMatches(asnAddress))
                .map((checkResult) -> (this.config.getFilterPolicy() == FilterPolicy.INCLUDE_COUNTRY_CODES) ? checkResult : !checkResult)
                .orElse(false);
    }

    public synchronized String getRIRCountryCode(final String ipAddress) {
        LOG.debug("Get RIR country code for: " + ipAddress);
        return ofNullable(ipAddress)
                .filter((address) -> validator.isValid(address))
                .map((address) -> this.validator.isValidInet4Address(address) ?
                        this.ipv4Checker.getRIRCountryCode(address) : this.ipv6Checker.getRIRCountryCode(address))
                .orElse(null);
    }

    private void parseAllCountryCodes(final List<String> listCountryCode) {

        final ExecutorService parserPool = Executors.newFixedThreadPool(6);
        this.listDownloadedFiles.forEach((file) -> {
            parserPool.submit(new RIRParser(this.ipv4Checker, this.ipv6Checker, this.asnChecker, file, listCountryCode));
        });

        parserPool.shutdown();

        try {
            parserPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (final InterruptedException e) {
            LOG.error("The pool to parse the RIR files was interrupted before termination.", e);
        }

        if (this.config.getIncludeIpv4LocalAddresses()) {
            // add local addresses as well:
            // 127.0.0.0/8 defined in https://tools.ietf.org/html/rfc3330
            final IPv4Subnet localhostIPv4 = new IPv4Subnet("127.0.0.0", 16777214);
            ipv4Checker.addSubnet(localhostIPv4);
        }
        if (this.config.getIncludeIpv6LocalAddresses()) {
            // ::1/128 defined in https://tools.ietf.org/html/rfc4291
            final IPv6Subnet localhostIPv6 = new IPv6Subnet("0:0:0:0:0:0:0:1", 128);
            ipv6Checker.addSubnet(localhostIPv6);
        }

        LOG.debug("Parsed all RIR files");
    }

    private void downloadListUrl() throws RIRNotDownloadedException {
        final ExecutorService downloadRIRPool = Executors.newFixedThreadPool(6);

        for (final String urlString : listAllRIR) {
            downloadRIRPool.submit(new RIRDownloader(this.listDownloadedFiles, urlString));
        }

        downloadRIRPool.shutdown();

        try {
            downloadRIRPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (final InterruptedException e) {
            LOG.error("The pool to download the RIR files was interrupted before termination.", e);
        }

        if (this.listDownloadedFiles.size() != listAllRIR.size()) {
            throw new RIRNotDownloadedException("Just " + this.listDownloadedFiles.size() +
                    " RIR databases were downloaded out of " + listAllRIR.size() + ".");
        }
    }

    private void deleteFiles() {
        try {
            this.listDownloadedFiles.forEach((file) -> {
                if (!file.delete()) {
                    LOG.warn("Failed to delete temp file: {}", file.getAbsolutePath());
                }
            });
            LOG.debug("Deleted temp files.");
        } catch (final Exception e) {
            LOG.error("Problem deleting temp files.", e);
        }
    }

}
