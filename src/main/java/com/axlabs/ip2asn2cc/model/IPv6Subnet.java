package com.axlabs.ip2asn2cc.model;

public class IPv6Subnet extends IPSubnet {

    // for IPv6::
    // * address: address (e.g., 2001:618::)
    // * networkMask: the ipv6 network mask on the CIDR format (e.g., 32 -- which means /32)

    private Integer networkMask;

    public IPv6Subnet(String address, Integer networkMask) {
        super(address);
        this.networkMask = networkMask;
    }

    public Integer getNetworkMask() {
        return networkMask;
    }

}
