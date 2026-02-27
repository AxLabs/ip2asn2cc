package com.axlabs.ip2asn2cc;

import com.axlabs.ip2asn2cc.model.FilterPolicy;

public class Config {

    private final FilterPolicy filterPolicy;

    private final Boolean includeIpv4LocalAddresses;

    private final Boolean includeIpv6LocalAddresses;

    public Config(final FilterPolicy filterPolicy, final Boolean includeIpv4LocalAddresses, final Boolean includeIpv6LocalAddresses) {
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
