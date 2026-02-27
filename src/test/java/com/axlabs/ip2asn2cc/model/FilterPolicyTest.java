package com.axlabs.ip2asn2cc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FilterPolicyTest {

    @Test
    public void testIncludeCountryCodesValue() {
        final FilterPolicy policy = FilterPolicy.INCLUDE_COUNTRY_CODES;
        assertNotNull(policy);
        assertEquals("INCLUDE_COUNTRY_CODES", policy.name());
    }

    @Test
    public void testExcludeCountryCodesValue() {
        final FilterPolicy policy = FilterPolicy.EXCLUDE_COUNTRY_CODES;
        assertNotNull(policy);
        assertEquals("EXCLUDE_COUNTRY_CODES", policy.name());
    }

    @Test
    public void testValuesMethod() {
        final FilterPolicy[] policies = FilterPolicy.values();
        assertEquals(2, policies.length);
        assertEquals(FilterPolicy.INCLUDE_COUNTRY_CODES, policies[0]);
        assertEquals(FilterPolicy.EXCLUDE_COUNTRY_CODES, policies[1]);
    }

    @Test
    public void testValueOfMethod() {
        final FilterPolicy includePolicy = FilterPolicy.valueOf("INCLUDE_COUNTRY_CODES");
        assertEquals(FilterPolicy.INCLUDE_COUNTRY_CODES, includePolicy);
        
        final FilterPolicy excludePolicy = FilterPolicy.valueOf("EXCLUDE_COUNTRY_CODES");
        assertEquals(FilterPolicy.EXCLUDE_COUNTRY_CODES, excludePolicy);
    }

}
