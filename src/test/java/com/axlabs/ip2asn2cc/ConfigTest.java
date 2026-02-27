package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.model.FilterPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {

    @Test
    public void testConfigWithIncludeFilterPolicy() {
        final Config config = new Config(FilterPolicy.INCLUDE_COUNTRY_CODES, true, true);
        
        assertEquals(FilterPolicy.INCLUDE_COUNTRY_CODES, config.getFilterPolicy());
        assertTrue(config.getIncludeIpv4LocalAddresses());
        assertTrue(config.getIncludeIpv6LocalAddresses());
    }

    @Test
    public void testConfigWithExcludeFilterPolicy() {
        final Config config = new Config(FilterPolicy.EXCLUDE_COUNTRY_CODES, false, false);
        
        assertEquals(FilterPolicy.EXCLUDE_COUNTRY_CODES, config.getFilterPolicy());
        assertFalse(config.getIncludeIpv4LocalAddresses());
        assertFalse(config.getIncludeIpv6LocalAddresses());
    }

    @Test
    public void testConfigWithMixedLocalAddressSettings() {
        final Config config = new Config(FilterPolicy.INCLUDE_COUNTRY_CODES, true, false);
        
        assertEquals(FilterPolicy.INCLUDE_COUNTRY_CODES, config.getFilterPolicy());
        assertTrue(config.getIncludeIpv4LocalAddresses());
        assertFalse(config.getIncludeIpv6LocalAddresses());
    }

    @Test
    public void testConfigWithDifferentMixedLocalAddressSettings() {
        final Config config = new Config(FilterPolicy.EXCLUDE_COUNTRY_CODES, false, true);
        
        assertEquals(FilterPolicy.EXCLUDE_COUNTRY_CODES, config.getFilterPolicy());
        assertFalse(config.getIncludeIpv4LocalAddresses());
        assertTrue(config.getIncludeIpv6LocalAddresses());
    }

}
