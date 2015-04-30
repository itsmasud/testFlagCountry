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
            String topic = intent.getStringExtra(PARAM_ACTION);
            switch (topic) {
                case TOPIC_LIST:
                    list(_context, intent);
                    break;
            }
        }
    }

    private static void list(Context context, Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        boolean isSync = intent.getBooleanExtra(PARAM_SYNC, false);

        List<StoredObject> list = StoredObject.list(context, objectType);

        if (list != null && list.size() > 0) {
            for (StoredObject obj : list) {
                RestDispatch.object(context, resultTag, objectType, obj.getObjKey(), obj.toBundle(), isSync);
            }
        }


    }
}
