package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv6Subnet;

public interface IPv6CheckerInterface {

    boolean checkIfIsInRange(String ipAddress);

    String getRIRCountryCode(String ipAddress);

    void addSubnet(IPv6Subnet ipSubnet);

}
