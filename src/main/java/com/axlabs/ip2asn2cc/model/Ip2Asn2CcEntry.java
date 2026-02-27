package com.axlabs.ip2asn2cc.model;

public class Ip2Asn2CcEntry {

    private final String registry;

    private final String inetFamily;

    private final String countryCode;

    private final String address;

    private final int addresses;

    private final String date;

    public Ip2Asn2CcEntry(final String registry, final String countryCode, final String inetFamily, final String address, final int addresses, final String date) {
        this.registry = registry;
        this.countryCode = countryCode;
        this.inetFamily = inetFamily;
        this.address = address;
        this.addresses = addresses;
        this.date = date;
    }

    public String getRegistry() {
        return registry;
    }

    public String getInetFamily() {
        return inetFamily;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getAddress() {
        return address;
    }

    public int getAddresses() {
        return addresses;
    }

    public String getDate() {
        return date;
    }

}
