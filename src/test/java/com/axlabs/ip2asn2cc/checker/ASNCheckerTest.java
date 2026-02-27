package com.axlabs.ip2asn2cc.checker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ASNCheckerTest {

    private ASNChecker checker;

    @Before
    public void setUp() {
        checker = new ASNChecker();
    }

    @Test
    public void testCheckIfMatchesWithMatchingASN() {
        checker.addASN("3356");
        
        assertTrue(checker.checkIfMatches("3356"));
    }

    @Test
    public void testCheckIfMatchesWithNonMatchingASN() {
        checker.addASN("3356");
        
        assertFalse(checker.checkIfMatches("13030"));
    }

    @Test
    public void testCheckIfMatchesWithEmptyChecker() {
        assertFalse(checker.checkIfMatches("3356"));
    }

    @Test
    public void testCheckIfMatchesWithNullASN() {
        checker.addASN("3356");
        
        assertFalse(checker.checkIfMatches(null));
    }

    @Test
    public void testAddASNWithMultipleASNs() {
        checker.addASN("3356");
        checker.addASN("13030");
        checker.addASN("64512");
        
        assertTrue(checker.checkIfMatches("3356"));
        assertTrue(checker.checkIfMatches("13030"));
        assertTrue(checker.checkIfMatches("64512"));
        assertFalse(checker.checkIfMatches("12345"));
    }

    @Test
    public void testAddASNWithDuplicateASN() {
        checker.addASN("3356");
        checker.addASN("3356");
        
        assertTrue(checker.checkIfMatches("3356"));
    }

    @Test
    public void testAddASNWithEmptyString() {
        checker.addASN("");
        
        assertTrue(checker.checkIfMatches(""));
        assertFalse(checker.checkIfMatches("3356"));
    }

}
