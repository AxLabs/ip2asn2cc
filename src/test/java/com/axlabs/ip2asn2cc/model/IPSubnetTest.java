package com.axlabs.ip2asn2cc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class IPSubnetTest {

    @Test
    public void testConstructorAndGetAddress() {
        final IPSubnet subnet = new IPSubnet("192.168.0.0");
        
        assertEquals("192.168.0.0", subnet.getAddress());
    }

    @Test
    public void testConstructorWithNullAddress() {
        final IPSubnet subnet = new IPSubnet(null);
        
        assertEquals(null, subnet.getAddress());
    }

    @Test
    public void testEqualsWithSameObject() {
        final IPSubnet subnet = new IPSubnet("192.168.0.0");
        
        assertTrue(subnet.equals(subnet));
    }

    @Test
    public void testEqualsWithNull() {
        final IPSubnet subnet = new IPSubnet("192.168.0.0");
        
        assertFalse(subnet.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        final IPSubnet subnet = new IPSubnet("192.168.0.0");
        final String other = "192.168.0.0";
        
        assertFalse(subnet.equals(other));
    }

    @Test
    public void testEqualsWithSameAddress() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:db8::", 32);
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db8::", 32);
        
        assertTrue(subnet1.equals(subnet2));
    }

    @Test
    public void testEqualsWithDifferentAddress() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:db8::", 32);
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db9::", 32);
        
        assertFalse(subnet1.equals(subnet2));
    }

    @Test
    public void testEqualsWithNullAddress() {
        final IPv6Subnet subnet1 = new IPv6Subnet(null, 32);
        final IPv6Subnet subnet2 = new IPv6Subnet(null, 32);
        
        // Both have null addresses, should be equal
        assertTrue(subnet1.equals(subnet2));
    }

    @Test
    public void testHashCodeConsistency() {
        final IPSubnet subnet = new IPSubnet("192.168.0.0");
        
        final int hash1 = subnet.hashCode();
        final int hash2 = subnet.hashCode();
        
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashCodeWithSameAddress() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:db8::", 32);
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db8::", 32);
        
        assertEquals(subnet1.hashCode(), subnet2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentAddress() {
        final IPv6Subnet subnet1 = new IPv6Subnet("2001:db8::", 32);
        final IPv6Subnet subnet2 = new IPv6Subnet("2001:db9::", 32);
        
        assertNotEquals(subnet1.hashCode(), subnet2.hashCode());
    }

}
