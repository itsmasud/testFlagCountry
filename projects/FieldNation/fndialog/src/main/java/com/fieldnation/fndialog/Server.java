package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by Michael on 9/19/2016.
 */
class Server extends TopicClient implements Constants {
    private static final String STAG = "Server";
    private final String TAG = UniqueTag.makeTag(STAG);

    public Server(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static abstract class Listener extends TopicClient.Listener {
        private static final String TAG = "Server.Listener";

        public abstract Server getClient();

        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_SHOW_DIALOG);
            getClient().register(TOPIC_ID_DISMISS_DIALOG);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Bundle bundle = (Bundle) payload;
            switch (topicId) {
                case TOPIC_ID_SHOW_DIALOG:
                    onShowDialog(bundle.getString(PARAM_DIALOG_CLASS_NAME), bundle.getClassLoader(), bundle.getBundle(PARAM_DIALOG_PARAMS));
                    break;
                case TOPIC_ID_DISMISS_DIALOG:
                    onDismissDialog(bundle.getString(PARAM_DIALOG_CLASS_NAME));
                    break;
                default:
                    break;
            }
        }

        public abstract void onShowDialog(String className, ClassLoader classLoader, Bundle params);

        public abstract void onDismissDialog(String className);
    }
}
