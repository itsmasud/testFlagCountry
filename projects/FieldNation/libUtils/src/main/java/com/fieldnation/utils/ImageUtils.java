package com.fieldnation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.DisplayMetrics;

/**
 * Created by Michael on 3/10/2016.
 */
public class ImageUtils {

    public static Bitmap resizeBitmap(Bitmap source, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, source.getWidth(), source.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     * removes a circle from the source bitmap that is exactly centered
     *
     * @param source
     * @return
     */
    public static Bitmap extractCircle(Bitmap source) {
        int[] pixels = new int[source.getWidth() * source.getHeight()];

        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());

        int size = Math.min(source.getWidth(), source.getHeight());
        int cx = source.getWidth() / 2;
        int cy = source.getHeight() / 2;
        int xoff = (source.getWidth() - size) / 2;
        int yoff = (source.getHeight() - size) / 2;

        int[] destpix = new int[size * size];

        int dist2 = Math.min(cx, cy);
        dist2 = dist2 * dist2;

        int l1 = (dist2 * 94) / 100;
        int l2 = (dist2 * 96) / 100;
        int l3 = (dist2 * 98) / 100;

        int dx = 0;
        int dy = 0;


        for (int x = xoff; x < source.getWidth() - xoff; x++) {
            for (int y = yoff; y < source.getHeight() - yoff; y++) {
                dx = cx - x;
                dy = cy - y;
                int dist = dx * dx + dy * dy;

                if (dist <= l1) {
                    destpix[(x - xoff) + (y - yoff) * size] = pixels[x + y * source.getWidth()];
                } else if (dist <= l2) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 192) / 256) << 56 & 0xFF000000);
                } else if (dist <= l3) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 128) / 256) << 56 & 0xFF000000);
                } else if (dist <= dist2) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 64) / 256) << 56 & 0xFF000000);
                }
            }
        }

        return Bitmap.createBitmap(destpix, size, size, Bitmap.Config.ARGB_8888);
    }

    public static float getDeviceDensity(Context context) {
        Resources res = context.getResources();
        if (res != null) {
            final DisplayMetrics metrics = res.getDisplayMetrics();
            if (metrics != null) {
                return metrics.density;
            } else {
                return 1;
            }
        }
        return 1;
    }

    public static Bitmap scaleDownBitmapBasedOnRequestedHeight(Bitmap bitmap, int requestedHeight) {

        float aspectRatio = 0f;

        if (bitmap.getWidth() <= bitmap.getHeight()) {
            aspectRatio = bitmap.getWidth() /
                    (float) bitmap.getHeight();
        } else {
            aspectRatio = bitmap.getHeight() /
                    (float) bitmap.getWidth();
        }

        int requestedWidth = Math.round(requestedHeight / aspectRatio);

        return Bitmap.createScaledBitmap(
                bitmap, requestedWidth, requestedHeight, false);

    }
}
