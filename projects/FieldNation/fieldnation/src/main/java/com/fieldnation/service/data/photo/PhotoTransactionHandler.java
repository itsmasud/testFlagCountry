package com.fieldnation.service.data.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.utils.misc;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoTransactionHandler extends WebTransactionHandler implements PhotoConstants {
    private static final String TAG = "PhotoTransactionHandler";

    public static byte[] pGet(String url, boolean getCircle) {
        try {
            JsonObject json = new JsonObject("action", "pGet");
            json.put("url", url);
            json.put("circle", getCircle);
            return json.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        Log.v(TAG, "handleResult");
        try {
            JsonObject json = new JsonObject(transaction.getHandlerParams());
            boolean getCircle = json.getBoolean("circle");
            String url = json.getString("url");
            String imageObjectName = "PhotoCache";
            String circleObjectName = "PhotoCacheCircle";

            Log.v(TAG, "handleResult " + url + "," + getCircle);

            // generate the bitmaps
            byte[] imageData = resultData.getByteArray();
            Bitmap imageBitmap = misc.resizeBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length), 95, 95);
            Bitmap circleBitmap = misc.extractCircle(imageBitmap);

            // find the paths
            String storagePath = GlobalState.getContext().getStoragePath() + "/temp";
            File tempFolder = new File(storagePath);
            tempFolder.mkdirs();

            ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
            StoredObject imageObj = StoredObject.put(context, imageObjectName, url, imageOut.toByteArray(), "PhotoCache.png", false);
            imageBitmap.recycle();


            ByteArrayOutputStream circleOut = new ByteArrayOutputStream();
            circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, circleOut);
            StoredObject circleObj = StoredObject.put(context, circleObjectName, url, circleOut.toByteArray(), "PhotoCacheCircle.png", false);
            circleBitmap.recycle();

            // build the response

            // done!
            if (getCircle) {
                PhotoDispatch.get(context, circleObj.getFile(), url, true, false, transaction.isSync());
            } else {
                PhotoDispatch.get(context, imageObj.getFile(), url, false, false, transaction.isSync());
            }
            Log.v(TAG, "handleResult");
            return Result.FINISH;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v(TAG, "handleResult");
            return Result.ERROR;
        }

    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject json = new JsonObject(transaction.getHandlerParams());
            boolean getCircle = json.getBoolean("circle");
            String url = json.getString("url");

            PhotoDispatch.get(context, null, url, getCircle, true, transaction.isSync());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }
}
