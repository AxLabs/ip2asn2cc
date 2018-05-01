package com.axlabs.ip2asn2cc.exception;

public class RIRNotDownloadedException extends Exception {

    public RIRNotDownloadedException(String message) {
        super(message);
    }

    public RIRNotDownloadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RIRNotDownloadedException(Throwable cause) {
        super(cause);
    }

    public RIRNotDownloadedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
