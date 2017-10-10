package com.fieldnation;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mc on 10/5/17.
 */

public class InputStreamMonitor extends InputStream {

    public interface Monitor {
        void progress(int bytesRead);
    }

    private InputStream source;
    private Monitor monitor;

    private int bytesRead = 0;

    public InputStreamMonitor(InputStream inputStream, Monitor monitor) {
        this.source = inputStream;
        this.monitor = monitor;
    }


    @Override
    public int read() throws IOException {
        int val = source.read();

        if (val != -1) {
            bytesRead++;
            if (monitor != null) monitor.progress(bytesRead);
        }

        return val;
    }

    @Override
    public int read(@NonNull byte[] b) throws IOException {
        int count = source.read(b);

        if (count != -1) {
            bytesRead += count;
            if (monitor != null) monitor.progress(bytesRead);
        }
        return count;
    }

    @Override
    public int read(@NonNull byte[] b, int off, int len) throws IOException {
        int count = source.read(b, off, len);

        if (count != -1) {
            bytesRead += count;
            if (monitor != null) monitor.progress(bytesRead);
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        return source.skip(n);
    }

    @Override
    public int available() throws IOException {
        return source.available();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public boolean markSupported() {
        return source.markSupported();
    }

    @Override
    public synchronized void mark(int readlimit) {
        source.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        source.reset();
    }

}
