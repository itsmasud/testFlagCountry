package com.fieldnation.fnlog;

/**
 * Created by Michael on 8/8/2016.
 */
public class DefaultRoller implements LogRoller {
    @Override
    public void println(int priority, String tag, String msg) {
        android.util.Log.println(priority, tag, msg);
    }
}
