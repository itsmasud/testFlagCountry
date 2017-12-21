package com.fieldnation.v2.ui;

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

    public static TaskTypeEnum fromTypeId(int id) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].value == id)
                return values()[i];
        }
        return NOT_SUPPORTED;
    }
}