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
    private final static String ARIN_RIR_DB_URL = "ftp://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest";
    private final static String RIPE_RIR_DB_URL = "ftp://ftp.ripe.net/ripe/stats/delegated-ripencc-latest";
    private final static String AFRINIC_RIR_DB_URL = "ftp://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest";
    private final static String APNIC_RIR_DB_URL = "ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-latest";
    private final static String LACNIC_RIR_DB_URL = "ftp://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest";

    private final static List<String> listAllRIR = Arrays.asList(
            ARIN_RIR_DB_URL, RIPE_RIR_DB_URL,
            AFRINIC_RIR_DB_URL, APNIC_RIR_DB_URL,
            LACNIC_RIR_DB_URL);

    private InetAddressValidator validator;

    private IPv4Checker ipv4Checker;
    private IPv6Checker ipv6Checker;
    private ASNChecker asnChecker;

    private List<File> listDownloadedFiles = Collections.synchronizedList(new ArrayList<File>());
    private Config config;

    public Ip2Asn2Cc(List<String> listCountryCodeRules) throws RIRNotDownloadedException {
        this(listCountryCodeRules, FilterPolicy.INCLUDE_COUNTRY_CODES, true, true);
    }

    public Ip2Asn2Cc(List<String> listCountryCodeRules, FilterPolicy filterPolicy) throws RIRNotDownloadedException {
        this(listCountryCodeRules, filterPolicy, true, true);
    }

    public Ip2Asn2Cc(List<String> listCountryCodeRules, FilterPolicy filterPolicy,
                     Boolean includeIpv4LocalAddresses, Boolean includeIpv6LocalAddresses) throws RIRNotDownloadedException {

        this.config = new Config(filterPolicy, includeIpv4LocalAddresses, includeIpv6LocalAddresses);
        this.validator = new InetAddressValidator();
        this.ipv4Checker = new IPv4Checker(this.validator);
        this.ipv6Checker = new IPv6Checker(this.validator);
        this.asnChecker = new ASNChecker();
        downloadListUrl();
        parseAllCountryCodes(listCountryCodeRules);
        deleteFiles();
    }

    public synchronized boolean checkIP(String ipAddress) {
        LOG.debug("Check for: " + ipAddress);
        return ofNullable(ipAddress)
                .filter((address) -> validator.isValid(address))
                .map((address) -> new Boolean(this.ipv4Checker.checkIfIsInRange(address) || this.ipv6Checker.checkIfIsInRange(address)))
                .map((checkResult) -> (this.config.getFilterPolicy() == FilterPolicy.INCLUDE_COUNTRY_CODES) ? checkResult : !checkResult)
                .orElse(false);
    }

    public synchronized boolean checkASN(String asn) {
        LOG.debug("Check for: " + asn);
        return ofNullable(asn).map((asnAddress) -> this.asnChecker.checkIfMatches(asnAddress))
                .map((checkResult) -> (this.config.getFilterPolicy() == FilterPolicy.INCLUDE_COUNTRY_CODES) ? checkResult : !checkResult)
                .orElse(false);
    }

    public synchronized String getRIRCountryCode(String ipAddress) {
        LOG.debug("Get RIR country code for: " + ipAddress);
        return ofNullable(ipAddress)
                .filter((address) -> validator.isValid(address))
                .map((address) -> this.validator.isValidInet4Address(address) ?
                        this.ipv4Checker.getRIRCountryCode(address) : this.ipv6Checker.getRIRCountryCode(address))
                .orElse(null);
    }

    private void parseAllCountryCodes(List<String> listCountryCode) {

        ExecutorService parserPool = Executors.newFixedThreadPool(6);
        this.listDownloadedFiles.forEach((file) -> {
            parserPool.submit(new RIRParser(this.ipv4Checker, this.ipv6Checker, this.asnChecker, file, listCountryCode));
        });

        parserPool.shutdown();

        try {
            parserPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOG.error("The pool to parse the RIR files was interrupted before termination.", e);
        }

        if (this.config.getIncludeIpv4LocalAddresses()) {
            // add local addresses as well:
            // 127.0.0.0/8 defined in https://tools.ietf.org/html/rfc3330
            IPv4Subnet localhostIPv4 = new IPv4Subnet("127.0.0.0", 16777214);
            ipv4Checker.addSubnet(localhostIPv4);
        }
        if (this.config.getIncludeIpv6LocalAddresses()) {
            // ::1/128 defined in https://tools.ietf.org/html/rfc4291
            IPv6Subnet localhostIPv6 = new IPv6Subnet("0:0:0:0:0:0:0:1", 128);
            ipv6Checker.addSubnet(localhostIPv6);
        }

        LOG.debug("Parsed all RIR files");
    }

    private void downloadListUrl() throws RIRNotDownloadedException {
        ExecutorService downloadRIRPool = Executors.newFixedThreadPool(6);

        for (String urlString : this.listAllRIR) {
            downloadRIRPool.submit(new RIRDownloader(this.listDownloadedFiles, urlString));
        }

        downloadRIRPool.shutdown();

        try {
            downloadRIRPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOG.error("The pool to download the RIR files was interrupted before termination.", e);
        }

        if (this.listDownloadedFiles.size() != this.listAllRIR.size()) {
            throw new RIRNotDownloadedException("Just " + this.listDownloadedFiles.size() +
                    " RIR databases were downloaded out of " + this.listAllRIR.size() + ".");
        }
    }

    private void deleteFiles() {
        try {
            this.listDownloadedFiles.stream().map((file) -> file.delete());
            LOG.debug("Deleted temp files.");
        } catch (Exception e) {
            LOG.error("Problem deleting temp files.");
        }
    }

}
