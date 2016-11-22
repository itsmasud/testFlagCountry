package com.fieldnation.fntools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Michael on 3/10/2016.
 */
public class StreamUtils {

    public interface ProgressListener {
        void progress(int position);
    }

    public static void copyStream(InputStream source, OutputStream dest, int expectedSize,
                                  long timeoutInMilli) throws IOException {
        copyStream(source, dest, expectedSize, timeoutInMilli, null);
    }

    public static void copyStream(InputStream source, OutputStream dest, int expectedSize,
                                  long timeoutInMilli, ProgressListener progress) throws IOException {
        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        byte[] packet = null;
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;
        long lastUpdateTime = 0;

        try {
            packet = DataUtils.allocPacket();
            try {
                // if (!waitForData(source, timeoutInMilli)) {
                // return;
                // }

                while (!error && !timedOut && !complete) {
                    read = source.read(packet, 0, 1024);
                    if (read > 0) {
                        pos += read;

                        if (progress != null) {
                            if (System.currentTimeMillis() > lastUpdateTime) {
                                progress.progress(pos);
                                lastUpdateTime = System.currentTimeMillis() + 500; // TODO make configurable
                            }
                        }

                        dest.write(packet, 0, read);
                        timeout = System.currentTimeMillis() + timeoutInMilli;
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    } else if (read == -1) {
                        // error, stop
                        error = true;
                    }

                    // finished, stop
                    if (pos == size && size != -1) {
                        complete = true;
                    }

                    // read too much!
                    if (pos > size && size != -1) {
                        error = true;
                    }

                    // timeout, stop
                    if (timeout < System.currentTimeMillis()) {
                        timedOut = true;
                    }

                }
            } catch (IOException e) {
                return;
            }
            if (timedOut && size != -1) {
                return;
            } else if (complete) {
                return;
            } else if (size == -1) {
                return;
            }

            throw new IOException("Error, could not read entire stream!");
        } finally {
            if (progress != null) {
                progress.progress(pos);
            }
            DataUtils.freePacket(packet);
        }
    }

    public static byte[] readAllFromStreamUntil(InputStream in, int expectedSize, int maxSize, long timeoutInMilli) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        byte[] packet = null;
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;
        try {
            packet = DataUtils.allocPacket();
            try {
                while (!error && !timedOut && !complete && bout.size() < maxSize) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                    read = in.read(packet, 0, 1024);

                    if (read > 0) {
                        pos += read;

                        bout.write(packet, 0, read);
                        timeout = System.currentTimeMillis() + timeoutInMilli;
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    } else if (read == -1) {
                        // error, stop
                        error = true;
                    }

                    // finished, stop
                    if (pos == size && size != -1) {
                        complete = true;
                    }

                    // read too much!
                    if (pos > size && size != -1) {
                        error = true;
                    }

                    // timeout, stop
                    if (timeout < System.currentTimeMillis()) {
                        timedOut = true;
                    }

                }
            } catch (IOException e) {
                return bout.toByteArray();
            }

            if (timedOut && size != -1) {
                return bout.toByteArray();
            } else if (complete) {
                return bout.toByteArray();
            } else if (size == -1) {
                return bout.toByteArray();
            }

            return null;
        } finally {
            DataUtils.freePacket(packet);
        }
    }

    public static byte[] readAllFromStream(InputStream in, int expectedSize, long timeoutInMilli) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        byte[] packet = null;
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            packet = DataUtils.allocPacket();
            try {
                while (!error && !timedOut && !complete) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                    read = in.read(packet, 0, 1024);

                    if (read > 0) {
                        pos += read;

                        bout.write(packet, 0, read);
                        timeout = System.currentTimeMillis() + timeoutInMilli;
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    } else if (read == -1) {
                        // error, stop
                        error = true;
                    }

                    // finished, stop
                    if (pos == size && size != -1) {
                        complete = true;
                    }

                    // read too much!
                    if (pos > size && size != -1) {
                        error = true;
                    }

                    // timeout, stop
                    if (timeout < System.currentTimeMillis()) {
                        timedOut = true;
                    }

                }
            } catch (IOException e) {
                return bout.toByteArray();
            }

            if (timedOut && size != -1) {
                return bout.toByteArray();
            } else if (complete) {
                return bout.toByteArray();
            } else if (size == -1) {
                return bout.toByteArray();
            }

            return null;
        } finally {
            DataUtils.freePacket(packet);
        }
    }

    public interface PacketListener {
        void onPacket(byte[] packet, int length);
    }

    public static void readAllFromStream(InputStream in, int expectedSize, long timeoutInMilli,
                                         PacketListener listener) throws IOException {
        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        byte[] packet = null;
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            packet = DataUtils.allocPacket();
            try {
                while (!error && !timedOut && !complete) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                    read = in.read(packet, 0, 1024);

                    if (read > 0) {
                        pos += read;

                        listener.onPacket(packet, read);
                        timeout = System.currentTimeMillis() + timeoutInMilli;
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    } else if (read == -1) {
                        // error, stop
                        error = true;
                    }

                    // finished, stop
                    if (pos == size && size != -1) {
                        complete = true;
                    }

                    // read too much!
                    if (pos > size && size != -1) {
                        error = true;
                    }

                    // timeout, stop
                    if (timeout < System.currentTimeMillis()) {
                        timedOut = true;
                    }

                }
            } catch (IOException e) {
            }
        } finally {
            DataUtils.freePacket(packet);
        }
    }


    public static boolean waitForData(InputStream inputStream, long timeout) {
        long finish = System.currentTimeMillis() + timeout;

        try {
            while (finish > System.currentTimeMillis()) {
                if (inputStream.available() > 0) {
                    return true;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
        }

        return false;
    }

}
