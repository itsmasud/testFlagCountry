package com.fieldnation.fnlog;

/**
 * Created by Michael on 8/8/2016.
 */
public interface LogRoller {

    void println(int priority, String tag, String msg);
}
