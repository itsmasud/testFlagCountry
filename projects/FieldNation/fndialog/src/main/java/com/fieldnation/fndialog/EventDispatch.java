package com.fieldnation.fndialog;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;

/**
 * Created by Michael on 9/19/2016.
 */
public class EventDispatch implements Constants {

    // TODO not sure where to put this =/
    public static void dialogComplete(Context context, Dialog dialog, Bundle response) {
        Bundle payload = new Bundle();
        payload.putString(PARAM_DIALOG_CLASS_NAME, dialog.getClass().getName());
        payload.putBundle(PARAM_DIALOG_RESPONSE, response);

        TopicClient.dispatchEvent(context, TOPIC_ID_DIALOG_COMPLETE + "/" + dialog.getClass().getName(), payload, Sticky.NONE);
    }

}
