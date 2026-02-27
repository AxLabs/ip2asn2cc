package com.axlabs.ip2asn2cc.rir;

import com.axlabs.ip2asn2cc.checker.ASNChecker;
import com.axlabs.ip2asn2cc.checker.IPv4Checker;
import com.axlabs.ip2asn2cc.checker.IPv6Checker;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RIRParserCountryCodeLookupTest {

    @Test
    public void shouldReturnCountryCodeForIPv4AndIPv6Ranges() throws Exception {
        Path rirFile = Files.createTempFile("rir-test", ".txt");
        try {
            Files.write(rirFile, Arrays.asList(
                    "arin|US|ipv4|8.8.8.0|256|20100101|allocated",
                    "ripe|CH|ipv6|2001:1620:2777::|48|20100101|allocated"
            ), StandardCharsets.UTF_8);

            InetAddressValidator validator = new InetAddressValidator();
            IPv4Checker ipv4Checker = new IPv4Checker(validator);
            IPv6Checker ipv6Checker = new IPv6Checker(validator);
            ASNChecker asnChecker = new ASNChecker();

            new RIRParser(ipv4Checker, ipv6Checker, asnChecker, rirFile.toFile(), Arrays.asList("US", "CH")).run();

            assertEquals("US", ipv4Checker.getRIRCountryCode("8.8.8.8"));
            assertEquals("CH", ipv6Checker.getRIRCountryCode("2001:1620:2777:23::2"));
            assertNull(ipv4Checker.getRIRCountryCode("1.1.1.1"));
            assertTrue(ipv4Checker.checkIfIsInRange("8.8.8.8"));
        } finally {
            Files.deleteIfExists(rirFile);
        }
    }
}
