package com.axlabs.ip2asn2cc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Ip2Asn2CcEntryTest {

    @Test
    public void testConstructorAndGetters() {
        final Ip2Asn2CcEntry entry = new Ip2Asn2CcEntry(
                "arin", "US", "ipv4", "8.8.8.0", 256, "20100101");
        
        assertEquals("arin", entry.getRegistry());
        assertEquals("US", entry.getCountryCode());
        assertEquals("ipv4", entry.getInetFamily());
        assertEquals("8.8.8.0", entry.getAddress());
        assertEquals(256, entry.getAddresses());
        assertEquals("20100101", entry.getDate());
    }

    @Test
    public void testConstructorWithIPv6() {
        final Ip2Asn2CcEntry entry = new Ip2Asn2CcEntry(
                "ripe", "CH", "ipv6", "2001:1620:2777::", 48, "20100101");
        
        assertEquals("ripe", entry.getRegistry());
        assertEquals("CH", entry.getCountryCode());
        assertEquals("ipv6", entry.getInetFamily());
        assertEquals("2001:1620:2777::", entry.getAddress());
        assertEquals(48, entry.getAddresses());
        assertEquals("20100101", entry.getDate());
    }

    @Test
    public void testConstructorWithASN() {
        final Ip2Asn2CcEntry entry = new Ip2Asn2CcEntry(
                "apnic", "AU", "asn", "4608", 1, "20030411");
        
        assertEquals("apnic", entry.getRegistry());
        assertEquals("AU", entry.getCountryCode());
        assertEquals("asn", entry.getInetFamily());
        assertEquals("4608", entry.getAddress());
        assertEquals(1, entry.getAddresses());
        assertEquals("20030411", entry.getDate());
    }

    @Test
    public void testConstructorWithNullValues() {
        final Ip2Asn2CcEntry entry = new Ip2Asn2CcEntry(
                null, null, null, null, 0, null);
        
        assertEquals(null, entry.getRegistry());
        assertEquals(null, entry.getCountryCode());
        assertEquals(null, entry.getInetFamily());
        assertEquals(null, entry.getAddress());
        assertEquals(0, entry.getAddresses());
        assertEquals(null, entry.getDate());
    }

}
