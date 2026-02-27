package com.axlabs.ip2asn2cc.exception;

public class RIRNotDownloadedException extends Exception {

    public RIRNotDownloadedException(final String message) {
        super(message);
    }

    public RIRNotDownloadedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RIRNotDownloadedException(final Throwable cause) {
        super(cause);
    }

    public RIRNotDownloadedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
