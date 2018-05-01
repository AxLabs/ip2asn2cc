package com.axlabs.ip2asn2cc.checker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ASNChecker implements ASNCheckerInterface {

    private Set<String> asns = Collections.synchronizedSet(new HashSet<String>());

    public ASNChecker() {
    }

    @Override
    public boolean checkIfMatches(String asn) {
        return this.asns.contains(asn);
    }

    @Override
    public void addASN(String asn) {
        this.asns.add(asn);
    }

}
