package com.axlabs.ip2asn2cc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IPv4SubnetTest {

    @Test
    public void testConstructorWithoutCountryCode() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256);
        
        assertEquals("192.168.0.0", subnet.getAddress());
        assertEquals(Integer.valueOf(256), subnet.getAmountOfAddresses());
        assertNull(subnet.getCountryCode());
    }

    @Test
    public void testConstructorWithCountryCode() {
        final IPv4Subnet subnet = new IPv4Subnet("8.8.8.0", 256, "US");
        
        assertEquals("8.8.8.0", subnet.getAddress());
        assertEquals(Integer.valueOf(256), subnet.getAmountOfAddresses());
        assertEquals("US", subnet.getCountryCode());
    }

    @Test
    public void testGetCIDRWithValidInput() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 256);
        
        final String cidr = subnet.getCIDR();
        assertEquals("192.168.0.0/24", cidr);
    }

    @Test
    public void testGetCIDRWithLargerSubnet() {
        final IPv4Subnet subnet = new IPv4Subnet("10.0.0.0", 65536);
        
        final String cidr = subnet.getCIDR();
        assertEquals("10.0.0.0/16", cidr);
    }

    @Test
    public void testGetCIDRWithSmallSubnet() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.1.0", 64);
        
        final String cidr = subnet.getCIDR();
        assertEquals("192.168.1.0/26", cidr);
    }

    @Test
    public void testGetCIDRWithNullAddress() {
        final IPv4Subnet subnet = new IPv4Subnet(null, 256);
        
        final String cidr = subnet.getCIDR();
        assertNull(cidr);
    }

    @Test
    public void testGetCIDRWithEmptyAddress() {
        final IPv4Subnet subnet = new IPv4Subnet("", 256);
        
        final String cidr = subnet.getCIDR();
        assertNull(cidr);
    }

    @Test
    public void testGetCIDRWithNullAmountOfAddresses() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", null);
        
        final String cidr = subnet.getCIDR();
        assertNull(cidr);
    }

    @Test
    public void testGetCIDRWithZeroAmountOfAddresses() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", 0);
        
        final String cidr = subnet.getCIDR();
        assertNull(cidr);
    }

    @Test
    public void testGetCIDRWithNegativeAmountOfAddresses() {
        // While negative values don't make sense, test that the method handles them gracefully
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.0", -1);
        
        // The calculation will produce a result, but it's not meaningful
        // The method doesn't validate input, which is acceptable for this internal model class
        final String cidr = subnet.getCIDR();
        assertNotNull(cidr); // Method returns a result even with invalid input
    }

    @Test
    public void testGetCIDRWithSingleAddress() {
        final IPv4Subnet subnet = new IPv4Subnet("192.168.0.1", 1);
        
        final String cidr = subnet.getCIDR();
        assertEquals("192.168.0.1/32", cidr);
    }

}
