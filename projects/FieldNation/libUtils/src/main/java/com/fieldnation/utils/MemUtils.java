package com.fieldnation.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.util.Log;

import java.io.File;

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

    public static Bitmap getMemoryEfficientBitmap(File source, int destWidth) {
        BitmapFactory.Options srcOptions = new BitmapFactory.Options();
        srcOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(source.toString(), srcOptions);

        BitmapFactory.Options dstOption = new BitmapFactory.Options();
        dstOption.inSampleSize = calculateInSampleSize(srcOptions.outWidth, destWidth);

        return BitmapFactory.decodeFile(source.toString(), dstOption);
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

        return (int) memoryInfo.getTotalPss() / 1024;
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

            if (!isLowMemorySituation(context)
                    && ((getAppPrivateMemory() + getAppNativeHeapFreeSize()) * MINIMUM_FREE_MEMORY_THRESHOLD_PERCENTAGE < getAppNativeHeapFreeSize())) {
                shouldSuspendLoadingMore_cache = true;
            } else {
                shouldSuspendLoadingMore_cache = false;
            }

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
