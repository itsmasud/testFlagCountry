package com.fieldnation.service.topics;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 2/27/2015.
 */
class TopicService extends Service implements TopicConstants {

    private Messenger _me = new Messenger(new IncomeHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        return _me.getBinder();
    }

    private void addListener(Bundle bundle, Messenger replyTo) {
        String topicId = bundle.getString(PARAM_TOPIC_ID);
        String tag = bundle.getString(PARAM_TOPIC_TAG);

        replyTo.hashCode()
    }


    /*-**********************************-*/
    /*-              Plumbing            -*/
    /*-**********************************-*/
    private static class IncomeHandler extends Handler {
        private WeakReference<TopicService> _oss;

        public IncomeHandler(TopicService oss) {
            _oss = new WeakReference<>(oss);
        }

        @Override
        public void handleMessage(Message msg) {
            TopicService svc = _oss.get();
            if (svc == null) {
                super.handleMessage(msg);
                return;
            }

            switch (msg.what) {
                case WHAT_ADD_LISTENER:
                    svc.addListener(msg.getData(), msg.replyTo);
                    break;
            }

            super.handleMessage(msg);
        }
    }

}
