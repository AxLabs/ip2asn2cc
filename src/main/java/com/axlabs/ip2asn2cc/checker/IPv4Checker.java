package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv4Subnet;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IPv4Checker implements IPv4CheckerInterface {

    private InetAddressValidator validator;
    private Map<IPv4Subnet, IPv4Subnet> ipv4Subnets = Collections.synchronizedMap(new HashMap<IPv4Subnet, IPv4Subnet>());

    public IPv4Checker(InetAddressValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean checkIfIsInRange(String ipAddress) {
        // if it's valid, check if the ipAddress is ipv4
        if (this.validator.isValidInet4Address(ipAddress)) {
            // if it's ipv4, check if it's in ANY ipv4 subnet range
            for (IPv4Subnet ipv4Subnet : ipv4Subnets.keySet()) {
                SubnetUtils subnetUtils = new SubnetUtils(ipv4Subnet.getCIDR());
                subnetUtils.setInclusiveHostCount(true);
                if (subnetUtils.getInfo().isInRange(ipAddress)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized void addSubnet(IPv4Subnet ipv4Subnet) {
        this.ipv4Subnets.put(ipv4Subnet, ipv4Subnet);
    }

}
