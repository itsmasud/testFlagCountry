package com.fieldnation.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Michael on 3/10/2016.
 */
public class FileUtils {

    public static void copyDirectoryTree(File sourceDir, File destDir) throws IOException {

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        File[] children = sourceDir.listFiles();

        for (File sourceChild : children) {

            String name = sourceChild.getName();

            File destChild = new File(destDir, name);

            if (sourceChild.isDirectory()) {
                copyDirectoryTree(sourceChild, destChild);
            } else {
                copyFile(sourceChild, destChild);
            }

        }
    }

    public static void deleteDirectoryTree(File file) throws IOException {
        if (file.isDirectory()) {
            // directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
            } else {
                // list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(file, temp);

                    // recursive delete
                    deleteDirectoryTree(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            // if file, then delete it
            file.delete();
        }
    }

    public static boolean writeStream(InputStream in, File dest) throws IOException {
        OutputStream outFile = null;
        InputStream inFile = null;

        int read = 0;
        byte[] packet = null;
        try {
            packet = DataUtils.allocPacket();

            try {
                inFile = new BufferedInputStream(in);
                outFile = new BufferedOutputStream(new FileOutputStream(dest));
                while (true) {
                    read = inFile.read(packet);
                    if (read > 0) {
                        outFile.write(packet, 0, read);
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    } else if (read == -1) {
                        break;
                    }
                }
            } finally {
                try {
                    inFile.close();
                } catch (IOException e) {
                }
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }
            return true;
        } finally {
            DataUtils.freePacket(packet);
        }
    }

    public static boolean copyFile(File src, File dest) throws IOException {
        OutputStream outFile = null;
        InputStream inFile = null;

        long size = src.length();
        long pos = 0;
        int read = 0;
        byte[] packet = null;
        try {
            packet = DataUtils.allocPacket();

            try {
                inFile = new BufferedInputStream(new FileInputStream(src));
                outFile = new BufferedOutputStream(new FileOutputStream(dest));
                while (pos < size) {
                    read = inFile.read(packet);
                    if (read > 0) {
                        outFile.write(packet, 0, read);
                        pos += read;
                    } else if (read == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    } else if (read == -1) {
                        break;
                    }
                }
            } finally {
                try {
                    inFile.close();
                } catch (IOException e) {
                }
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }
            return pos == size;
        } finally {
            DataUtils.freePacket(packet);
        }
    }
}
