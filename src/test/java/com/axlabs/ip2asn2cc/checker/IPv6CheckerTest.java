package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv6Subnet;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IPv6CheckerTest {

    private IPv6Checker checker;
    private InetAddressValidator validator;

    @Before
    public void setUp() {
        validator = new InetAddressValidator();
        checker = new IPv6Checker(validator);
    }

    @Test
    public void testCheckIfIsInRangeWithMatchingIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertTrue(checker.checkIfIsInRange("2001:1620:2777:23::2"));
        assertTrue(checker.checkIfIsInRange("2001:1620:2777:ffff::1"));
    }

    @Test
    public void testCheckIfIsInRangeWithNonMatchingIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("2001:1620:2778::1"));
        assertFalse(checker.checkIfIsInRange("2001:db8::1"));
    }

    @Test
    public void testCheckIfIsInRangeWithInvalidIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("invalid-ip"));
        assertFalse(checker.checkIfIsInRange("gggg:gggg::1"));
    }

    @Test
    public void testCheckIfIsInRangeWithIPv4Address() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("192.168.0.1"));
    }

    @Test(expected = NullPointerException.class)
    public void testCheckIfIsInRangeWithNullIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        checker.checkIfIsInRange(null);
    }

    @Test
    public void testCheckIfIsInRangeWithEmptySubnets() {
        assertFalse(checker.checkIfIsInRange("2001:1620:2777:23::2"));
    }

    @Test
    public void testGetRIRCountryCodeWithMatchingIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertEquals("CH", checker.getRIRCountryCode("2001:1620:2777:23::2"));
    }

    @Test
    public void testGetRIRCountryCodeWithNonMatchingIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("2001:db8::1"));
    }

    @Test
    public void testGetRIRCountryCodeWithInvalidIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("invalid-ip"));
    }

    @Test
    public void testGetRIRCountryCodeWithIPv4Address() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("192.168.0.1"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetRIRCountryCodeWithNullIP() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        checker.addSubnet(subnet);
        
        checker.getRIRCountryCode(null);
    }

    @Test
    public void testAddSubnetWithMultipleSubnets() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db8::", 32, "US");
        
        checker.addSubnet(subnet1);
        checker.addSubnet(subnet2);
        
        assertTrue(checker.checkIfIsInRange("2001:1620:2777:23::2"));
        assertTrue(checker.checkIfIsInRange("2001:db8::1"));
        assertEquals("CH", checker.getRIRCountryCode("2001:1620:2777:23::2"));
        assertEquals("US", checker.getRIRCountryCode("2001:db8::1"));
    }

    @Test
    public void testCheckIfIsInRangeWithLargeSubnet() {
        final IPv6Subnet subnet = new IPv6Subnet("2001::", 16, "US");
        checker.addSubnet(subnet);
        
        assertTrue(checker.checkIfIsInRange("2001::1"));
        assertTrue(checker.checkIfIsInRange("2001:ffff:ffff:ffff::1"));
        assertFalse(checker.checkIfIsInRange("2002::1"));
    }

}
