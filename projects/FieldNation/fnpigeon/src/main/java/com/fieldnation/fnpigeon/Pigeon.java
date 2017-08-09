package com.fieldnation.fnpigeon;

import android.os.Parcelable;

/**
 * Created by mc on 8/8/17.
 */

public abstract class Pigeon {
    public static final String TAG = "Pigeon";

    public abstract void onMessage(String address, Parcelable message);
}
