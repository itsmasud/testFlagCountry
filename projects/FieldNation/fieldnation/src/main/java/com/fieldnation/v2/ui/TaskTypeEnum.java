package com.fieldnation.v2.ui;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.Task;

/**
 * Created by Shoaib on 6/11/17.
 */

public enum TaskTypeEnum {
    SET_ETA(1),
    CLOSING_NOTES(2),
    CHECK_IN(3),
    CHECK_OUT(4),
    UPLOAD_FILE(5),
    UPLOAD_PICTURE(6),
    CUSTOM_FIELD(7),
    PHONE(8),
    EMAIL(9),
    UNIQUE_TASK(10),
    SIGNATURE(11),
    SHIPMENT(12),
    DOWNLOAD(13),
    NOT_SUPPORTED(14);

    private int value;

    TaskTypeEnum(int value) {
        this.value = value;
    }

    private int getValue() {
        return this.value;
    }

    public static TaskTypeEnum fromTypeId(int typeId) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].value == typeId)
                return values()[i];
        }
        return NOT_SUPPORTED;
    }

    public static String getActivityName(Task task) {
        String activityName = "";
        switch (fromTypeId(task.getType().getId())) {
            // will never reach here
            case SET_ETA: // set eta
                break;

            // will never reach here
            case CLOSING_NOTES: // closing notes
                break;

            // will never reach here
            case CHECK_IN: // check in
                break;

            // will never reach here
            case CHECK_OUT: // check out
                break;

            case UPLOAD_FILE: // upload file
                activityName = task.getType().getName() + ": " + task.getLabel();
                break;

            case UPLOAD_PICTURE: // upload picture
                activityName = task.getType().getName() + ": " + task.getLabel();
                break;

            case CUSTOM_FIELD: // custom field
                activityName = task.getLabel();
                break;

            case PHONE: // phone
                activityName = "Call: " + task.getLabel();
                break;

            case EMAIL: // email
                activityName = "Email: " + task.getLabel();
                break;

            case UNIQUE_TASK: // unique task
                activityName = App.get().getResources().getString(R.string.complete_tasks) + ": " + task.getLabel();
                break;

            // will never reach here
            case SIGNATURE: // signature
                break;

            // will never reach here
            case SHIPMENT: // shipment
                break;

            case DOWNLOAD:
                activityName = task.getType().getName() + ": " + task.getLabel();
                break;
        }
        return activityName;

    }
}