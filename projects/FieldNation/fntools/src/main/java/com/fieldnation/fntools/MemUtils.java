package com.fieldnation.fntools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;

import com.fieldnation.fnlog.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MemUtils {
    private static final String TAG = "MemUtils";

    private static final double BYTES_IN_MB = 1024.0 * 1024.0;
    private static final double BYTES_IN_KB = 1024.0;
    private static final double BYTES_PER_PX = 4.0;
    private static final double MINIMUM_FREE_MEMORY_THRESHOLD_PERCENTAGE = 0.15;

    public static Bitmap getMemoryEfficientBitmap(Context context, int sourceImage, int reqHeight) {
        Resources res = context.getResources();

        if (getAppFreeHeapMemory(context) < getAllocatedBitmapMemory(sourceImage, res)) {
            return subSampleImage(reqHeight, res, sourceImage);
        }

        return subSampleImage(0, res, sourceImage);
    }

    public static Bitmap getMemoryEfficientBitmap(Context context, Uri uri, int destWidth) throws FileNotFoundException {
        BitmapFactory.Options srcOptions = new BitmapFactory.Options();
        srcOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, srcOptions);

        BitmapFactory.Options dstOption = new BitmapFactory.Options();
        dstOption.inJustDecodeBounds = false;
        dstOption.inSampleSize = calculateInSampleSize(srcOptions.outWidth, destWidth);

        Log.v("MemUtils", "inSampleSize: " + dstOption.inSampleSize);

        return rotateImageIfRequired(context,
                BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, dstOption),
                uri);
    }

    public static Bitmap getMemoryEfficientBitmap(String pathName, int destWidth) {
        BitmapFactory.Options srcOptions = new BitmapFactory.Options();
        srcOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, srcOptions);

        BitmapFactory.Options dstOption = new BitmapFactory.Options();
        dstOption.inJustDecodeBounds = false;
        dstOption.inSampleSize = calculateInSampleSize(srcOptions.outWidth, destWidth);

        Log.v("MemUtils", "inSampleSize: " + dstOption.inSampleSize);

        return rotateImageIfRequired(BitmapFactory.decodeFile(pathName, dstOption), pathName);
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) {
        try {
            ExifInterface ei = new ExifInterface(selectedImage);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (Exception ex) {
            return img;
        }
    }

    private static String getStoragePath(Context context) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    private static String getTempFolder(Context context) {
        File tempFolder = new File(getStoragePath(context) + "/temp");
        tempFolder.mkdirs();
        return tempFolder.getAbsolutePath();
    }

    /**
     * Rotate an image if required.
     *
     * @param context Application context
     * @param img     The image bitmap
     * @param uri     Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri uri) {
        InputStream in = null;
        File tempFile = null;
        try {
            ExifInterface ei = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ei = new ExifInterface(context.getContentResolver().openInputStream(uri));
            } else {
                in = context.getContentResolver().openInputStream(uri);
                tempFile = File.createTempFile("img", "dat", new File(getTempFolder(context)));
                FileUtils.writeStream(in, tempFile);
                in.close();
                in = null;
                ei = new ExifInterface(tempFile.getAbsolutePath());
            }

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (Exception ex) {
            return img;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ex) {
            }
            try {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            } catch (Exception ex) {
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private static Bitmap subSampleImage(int reqHeight, Resources res, int sourceImage) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options.outHeight, reqHeight);
        return BitmapFactory.decodeResource(res, sourceImage, options);
    }

    private static double getAllocatedBitmapMemory(int sourceImage, Resources res) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, sourceImage, options);
        final double imageHeight = options.outHeight;
        final double imageWidth = options.outWidth;

//        return imageWidth * imageHeight * getBytesPerPixel(BitmapFactory.decodeResource(res, sourceImage, options).getConfig()) / MemUtils.BYTES_IN_MB;
        return imageWidth * imageHeight * BYTES_PER_PX / MemUtils.BYTES_IN_MB;
    }


    public static double getDeviceFreeMemory() {
        final Runtime rt = Runtime.getRuntime();
        final double bytesUsed = rt.totalMemory();
        final double mbUsed = bytesUsed / BYTES_IN_MB;
        final double mbFree = getDeviceAvailableMemory() - mbUsed;
        return mbFree;
    }

    public static double getDeviceAvailableMemory() {
        final Runtime rt = Runtime.getRuntime();
        final double bytesAvailable = rt.maxMemory();
        Log.v(TAG, "getDeviceFreeMemory:" + bytesAvailable / BYTES_IN_MB);

        return bytesAvailable / BYTES_IN_MB;
    }


    private static int getAppFreeHeapMemory(Context context) {
        Log.v(TAG, "getAppFreeHeapMemory:" + (getAppHeapMemory(context) - getAppsUsedHeapMemory()));
        return getAppHeapMemory(context) - getAppsUsedHeapMemory();
    }

    public static int getAppHeapMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.v(TAG, "getAppHeapMemory:" + memoryClass);
        return memoryClass;
    }


    // returns used MB on heap (fetches almost same value like Device Monitor)
    public static double getAppPrivateMemory() {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Debug.MemoryInfo mi = new Debug.MemoryInfo();
            Debug.getMemoryInfo(mi);
//        Log.e(TAG, "Private : " + mi.getTotalPrivateDirty() / BYTES_IN_KB);
            return mi.getTotalPrivateDirty() / BYTES_IN_KB;
        } finally {
            Log.v(TAG, "getAppPrivateMemory time: " + stopwatch.finish());
        }
    }

    // returns free MB on heap (fetches almost same value like Device Monitor)
    public static double getAppNativeHeapFreeSize() {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Debug.MemoryInfo mi = new Debug.MemoryInfo();
            Debug.getMemoryInfo(mi);
//        Log.e(TAG, "NativeHeapFreeSize() :" + Debug.getNativeHeapFreeSize() / BYTES_IN_MB);
            return Debug.getNativeHeapFreeSize() / BYTES_IN_MB;
        } finally {
            Log.v(TAG, "getAppNativeHeapFreeSize time: " + stopwatch.finish());
        }
    }

    private static int getAppsUsedHeapMemory() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

        String memMessage = String.format("App Memory: Pss=%.2f MB\nPrivate=%.2f MB\nShared=%.2f MB",
                memoryInfo.getTotalPss() / 1024.0,
                memoryInfo.getTotalPrivateDirty() / 1024.0,
                memoryInfo.getTotalSharedDirty() / 1024.0);
        Log.i(TAG, memMessage);
        Log.v(TAG, "getAppsUsedHeapMemory:" + memoryInfo.getTotalPss() / 1024);

        return memoryInfo.getTotalPss() / 1024;
    }

    // Memory info doesnt match with Device Monitor
//    public static boolean shouldSuspendLoadingMore(Context context) {
//        if (getAppHeapMemory(context) * MINIMUM_FREE_MEMORY_THRESHOLD_PERCENTAGE < getAppFreeHeapMemory(context)) {
//            Log.v(TAG,"AppHeapMemory: " + getAppHeapMemory(context));
//            Log.v(TAG,"Threshold of Minimum Free HeapMemory: " + getAppHeapMemory(context)* MINIMUM_FREE_MEMORY_THRESHOLD_PERCENTAGE);
//            Log.v(TAG,"AppFreeHeapMemory: " + getAppFreeHeapMemory(context));
//            return true;
//        }
//        return false;
//    }

    private static long shouldSuspendLoadingMore_timer = 0;
    private static boolean shouldSuspendLoadingMore_cache = false;

    // Memory info matches with Device Monitor
    public static boolean shouldSuspendLoadingMore(Context context) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {

            if (System.currentTimeMillis() < shouldSuspendLoadingMore_timer)
                return shouldSuspendLoadingMore_cache;

//        Log.e(TAG,"getAppPrivateMemory: " + getAppPrivateMemory());
//        Log.e(TAG,"getAppNativeHeapFreeSize: " + getAppNativeHeapFreeSize());
//        Log.e(TAG,"isLowMemorySituation((context): " + isLowMemorySituation((context)));

            shouldSuspendLoadingMore_cache = !isLowMemorySituation(context)
                    && ((getAppPrivateMemory() + getAppNativeHeapFreeSize()) * MINIMUM_FREE_MEMORY_THRESHOLD_PERCENTAGE < getAppNativeHeapFreeSize());

            shouldSuspendLoadingMore_timer = System.currentTimeMillis() + 10000;

            return shouldSuspendLoadingMore_cache;
        } finally {
            Log.v(TAG, "shouldSuspendLoadingMore time: " + stopwatch.finish());
        }
    }


    public static boolean isLowMemorySituation(Context context) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memInfo);
            return memInfo.lowMemory;
        } finally {
            Log.v(TAG, "isLowMemorySituation time: " + stopwatch.finish());
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static int calculateInSampleSize(int srcDim, int destDim) {
        if (destDim == 0)
            return 0;

        int inSampleSize = 1;

        if (srcDim > destDim) {

            final int halfDim = srcDim / 2;

            while ((halfDim / inSampleSize) > destDim) {
                inSampleSize *= 2;
            }
        }

        Log.e("inSampleSize:", inSampleSize + "");

        return inSampleSize;
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
     */
    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 4;
    }
}
