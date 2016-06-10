package com.fieldnation.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private static boolean isFileExtensionChanged(final String newFileName, final String originalExt) {
        return !newFileName.substring(newFileName.lastIndexOf('.') + 1).equals(originalExt);
    }

    public static String getFileNameWithOriginalExtenstion(final String newFileName, String originalFileName) {

        if (isFileExtensionChanged(newFileName, originalFileName.substring(originalFileName.lastIndexOf('.') + 1))) {
            return newFileName.substring(0, newFileName.lastIndexOf('.') + 1)
                    + originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }
        return newFileName;
    }

    public static String getFileAbsolutePathWithOriginalExtension(String directoryPath, String newFileName, String originalFileName) {
        return directoryPath + File.separator
                + FileUtils.getFileNameWithOriginalExtenstion(newFileName, originalFileName);
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

    private static String getFileNameFromContentUri(Context context, Uri contentUri) {

        final String fileName;

        if (contentUri.getScheme().equals("content")) {
//            Log.v(TAG, "Works for gallery app, google photos");
            Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            fileName = cursor.getString(nameIndex);
            cursor.close();
            return fileName;
        }
        return null;
    }


    public static String getFileNameFromUri(Context context, Uri uri) {

        if (uri.getScheme().equals("file")) {
//            Log.v(TAG, "Uri from dropbox, onedrive. ");
            return uri.getLastPathSegment();

        } else if (uri.getScheme().equals("content")) {
//                Log.v(TAG, "For gallery app, google photos");
            return getFileNameFromContentUri(context, uri);
        }
        return null;
    }


    public static String getFilePathFromUri(Context context, Uri uri) {
        final String filePath;
        if (uri.getScheme().equals("file")) {
//            Log.v(TAG, "Uri from dropbox, onedrive. ");
            return uri.getPath();

        } else if (uri.getScheme().equals("content")) {
            if (uri.getAuthority().equals("media")) {
//                Log.v(TAG, "For gallery app");
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                int filePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(filePathIndex);
                cursor.close();
                return filePath;

            } else if (uri.getAuthority() != null &&
                    uri.getAuthority().equals("com.google.android.apps.photos.contentprovider")) {
//                Log.v(TAG, "form google photos");
                return null;

            }
        }
        return null;
    }

    public static String getMimeTypeFromIntent(Context context, Intent intent){

        if(misc.isEmptyOrNull(intent.getType())){
            ContentResolver cr = context.getContentResolver();
            return cr.getType(intent.getData());
        }
        return intent.getType();



    }


}
