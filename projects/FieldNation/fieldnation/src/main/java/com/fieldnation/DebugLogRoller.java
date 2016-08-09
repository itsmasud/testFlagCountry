package com.fieldnation;

import com.fieldnation.fnlog.LogRoller;

/**
 * Created by Michael on 8/8/2016.
 */
public class DebugLogRoller implements LogRoller {
    @Override
    public void println(int priority, String tag, String msg) {
        Debug.log(priority, tag, msg);
    }
}
