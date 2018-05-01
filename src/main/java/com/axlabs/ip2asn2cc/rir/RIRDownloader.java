package com.axlabs.ip2asn2cc.rir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class RIRDownloader implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RIRDownloader.class);

    private List<File> listDownloadedFiles;
    private String urlToDownload;

    public RIRDownloader(List<File> listDownloadedFiles, String urlToDownload) {
        this.listDownloadedFiles = listDownloadedFiles;
        this.urlToDownload = urlToDownload;
    }

    @Override
    public void run() {
        try {
            File fileDownloaded = download(this.urlToDownload);
            this.listDownloadedFiles.add(fileDownloaded);
        } catch (Exception e) {
            LOG.error("Error downloading the RIR file from URL (" + this.urlToDownload + ").", e);
        }
    }

    private File download(String urlString) throws Exception {
        URL url = new URL(urlString);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        File tempFile = File.createTempFile("ip2asn2cc-", ".db");
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        LOG.debug("Downloaded RIR file: {}", urlString);
        return tempFile;
    }
}
