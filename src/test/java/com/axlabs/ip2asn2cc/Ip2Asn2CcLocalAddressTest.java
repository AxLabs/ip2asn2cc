package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.checker.ASNChecker;
import com.axlabs.ip2asn2cc.checker.IPv4Checker;
import com.axlabs.ip2asn2cc.checker.IPv6Checker;
import com.axlabs.ip2asn2cc.model.FilterPolicy;
import com.axlabs.ip2asn2cc.model.IPv4Subnet;
import com.axlabs.ip2asn2cc.model.IPv6Subnet;
import com.axlabs.ip2asn2cc.rir.RIRParser;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Ip2Asn2CcLocalAddressTest {

    private IPv4Checker ipv4Checker;
    private IPv6Checker ipv6Checker;
    private ASNChecker asnChecker;

    @Before
    public void setUp() {
        final InetAddressValidator validator = new InetAddressValidator();
        ipv4Checker = new IPv4Checker(validator);
        ipv6Checker = new IPv6Checker(validator);
        asnChecker = new ASNChecker();
    }

    @Test
    public void testIPv4LocalhostHandling() throws Exception {
        // Simulate what Ip2Asn2Cc does for local addresses
        // Add local addresses as well:
        // 127.0.0.0/8 defined in https://tools.ietf.org/html/rfc3330
        final IPv4Subnet localhostIPv4 = new IPv4Subnet("127.0.0.0", 16777214);
        ipv4Checker.addSubnet(localhostIPv4);

        assertTrue(ipv4Checker.checkIfIsInRange("127.0.0.1"));
        assertTrue(ipv4Checker.checkIfIsInRange("127.0.0.2"));
        assertTrue(ipv4Checker.checkIfIsInRange("127.255.255.254"));
    }

    @Test
    public void testIPv6LocalhostHandling() throws Exception {
        // ::1/128 defined in https://tools.ietf.org/html/rfc4291
        final IPv6Subnet localhostIPv6 = new IPv6Subnet("0:0:0:0:0:0:0:1", 128);
        ipv6Checker.addSubnet(localhostIPv6);

        assertTrue(ipv6Checker.checkIfIsInRange("0:0:0:0:0:0:0:1"));
        assertTrue(ipv6Checker.checkIfIsInRange("::1"));
    }

    @Test
    public void testCheckIPWithInvalidIP() throws Exception {
        // Create a temp RIR file with test data
        final Path rirFile = Files.createTempFile("rir-test", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            // Test with invalid IPs
            assertFalse(ipv4Checker.checkIfIsInRange("invalid-ip"));
            assertFalse(ipv4Checker.checkIfIsInRange("999.999.999.999"));
            assertFalse(ipv4Checker.checkIfIsInRange(""));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testGetRIRCountryCodeWithValidIPs() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "ripe|CH|ipv6|2001:1620:2777::|48|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US", "CH")).run();

            assertEquals("US", ipv4Checker.getRIRCountryCode("8.8.8.8"));
            assertEquals("CH", ipv6Checker.getRIRCountryCode("2001:1620:2777:23::2"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testGetRIRCountryCodeWithInvalidIP() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            assertNull(ipv4Checker.getRIRCountryCode("invalid-ip"));
            assertNull(ipv4Checker.getRIRCountryCode("999.999.999.999"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testCheckIPWithNullIP() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            assertFalse(ipv4Checker.checkIfIsInRange(null));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

}
