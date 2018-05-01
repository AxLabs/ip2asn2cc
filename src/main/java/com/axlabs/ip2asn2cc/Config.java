package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.model.FilterPolicy;

public class Config {

    private FilterPolicy filterPolicy;

    private Boolean includeIpv4LocalAddresses;

    private Boolean includeIpv6LocalAddresses;

    public Config(FilterPolicy filterPolicy, Boolean includeIpv4LocalAddresses, Boolean includeIpv6LocalAddresses) {
        this.filterPolicy = filterPolicy;
        this.includeIpv4LocalAddresses = includeIpv4LocalAddresses;
        this.includeIpv6LocalAddresses = includeIpv6LocalAddresses;
    }

    public FilterPolicy getFilterPolicy() {
        return filterPolicy;
    }

    public Boolean getIncludeIpv4LocalAddresses() {
        return includeIpv4LocalAddresses;
    }

    public Boolean getIncludeIpv6LocalAddresses() {
        return includeIpv6LocalAddresses;
    }
}
