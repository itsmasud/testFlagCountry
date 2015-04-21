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

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoTransactionHandler extends WebTransactionHandler implements PhotoConstants {
    private static final String TAG = "PhotoTransactionHandler";

    public static byte[] generateParams(String url, boolean getCircle) {
        try {
            JsonObject json = new JsonObject();
            json.put(PARAM_URL, url);
            json.put(PARAM_CIRCLE, getCircle);
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
            boolean getCircle = json.getBoolean(PARAM_CIRCLE);
            String url = json.getString(PARAM_URL);
            String imageObjectName = "PhotoCache";
            String circleObjectName = "PhotoCacheCircle";

            Log.v(TAG, "handleResult " + url + "," + getCircle);

            // generate the bitmaps
            byte[] imageData = resultData.getResultsAsByteArray();
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            Bitmap circleBitmap = misc.extractCircle(imageBitmap);

            // find the paths
            String storagePath = GlobalState.getContext().getStoragePath() + "/temp";
            File tempFolder = new File(storagePath);
            tempFolder.mkdirs();

            // generate the pngs
            File imageFile = File.createTempFile("photo", "img", tempFolder);
            FileOutputStream fout = new FileOutputStream(imageFile, false);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.close();

            File circleFile = File.createTempFile("photo", "img", tempFolder);
            fout = new FileOutputStream(circleFile, false);
            circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.close();

            // push into data store
            StoredObject imageObj = StoredObject.put(context, imageObjectName, url, imageFile, false);
            StoredObject circleObj = StoredObject.put(context, circleObjectName, url, circleFile, false);

            // delete temporary stuff
            imageFile.delete();
            circleFile.delete();
            imageBitmap.recycle();
            circleBitmap.recycle();

            // build the response

            // done!
            if (getCircle) {
                PhotoDataDispatch.photo(context, circleObj.getFile(), url, getCircle);
            } else {
                PhotoDataDispatch.photo(context, imageObj.getFile(), url, getCircle);
            }
            Log.v(TAG, "handleResult");
            return Result.FINISH;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v(TAG, "handleResult");
            return Result.REQUEUE;
        }

    }
}
