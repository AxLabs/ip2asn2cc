package com.axlabs.ip2asn2cc.rir;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RIRDownloaderTest {

    @Test
    public void testDownloaderWithInvalidURL() {
        // Test that downloader handles invalid URLs gracefully
        final List<File> downloadedFiles = new ArrayList<File>();
        final String invalidUrl = "ftp://invalid-url-that-does-not-exist.example.com/file.txt";
        
        final RIRDownloader downloader = new RIRDownloader(downloadedFiles, invalidUrl);
        
        // Run the downloader - it should catch the exception and not add to the list
        downloader.run();
        
        // The file should not be added to the list since download failed
        assertEquals(0, downloadedFiles.size());
    }

    @Test
    public void testDownloaderWithMalformedURL() {
        final List<File> downloadedFiles = new ArrayList<File>();
        final String malformedUrl = "not-a-valid-url";
        
        final RIRDownloader downloader = new RIRDownloader(downloadedFiles, malformedUrl);
        downloader.run();
        
        // The file should not be added to the list since URL is malformed
        assertEquals(0, downloadedFiles.size());
    }

    @Test
    public void testDownloaderConstructor() {
        final List<File> downloadedFiles = new ArrayList<File>();
        final String url = "ftp://example.com/file.txt";
        
        final RIRDownloader downloader = new RIRDownloader(downloadedFiles, url);
        
        // Constructor should not throw any exceptions
        assertEquals(0, downloadedFiles.size());
    }

}
