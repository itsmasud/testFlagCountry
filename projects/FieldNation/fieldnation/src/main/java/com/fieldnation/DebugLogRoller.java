package com.fieldnation;

import com.fieldnation.fnlog.LogRoller;

/**
 * Created by Michael on 8/8/2016.
 */
public class DebugLogRoller implements LogRoller {
    @Override
    public void println(final int priority, final String tag, final String msg) {
        Debug.log(priority, tag, msg);
    }

    @Override
    public void logException(final Throwable throwable) {
        Debug.logException(throwable);
    }
}
