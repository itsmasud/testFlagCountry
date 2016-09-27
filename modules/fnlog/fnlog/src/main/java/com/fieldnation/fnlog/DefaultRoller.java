package com.fieldnation.fnlog;

/**
 * Created by Michael on 8/8/2016.
 */
public class DefaultRoller implements LogRoller {
    @Override
    public void println(final int priority, final String tag, final String msg) {
        android.util.Log.println(priority, tag, msg);
    }

    @Override
    public void logException(final Throwable throwable) {
        Log.d("EXCEPTION", throwable);
    }
}
