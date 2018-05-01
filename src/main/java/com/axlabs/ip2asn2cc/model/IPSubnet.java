package com.axlabs.ip2asn2cc.model;

import java.util.Objects;

public class IPSubnet {

    protected String address;

    public IPSubnet(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPv6Subnet that = (IPv6Subnet) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(address);
    }

}
