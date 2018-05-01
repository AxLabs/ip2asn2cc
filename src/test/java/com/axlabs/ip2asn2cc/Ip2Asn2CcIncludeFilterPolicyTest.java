package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.exception.RIRNotDownloadedException;
import com.axlabs.ip2asn2cc.model.FilterPolicy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Ip2Asn2CcIncludeFilterPolicyTest {

    private static Ip2Asn2Cc ip2Asn2Cc;

    @BeforeClass
    public static void initialize() throws RIRNotDownloadedException {
        List<String> listCountryCode = new ArrayList<String>();
        listCountryCode.add("US");
        ip2Asn2Cc = new Ip2Asn2Cc(listCountryCode, FilterPolicy.INCLUDE_COUNTRY_CODES);
    }

    @Test
    public void testIPv4() {
        // it should return true since the Ip2Asn2Cc class was
        // initialized with "US" country code and the option to "include" such
        // country codes in the check
        assertTrue(ip2Asn2Cc.checkIP("8.8.8.8"));
        // It should return false, since it's an IP address from Switzerland
        assertFalse(ip2Asn2Cc.checkIP("77.109.144.219"));
    }

    @Test
    public void testIPv6() {
        assertTrue(ip2Asn2Cc.checkIP("2600:1f18:1f:db01:11af:58af:ae11:f645"));
        assertFalse(ip2Asn2Cc.checkIP("2001:1620:2777:23::2"));
    }

    @Test
    public void testASN() {
        assertTrue(ip2Asn2Cc.checkASN("3356"));
        assertFalse(ip2Asn2Cc.checkASN("13030"));
    }

}
