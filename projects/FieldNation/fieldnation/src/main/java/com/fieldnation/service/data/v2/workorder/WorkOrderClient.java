package com.fieldnation.service.data.v2.workorder;

import android.content.Context;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderClient extends TopicClient implements WorkOrderConstants {
    private static final String STAG = "WorkOrderClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    /*-************************************-*/
    /*-             Life Cycle             -*/
    /*-************************************-*/
    public WorkOrderClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    /*-********************************-*/
    /*-             Search             -*/
    /*-********************************-*/


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);
        }
    }
}
