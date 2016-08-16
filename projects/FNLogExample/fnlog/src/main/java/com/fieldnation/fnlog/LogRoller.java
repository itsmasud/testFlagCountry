package com.fieldnation.fnlog;

/**
 * Created by Michael on 8/8/2016.
 */
public interface LogRoller {

    void println(final int priority, final String tag, final String msg);

    void logException(final Throwable throwable);
}
