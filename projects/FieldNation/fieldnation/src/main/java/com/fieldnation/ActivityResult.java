package com.fieldnation;

import android.content.Intent;

/**
 * Created by Michael Carver on 1/8/2015.
 */
public class ActivityResult {
    public final int requestCode;
    public final int resultCode;
    public final Intent data;

    public ActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
}
