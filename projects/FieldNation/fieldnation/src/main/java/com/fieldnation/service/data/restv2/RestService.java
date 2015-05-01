package com.fieldnation.service.data.restv2;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.List;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestService extends MSService implements RestConstants {


    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents) {
        return new RestServiceThread(manager, this, intents);
    }

    private class RestServiceThread extends WorkerThread {
        private String TAG = UniqueTag.makeTag("RestServiceThread");
        private Context _context;

        public RestServiceThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            String topic = intent.getStringExtra(PARAM_TOPIC);
            switch (topic) {
                case TOPIC_LIST:
                    list(_context, intent);
                    break;
                case TOPIC_ACTION:
                    action(_context, intent);
                    break;
                case TOPIC_CREATE:
                    create(_context, intent);
                    break;
                case TOPIC_DELETE:
                    delete(_context, intent);
                    break;
                case TOPIC_GET:
                    get(_context, intent);
                    break;
            }
        }
    }

    private static void list(Context context, Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        String params = intent.getStringExtra(PARAM_URL_PARAMS);
        boolean isSync = intent.getBooleanExtra(PARAM_SYNC, false);

        List<StoredObject> list = StoredObject.list(context, objectType);

        if (list != null && list.size() > 0) {
            for (StoredObject obj : list) {
                RestDispatch.object(context, resultTag, objectType, obj.getObjKey(), obj.toBundle(), isSync);
            }
        }

        RestTransactionBuilder.list(context, resultTag, objectType, params, isSync);
    }

    private static void action(Context context, Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        String id = intent.getStringExtra(PARAM_OBJECT_ID);
        String action = intent.getStringExtra(PARAM_ACTION);
        boolean isSync = intent.getBooleanExtra(PARAM_SYNC, false);
        String params = intent.getStringExtra(PARAM_URL_PARAMS);
        String contentType = intent.getStringExtra(PARAM_CONTENT_TYPE);

        String body = intent.getStringExtra(PARAM_OBJECT_DATA_STRING);

        RestTransactionBuilder.action(context, resultTag, objectType, id, action, params, contentType, body, isSync);

        RestClient.get(context, resultTag, objectType, id, isSync);
    }

    private static void create(Context context, Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);

        if (intent.hasExtra(PARAM_OBJECT_DATA_BYTE_ARRAY)) {
            RestTransactionBuilder.create(context, resultTag, objectType, new String(intent.getByteArrayExtra(PARAM_OBJECT_DATA_BYTE_ARRAY)));
        } else if (intent.hasExtra(PARAM_OBJECT_DATA_STRING)) {
            RestTransactionBuilder.create(context, resultTag, objectType, intent.getStringExtra(PARAM_OBJECT_DATA_STRING));
        }

    }

    private static void delete(Context context, Intent intent) {

    }

    private static void get(Context context, Intent intent) {

    }
}
