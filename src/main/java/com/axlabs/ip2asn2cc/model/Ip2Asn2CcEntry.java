package com.axlabs.ip2asn2cc.model;

public class Ip2Asn2CcEntry {

    private String registry;

    private String inetFamily;

    private String address;

    private int addresses;

    private String date;

    public Ip2Asn2CcEntry(String registry, String inetFamily, String address, int addresses, String date) {
        this.registry = registry;
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
