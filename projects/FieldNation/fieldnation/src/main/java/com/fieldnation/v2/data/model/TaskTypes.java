package com.fieldnation.v2.data.model;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;

public class TaskTypes {
    private static final String STAG = "TaskTypes";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static TaskType[] _taskTypes = null;

    static {
        _taskTypes = new TaskType[13];
        try {
            _taskTypes[0] = new TaskType(1, "Confirm Assignment");
            _taskTypes[1] = new TaskType(2, "Enter close out notes");
            _taskTypes[2] = new TaskType(3, "Check in");
            _taskTypes[3] = new TaskType(4, "Check out");
            _taskTypes[4] = new TaskType(5, "Upload a file");
            _taskTypes[5] = new TaskType(6, "Upload or take a picture");
            _taskTypes[6] = new TaskType(7, "Fill out custom field");
            _taskTypes[7] = new TaskType(8, "Call phone number");
            _taskTypes[8] = new TaskType(9, "Send an e-mail");
            _taskTypes[9] = new TaskType(10, "Unique task");
            _taskTypes[10] = new TaskType(11, "Collect a signature");
            _taskTypes[11] = new TaskType(12, "Enter shipment tracking");
            _taskTypes[12] = new TaskType(13, "Download a File");
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    private final Context _context;
    private Listener _listener = null;

    public TaskTypes(Context context) {
        _context = context.getApplicationContext();
    }

    public void setListener(Listener listener) {
        _listener = listener;

        if (_taskTypes != null) {
            _listener.onHaveTypes(_taskTypes);
        }
    }

    public interface Listener {
        void onHaveTypes(TaskType[] taskTypes);
    }
}
