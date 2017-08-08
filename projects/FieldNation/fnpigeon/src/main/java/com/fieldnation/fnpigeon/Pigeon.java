package com.fieldnation.fnpigeon;

import android.os.Parcelable;

/**
 * Created by mc on 8/8/17.
 */

public abstract class Pigeon implements TopicConstants {
    public static final String TAG = "Pigeon";

    public abstract void onTopic(String topicId, Parcelable payload);
}
