package com.fieldnation;

import android.content.Intent;

/**
 * Created by Michael Carver on 1/8/2015.
 */
public class ActivityResult {
    public int requestCode;
    public int resultCode;
    public Intent data;

    public ActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
}
