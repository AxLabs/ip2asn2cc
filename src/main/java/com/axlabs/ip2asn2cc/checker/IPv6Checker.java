package com.axlabs.ip2asn2cc.checker;

import com.axlabs.ip2asn2cc.model.IPv6Subnet;
import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;
import com.googlecode.ipv6.IPv6Network;
import com.googlecode.ipv6.IPv6NetworkMask;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IPv6Checker implements IPv6CheckerInterface {

    private InetAddressValidator validator;
    private Map<IPv6Subnet, IPv6Subnet> ipv6Subnets = Collections.synchronizedMap(new HashMap<IPv6Subnet, IPv6Subnet>());

    public IPv6Checker(InetAddressValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean checkIfIsInRange(String ipAddress) {
        // if it's valid, check if the ipAddress is ipv6
        if (validator.isValidInet6Address(ipAddress)) {
            // if it's ipv6, check if it's in ANY ipv6 subnet range
            for (IPv6Subnet ipv6Subnet : ipv6Subnets.keySet()) {
                IPv6Address ipv6AddressFromClient = IPv6Address.fromString(ipAddress);
                IPv6Address ipv6AddressFromEntry = IPv6Address.fromString(ipv6Subnet.getAddress());
                IPv6NetworkMask ipv6NetworkMask = IPv6NetworkMask.fromPrefixLength(ipv6Subnet.getNetworkMask());
                IPv6AddressRange range = IPv6Network.fromAddressAndMask(ipv6AddressFromEntry, ipv6NetworkMask);
                if (range.contains(ipv6AddressFromClient)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized void addSubnet(IPv6Subnet ipv6Subnet) {
        this.ipv6Subnets.put(ipv6Subnet, ipv6Subnet);
    }

}
