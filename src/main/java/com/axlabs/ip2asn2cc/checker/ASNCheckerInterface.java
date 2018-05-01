package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv4Subnet;

public interface ASNCheckerInterface {

    boolean checkIfMatches(String asn);

    void addASN(String asn);

}
