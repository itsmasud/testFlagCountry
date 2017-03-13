package com.fieldnation.fnhttpjson;

import android.net.Uri;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 *
 * @author www.codejava.net
 */
public class MultipartUtility {
    private static final String TAG = "MultipartUtility";

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private final HttpURLConnection httpConn;
    private final String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type is
     * set to multipart/form-data
     *
     * @param httpConnection
     * @param charset
     * @throws IOException
     */
    /*- from: http://www.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically -*/
    public MultipartUtility(HttpURLConnection httpConnection, String charset) throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        httpConn = httpConnection;
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        httpConn.setChunkedStreamingMode(1024);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a form field to the request
     *
     * @param name        field name
     * @param value       field value
     * @param contentType The content type of the value
     */
    public void addFormField(String name, String value, String contentType) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(contentType).append("; charset=").append(charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String fieldName, String filename, InputStream inputStream, final int length, final HttpJson.ProgressListener listener) throws IOException {
        Log.v(TAG, "addFilePart(" + fieldName + "," + filename + "," + length + ","
                + FileUtils.guessContentTypeFromName(filename) + ")");

        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"").append(filename).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(FileUtils.guessContentTypeFromName(filename)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);

        if (length > 0) {
            writer.append("Content-Length: " + length).append(LINE_FEED);
        }
        writer.append(LINE_FEED);
        writer.flush();

        final long startTime = System.currentTimeMillis();
        Stopwatch stopwatch = new Stopwatch(true);
        Log.v(TAG, "Start upload....");

        if (listener != null) {
            StreamUtils.copyStream(inputStream, outputStream, length, 1000, new StreamUtils.ProgressListener() {
                @Override
                public void progress(int position) {
                    if (listener != null)
                        listener.progress(position, length, System.currentTimeMillis() - startTime);
                }
            });
        } else {
            StreamUtils.copyStream(inputStream, outputStream, length, 1000, null);
        }
        Log.v(TAG, "Finish upload...." + stopwatch.finish());
        outputStream.flush();

        writer.append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String fieldName, String filename, Uri uri, String contentType, final HttpJson.ProgressListener listener) throws IOException {
        Log.v(TAG, "addFilePart(" + fieldName + "," + filename + "," + contentType + ")");

        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"").append(filename).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(contentType).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        final long startTime = System.currentTimeMillis();
        final InputStream inputStream = ContextProvider.get().getContentResolver().openInputStream(uri);
        final int inputSize = inputStream.available();

        Stopwatch stopwatch = new Stopwatch(true);
        Log.v(TAG, "Start upload....");
        if (listener != null) {
            StreamUtils.copyStream(inputStream, outputStream, -1, 1000, new StreamUtils.ProgressListener() {
                @Override
                public void progress(int position) {
                    if (listener != null)
                        listener.progress(position, inputSize, System.currentTimeMillis() - startTime);
                }
            });
        } else {
            StreamUtils.copyStream(inputStream, outputStream, -1, 1000, null);
        }

        Log.v(TAG, "Finish upload...." + stopwatch.finish());
        outputStream.flush();
        writer.append(LINE_FEED);
        writer.flush();

    }

    public void addFilePart(String fieldName, String filename, byte[] filedata, String contentType) throws IOException {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"").append(filename).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(contentType).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append("Content-Length: " + filedata.length).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        outputStream.write(filedata);
        outputStream.flush();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned status
     * OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public HttpURLConnection finish() throws IOException {
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        return httpConn;
    }
}
