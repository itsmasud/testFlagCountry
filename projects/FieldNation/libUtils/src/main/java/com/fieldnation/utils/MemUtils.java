package com.fieldnation.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.util.Log;

public class MemUtils {
    private static final String TAG = "MemUtils";

    private static final float BYTES_IN_MB = 1024.0f * 1024.0f;
    private static final float BYTES_PER_PX = 4.0f;

    public static Bitmap getMemoryEfficientBitmap(Context context, int sourceImage, int reqHeight) {
//        Log.v(TAG, "getDeviceFreeMemory:" + getDeviceFreeMemory());
//        Log.v(TAG, "getDeviceAvailableMemory:" + getDeviceAvailableMemory());
        Resources res = context.getResources();

        if (getAppFreeHeapMemory(context) < getAllocatedBitmapMemory(sourceImage, res)) {
            return subSampleImage(reqHeight, res, sourceImage);
        }

        return subSampleImage(0, res, sourceImage);
    }


    private static Bitmap subSampleImage(int reqHeight, Resources res, int sourceImage) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqHeight);
        return BitmapFactory.decodeResource(res, sourceImage, options);
    }

    private static float getAllocatedBitmapMemory(int sourceImage, Resources res) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, sourceImage, options);
        final float imageHeight = options.outHeight;
        final float imageWidth = options.outWidth;

//        return imageWidth * imageHeight * getBytesPerPixel(BitmapFactory.decodeResource(res, sourceImage, options).getConfig()) / MemUtils.BYTES_IN_MB;
        return imageWidth * imageHeight * BYTES_PER_PX / MemUtils.BYTES_IN_MB;
    }


    public static float getDeviceFreeMemory() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesUsed = rt.totalMemory();
        final float mbUsed = bytesUsed / BYTES_IN_MB;
        final float mbFree = getDeviceAvailableMemory() - mbUsed;
        return mbFree;
    }

    public static float getDeviceAvailableMemory() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesAvailable = rt.maxMemory();
        return bytesAvailable / BYTES_IN_MB;
    }


    private static int getAppFreeHeapMemory(Context context) {
//        Log.v(TAG, "getAppFreeHeapMemory:" + (getAppHeapMemory(context) - getAppsUsedHeapMemory()));
        return getAppHeapMemory(context) - getAppsUsedHeapMemory();
    }

    public static int getAppHeapMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
//        Log.v(TAG, "getAppHeapMemory:" + memoryClass);
        return memoryClass;
    }


    private static int getAppsUsedHeapMemory() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

//        String memMessage = String.format("App Memory: Pss=%.2f MB\nPrivate=%.2f MB\nShared=%.2f MB",
//                memoryInfo.getTotalPss() / 1024.0,
//                memoryInfo.getTotalPrivateDirty() / 1024.0,
//                memoryInfo.getTotalSharedDirty() / 1024.0);
//        Log.i(TAG, memMessage);
//        Log.v(TAG, "getAppsUsedHeapMemory:" + memoryInfo.getTotalPss() / 1024);

        return (int) memoryInfo.getTotalPss() / 1024;
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


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight) {
        if (reqHeight == 0)
            return 0;

        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight) {

            final int halfHeight = height / 2;

            while ((halfHeight / inSampleSize) > reqHeight) {
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
