package com.fieldnation.service.data.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.ImageUtils;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.io.ByteArrayOutputStream;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoTransactionListener extends WebTransactionListener implements PhotoConstants {
    private static final String TAG = "PhotoTransactionListener";

    public static byte[] pGet(String url, boolean getCircle) {
        try {
            JsonObject json = new JsonObject("action", "pGet");
            json.put("url", url);
            json.put("circle", getCircle);
            return json.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return null;
    }

    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        Log.v(TAG, "onComplete");
        if (result == Result.CONTINUE) {
            try {
                JsonObject json = new JsonObject(transaction.getListenerParams());
                boolean getCircle = json.getBoolean("circle");
                String url = json.getString("url");
                String imageObjectName = "PhotoCache";
                String circleObjectName = "PhotoCacheCircle";

                Log.v(TAG, "onComplete " + url + "," + getCircle);

                // generate the bitmaps
                byte[] imageData = httpResult.getByteArray();
                Bitmap sourceBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                Bitmap imageBitmap = ImageUtils.resizeBitmap(sourceBitmap, 95, 95);
                sourceBitmap.recycle();
                Bitmap circleBitmap = ImageUtils.extractCircle(imageBitmap);

                // Calling temp folder. Will be created if doesn't exist
                App.get().getTempFolder();

                ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
                StoredObject imageObj = StoredObject.put(context, App.getProfileId(), imageObjectName, url, imageOut.toByteArray(), "PhotoCache.png", false);
                imageBitmap.recycle();


                ByteArrayOutputStream circleOut = new ByteArrayOutputStream();
                circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, circleOut);
                StoredObject circleObj = StoredObject.put(context, App.getProfileId(), circleObjectName, url, circleOut.toByteArray(), "PhotoCacheCircle.png", false);
                circleBitmap.recycle();

                // build the response

                // done!
                if (getCircle) {
                    PhotoDispatch.get(context, circleObj.getFile(), url, true, false, transaction.isSync());
                } else {
                    PhotoDispatch.get(context, imageObj.getFile(), url, false, false, transaction.isSync());
                }
                Log.v(TAG, "onComplete");
                return result;
            } catch (Exception ex) {
                Log.v(TAG, ex);
                Log.v(TAG, "onComplete");
                return Result.DELETE;
            }

        } else if (result == Result.DELETE) {
            try {
                JsonObject json = new JsonObject(transaction.getListenerParams());
                boolean getCircle = json.getBoolean("circle");
                String url = json.getString("url");

                PhotoDispatch.get(context, null, url, getCircle, true, transaction.isSync());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}
