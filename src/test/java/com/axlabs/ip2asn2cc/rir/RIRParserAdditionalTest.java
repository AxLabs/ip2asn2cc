package com.axlabs.ip2asn2cc.rir;

import com.axlabs.ip2asn2cc.checker.ASNChecker;
import com.axlabs.ip2asn2cc.checker.IPv4Checker;
import com.axlabs.ip2asn2cc.checker.IPv6Checker;
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

public class RIRParserAdditionalTest {

    private IPv4Checker ipv4Checker;
    private IPv6Checker ipv6Checker;
    private ASNChecker asnChecker;
    private InetAddressValidator validator;

    @Before
    public void setUp() {
        validator = new InetAddressValidator();
        ipv4Checker = new IPv4Checker(validator);
        ipv6Checker = new IPv6Checker(validator);
        asnChecker = new ASNChecker();
    }

    @Test
    public void testParseASNEntries() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-asn", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|asn|3356|1|20100101|allocated",
                    "ripe|CH|asn|13030|1|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US", "CH")).run();

            assertTrue(asnChecker.checkIfMatches("3356"));
            assertTrue(asnChecker.checkIfMatches("13030"));
            assertFalse(asnChecker.checkIfMatches("12345"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseMixedEntries() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-mixed", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "arin|US|asn|3356|1|20100101|allocated",
                    "ripe|CH|ipv6|2001:1620:2777::|48|20100101|allocated",
                    "ripe|CH|asn|13030|1|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US", "CH")).run();

            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
            assertTrue(ipv6Checker.checkIfIsInRange("2001:1620:2777:23::2"));
            assertTrue(asnChecker.checkIfMatches("3356"));
            assertTrue(asnChecker.checkIfMatches("13030"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseWithUnmatchedCountryCode() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-unmatched", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "ripe|GB|ipv4|77.109.144.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            // Only parse US, not GB
            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
            assertFalse(ipv4Checker.checkIfIsInRange("77.109.144.1"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseWithAssignedStatus() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-assigned", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|assigned",
                    "ripe|CH|ipv6|2001:1620:2777::|48|20100101|assigned"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US", "CH")).run();

            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
            assertTrue(ipv6Checker.checkIfIsInRange("2001:1620:2777:23::2"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseWithInvalidLines() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-invalid", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "# This is a comment",
                    "invalid line without proper format",
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "another invalid line",
                    ""
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            // Valid line should still be parsed
            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseEmptyFile() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-empty", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            assertFalse(ipv4Checker.checkIfIsInRange("8.8.8.8"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseMultipleCountryCodes() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-multi", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "ripe|CH|ipv4|77.109.144.0|256|20100101|allocated",
                    "ripe|GB|ipv4|212.58.244.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), 
                    Arrays.asList("US", "CH", "GB")).run();

            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
            assertEquals("US", ipv4Checker.getRIRCountryCode("8.8.8.8"));
            
            assertTrue(ipv4Checker.checkIfIsInRange("77.109.144.1"));
            assertEquals("CH", ipv4Checker.getRIRCountryCode("77.109.144.1"));
            
            assertTrue(ipv4Checker.checkIfIsInRange("212.58.244.1"));
            assertEquals("GB", ipv4Checker.getRIRCountryCode("212.58.244.1"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

    @Test
    public void testParseLowercaseCountryCode() throws Exception {
        final Path rirFile = Files.createTempFile("rir-test-lowercase", ".txt");
        try {
            // RIR files should have uppercase country codes, but test lowercase handling
            // The parser converts to uppercase in the pattern, so lowercase in file won't match
            Files.write(rirFile, Arrays.asList(
                    "arin|us|ipv4|8.8.8.0|256|20100101|allocated"
            ), StandardCharsets.UTF_8);

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US")).run();

            // Lowercase "us" won't match "US" pattern, so should not be in range
            assertFalse(ipv4Checker.checkIfIsInRange("8.8.8.8"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }

}
