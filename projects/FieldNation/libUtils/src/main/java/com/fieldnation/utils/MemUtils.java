package com.fieldnation.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MemUtils {

    public static final float BYTES_IN_MB = 1024.0f * 1024.0f;
    private static final float BYTES_PER_PX = 4.0f;

    public static Bitmap getMemoryEfficientBitmap(Resources res, int sourceImage) {
        if (getFreeMemoryInMB() < getAllocatedBitmapMemory(sourceImage, res)) {
            return subSampleImage(2, res, sourceImage);
        }
        return subSampleImage(0, res, sourceImage);
    }


    private static Bitmap subSampleImage(int powerOf2, Resources res, int sourceImage) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = powerOf2;
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


    public static float getFreeMemoryInMB() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesUsed = rt.totalMemory();
        final float mbUsed = bytesUsed / BYTES_IN_MB;
        final float mbFree = getAvailableMemoryInMB() - mbUsed;
        return mbFree;
    }

    public static float getAvailableMemoryInMB() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesAvailable = rt.maxMemory();
        return bytesAvailable / BYTES_IN_MB;
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
