package com.fieldnation.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Michael on 3/10/2016.
 */
public class DebugUtils {
    public static void printStackTrace(String message) {
        try {
            throw new Exception(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File dumpLogcat(Context context, String version) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
        temppath.mkdirs();
        File tempfile = new File(temppath + "/logcat-" + (System.currentTimeMillis() / 1000) + ".log");
        try {
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(tempfile));

            fout.write("APP VERSION: " + version + "\n");
            fout.write("MANUFACTURER: " + Build.MANUFACTURER + "\n");
            fout.write("MODEL: " + Build.MODEL + "\n");
            fout.write("RELEASE: " + Build.VERSION.RELEASE + "\n");
            fout.write("SDK: " + Build.VERSION.SDK_INT + "\n");

            try {
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    fout.write(line);
                    fout.write("\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tempfile;
    }

    public static void flushLogs(Context context, long deathAge) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");

        String[] files = temppath.list();

        if (files == null)
            return;

        for (int i = 0; i < files.length; i++) {
            File file = new File(temppath.getAbsolutePath() + "/" + files[i]);

            Log.v("misc", "checking " + file.getAbsolutePath() + ":" + file.lastModified());

            if (file.lastModified() + deathAge < System.currentTimeMillis()) {
                Log.v("misc", "deleting " + file.getAbsolutePath());
                file.delete();
            }
        }

    }

/*
    public static String getLogcat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            return log.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
*/

    public static String getStackTrace(Exception e) {
        StringBuilder trace = new StringBuilder();

        trace.append(e.getMessage() + "\n");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement elem = e.getStackTrace()[i];
            trace.append(elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + String.valueOf(elem.getLineNumber()) + ")\n");
        }

        return trace.toString();
    }

}
