package com.axlabs.ip2asn2cc.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RIRNotDownloadedExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        final String message = "Test error message";
        final RIRNotDownloadedException exception = new RIRNotDownloadedException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        final String message = "Test error message";
        final Throwable cause = new RuntimeException("Cause exception");
        final RIRNotDownloadedException exception = new RIRNotDownloadedException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        final Throwable cause = new RuntimeException("Cause exception");
        final RIRNotDownloadedException exception = new RIRNotDownloadedException(cause);
        
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("RuntimeException"));
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithAllParameters() {
        final String message = "Test error message";
        final Throwable cause = new RuntimeException("Cause exception");
        final RIRNotDownloadedException exception = new RIRNotDownloadedException(
                message, cause, true, true);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
