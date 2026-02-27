package com.axlabs.ip2asn2cc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IPv6SubnetTest {

    @Test
    public void testConstructorWithoutCountryCode() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:db8::", 32);
        
        assertEquals("2001:db8::", subnet.getAddress());
        assertEquals(Integer.valueOf(32), subnet.getNetworkMask());
        assertNull(subnet.getCountryCode());
    }

    @Test
    public void testConstructorWithCountryCode() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:1620:2777::", 48, "CH");
        
        assertEquals("2001:1620:2777::", subnet.getAddress());
        assertEquals(Integer.valueOf(48), subnet.getNetworkMask());
        assertEquals("CH", subnet.getCountryCode());
    }

    @Test
    public void testGetNetworkMaskWithDifferentValues() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:db8::", 64);
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db8::", 128);
        final IPv6Subnet subnet3 = new IPv6Subnet("2001:db8::", 8);
        
        assertEquals(Integer.valueOf(64), subnet1.getNetworkMask());
        assertEquals(Integer.valueOf(128), subnet2.getNetworkMask());
        assertEquals(Integer.valueOf(8), subnet3.getNetworkMask());
    }

    @Test
    public void testGetCountryCodeWithNullValue() {
        final IPv6Subnet subnet = new IPv6Subnet("2001:db8::", 32, null);
        
        assertNull(subnet.getCountryCode());
    }

    @Test
    public void testGetAddressWithNullValue() {
        final IPv6Subnet subnet = new IPv6Subnet(null, 32, "US");
        
        assertNull(subnet.getAddress());
        assertEquals(Integer.valueOf(32), subnet.getNetworkMask());
        assertEquals("US", subnet.getCountryCode());
    }

}
