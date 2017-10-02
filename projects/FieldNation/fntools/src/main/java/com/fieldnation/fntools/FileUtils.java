package com.fieldnation.fntools;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.fieldnation.fnlog.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * Created by Michael on 3/10/2016.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    public static String guessContentTypeFromName(String url) {
        try {
            return URLConnection.guessContentTypeFromName(url);
        } catch (Exception ex) {
        }
        return "application/octet-stream";
    }

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
                inFile = in;
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

    public static boolean isValidFileName(String fileName) {
        // TODO need more character to add
        final String[] srch = {"/", "\\", "?", "%", "*", ":", "|", "\"", "\'", "<", ">"};

        for (final String sampleChar : srch) {
            if (fileName.contains(sampleChar)) {
                return false;
            }
        }
        return true;
    }

    public static String getFilePathFromUri(Context context, Uri uri) {
        final String filePath;

        if (uri.getScheme().equals("file")) {
//            Log.e("FileUtils", "Uri from dropbox, onedrive. ");
            return uri.getPath();

        } else if (uri.getScheme().equals("content")) {
//            if (uri.getAuthority().equals("media")) {
            Log.e("FileUtils", "For gallery app, google photo");
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            int filePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(filePathIndex);
            cursor.close();
            return filePath;

//            } else if (uri.getAuthority() != null &&
//                    uri.getAuthority().equals("com.google.android.apps.photos.contentprovider")) {
//                Log.e("FileUtils", "form google photos");
//                return null;
//
//            }
        }
        return null;
    }

    public static String getMimeTypeFromIntent(Context context, Intent intent) {

        if (misc.isEmptyOrNull(intent.getType())) {
            ContentResolver cr = context.getContentResolver();
            return cr.getType(intent.getData());
        }
        return intent.getType();
    }

    public static String getMimeTypeFromFile(File file) {
        if (file != null) {
            String fileName = file.getName();
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(fileName.lastIndexOf('.') + 1));
        }
        return null;
    }


    public static String getFileNameFromUri(Context context, final Uri uri) {
        if (uri != null) {
            Log.v(TAG, "Crashlytics 1476 catch: " + uri.toString());
        }
        String fileName = "";

        try {
            if (uri.getScheme().compareTo("content") == 0) {
                //                Log.v(TAG, "For gallery app, google photos");
                final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor == null) {
                } else if (cursor.moveToFirst()) {
                    String[] columnNames = new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};

                    for (String columnName : columnNames) {
                        int nameIndex = -1;
                        try {
                            nameIndex = cursor.getColumnIndexOrThrow(columnName);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }

                        if (nameIndex != -1) {
                            String value = cursor.getString(nameIndex);
                            if (value != null) {
                                fileName = Uri.parse(value).getLastPathSegment();
                                break;
                            }
                        }
                    }
                }
            } else if (uri.getScheme().compareTo("file") == 0) {
                fileName = new File(uri.getPath()).getName();
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return fileName;
    }


}
