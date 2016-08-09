package com.fieldnation.service.data.restv2;

import android.content.Intent;

import com.fieldnation.App;
import com.fieldnation.fntools.MultiThreadedService;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.fnpigeon.Sticky;

import java.util.List;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestService extends MultiThreadedService implements RestConstants {

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public void processIntent(Intent intent) {
        if (intent.hasExtra(PARAM_TOPIC)) {
            String topic = intent.getStringExtra(PARAM_TOPIC);
            switch (topic) {
                case TOPIC_LIST:
                    list(intent);
                    break;
                case TOPIC_ACTION:
                    action(intent);
                    break;
                case TOPIC_CREATE:
                    create(intent);
                    break;
                case TOPIC_DELETE:
                    delete(intent);
                    break;
                case TOPIC_GET:
                    get(intent);
                    break;
            }
        }
    }

    private void list(Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        String params = intent.getStringExtra(PARAM_URL_PARAMS);
        boolean isSync = intent.getBooleanExtra(PARAM_SYNC, false);
        Sticky sticky = (Sticky) intent.getSerializableExtra(PARAM_STICKY);

        List<StoredObject> list = StoredObject.list(App.getProfileId(), objectType);

        if (list != null && list.size() > 0) {
            for (StoredObject obj : list) {
                RestDispatch.object(this, resultTag, objectType, obj.getObjKey(), obj.toBundle(), sticky, isSync);
            }
        }

        RestTransactionBuilder.list(this, resultTag, objectType, params, sticky, isSync);
    }

    private void action(Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        String id = intent.getStringExtra(PARAM_OBJECT_ID);
        String action = intent.getStringExtra(PARAM_ACTION);
        boolean isSync = intent.getBooleanExtra(PARAM_SYNC, false);
        String params = intent.getStringExtra(PARAM_URL_PARAMS);
        String contentType = intent.getStringExtra(PARAM_CONTENT_TYPE);

        String body = intent.getStringExtra(PARAM_OBJECT_DATA_STRING);

        Sticky sticky = (Sticky) intent.getSerializableExtra(PARAM_STICKY);

        RestTransactionBuilder.action(this, resultTag, objectType, id, action, params, contentType, body, sticky, isSync);

        RestClient.get(this, resultTag, objectType, id, sticky, isSync);
    }

    private void create(Intent intent) {
        String resultTag = intent.getStringExtra(PARAM_RESULT_TAG);
        String objectType = intent.getStringExtra(PARAM_OBJECT_TYPE);
        Sticky sticky = (Sticky) intent.getSerializableExtra(PARAM_STICKY);

        if (intent.hasExtra(PARAM_OBJECT_DATA_BYTE_ARRAY)) {
            RestTransactionBuilder.create(this, resultTag, objectType, new String(intent.getByteArrayExtra(PARAM_OBJECT_DATA_BYTE_ARRAY)), sticky);
        } else if (intent.hasExtra(PARAM_OBJECT_DATA_STRING)) {
            RestTransactionBuilder.create(this, resultTag, objectType, intent.getStringExtra(PARAM_OBJECT_DATA_STRING), sticky);
        }

    }

    private void delete(Intent intent) {

    }

    private void get(Intent intent) {

    }
}
