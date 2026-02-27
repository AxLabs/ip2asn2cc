package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv4Subnet;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IPv4CheckerTest {

    private IPv4Checker checker;
    private InetAddressValidator validator;

    @Before
    public void setUp() {
        validator = new InetAddressValidator();
        checker = new IPv4Checker(validator);
    }

    @Test
    public void testCheckIfIsInRangeWithMatchingIP() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertTrue(checker.checkIfIsInRange("192.168.0.1"));
        assertTrue(checker.checkIfIsInRange("192.168.0.255"));
    }

    @Test
    public void testCheckIfIsInRangeWithNonMatchingIP() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("192.168.1.1"));
        assertFalse(checker.checkIfIsInRange("10.0.0.1"));
    }

    @Test
    public void testCheckIfIsInRangeWithInvalidIP() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("invalid-ip"));
        assertFalse(checker.checkIfIsInRange("999.999.999.999"));
    }

    @Test
    public void testCheckIfIsInRangeWithIPv6Address() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange("2001:db8::1"));
    }

    @Test
    public void testCheckIfIsInRangeWithNullIP() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertFalse(checker.checkIfIsInRange(null));
    }

    @Test
    public void testCheckIfIsInRangeWithEmptySubnets() {
        assertFalse(checker.checkIfIsInRange("192.168.0.1"));
    }

    @Test
    public void testGetRIRCountryCodeWithMatchingIP() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertEquals("US", checker.getRIRCountryCode("8.8.8.8"));
    }

    @Test
    public void testGetRIRCountryCodeWithNonMatchingIP() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("1.1.1.1"));
    }

    @Test
    public void testGetRIRCountryCodeWithInvalidIP() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("invalid-ip"));
    }

    @Test
    public void testGetRIRCountryCodeWithIPv6Address() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode("2001:db8::1"));
    }

    @Test
    public void testGetRIRCountryCodeWithNullIP() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        checker.addSubnet(subnet);
        
        assertNull(checker.getRIRCountryCode(null));
    }

    @Test
    public void testAddSubnetWithMultipleSubnets() {
        final IPv4Subnet subnet1 = new IPv4Subnet("192.168.0.0", 256, "US");
        final IPv4Subnet subnet2 = new IPv4Subnet("10.0.0.0", 256, "GB");
        
        checker.addSubnet(subnet1);
        checker.addSubnet(subnet2);
        
        assertTrue(checker.checkIfIsInRange("192.168.0.1"));
        assertTrue(checker.checkIfIsInRange("10.0.0.1"));
        assertEquals("US", checker.getRIRCountryCode("192.168.0.1"));
        assertEquals("GB", checker.getRIRCountryCode("10.0.0.1"));
    }

    @Test
    public void testCheckIfIsInRangeWithLargeSubnet() {
        final IPv4Subnet subnet = new IPv4Subnet("10.0.0.0", 65536, "US");
        checker.addSubnet(subnet);
        
        assertTrue(checker.checkIfIsInRange("10.0.0.1"));
        assertTrue(checker.checkIfIsInRange("10.0.255.255"));
        assertFalse(checker.checkIfIsInRange("10.1.0.0"));
    }

}
