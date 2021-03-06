package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Task implements Parcelable {
    private static final String TAG = "Task";

    @Json(name = "alertOnCompletion")
    private String[] _alertOnCompletion;
    @Json(name = "alertOnCompletionInput")
    private String _alertOnCompletionInput;
    @Json(name = "assignedTo")
    private String _assignedTo;
    @Json(name = "canDelete")
    private Boolean _canDelete;
    @Json(name = "completed")
    private Boolean _completed;
    @Json(name = "completedAtDate")
    private String _completedAtDate;
    @Json(name = "customField")
    private Long _customField;
    @Json(name = "description")
    private String _description;
    @Json(name = "emailAddress")
    private String _emailAddress;
    @Json(name = "hours")
    private Integer _hours;
    @Json(name = "identifier")
    private Integer _identifier;
    @Json(name = "isEditing")
    private Boolean _isEditing;
    @Json(name = "isNew")
    private Boolean _isNew;
    @Json(name = "minutes")
    private Integer _minutes;
    @Json(name = "order")
    private Integer _order;
    @Json(name = "phoneNumber")
    private String _phoneNumber;
    @Json(name = "showAlertMenu")
    private Boolean _showAlertMenu;
    @Json(name = "showTimeMenu")
    private Boolean _showTimeMenu;
    @Json(name = "slotData")
    private SlotData _slotData;
    @Json(name = "slotId")
    private Long _slotId;
    @Json(name = "stage")
    private String _stage;
    @Json(name = "taskId")
    private long _taskId;
    @Json(name = "taskIdentifier")
    private Integer _taskIdentifier;
    @Json(name = "taskIsCompleted")
    private Integer _taskIsCompleted;
    @Json(name = "timeRelativeTo")
    private String _timeRelativeTo;
    @Json(name = "type")
    private String _type;
    @Json(name = "typeId")
    private Integer _typeId;
    @Json(name = "workorderTaskId")
    private Integer _workorderTaskId;

    public Task() {
    }

    public String[] getAlertOnCompletion() {
        return _alertOnCompletion;
    }

    public String getAlertOnCompletionInput() {
        return _alertOnCompletionInput;
    }

    public String getAssignedTo() {
        return _assignedTo;
    }

    public Boolean getCanDelete() {
        return _canDelete;
    }

    public Boolean getCompleted() {
        return _completed;
    }

    public String getCompletedAtDate() {
        return _completedAtDate;
    }

    public Long getCustomField() {
        return _customField;
    }

    public String getDescription() {
        return _description;
    }

    public String getEmailAddress() {
        return _emailAddress;
    }

    public Integer getHours() {
        return _hours;
    }

    public Integer getIdentifier() {
        return _identifier;
    }

    public Boolean getIsEditing() {
        return _isEditing;
    }

    public Boolean getIsNew() {
        return _isNew;
    }

    public Integer getMinutes() {
        return _minutes;
    }

    public Integer getOrder() {
        return _order;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public Boolean getShowAlertMenu() {
        return _showAlertMenu;
    }

    public Boolean getShowTimeMenu() {
        return _showTimeMenu;
    }

    public SlotData getSlotData() {
        return _slotData;
    }

    public Long getSlotId() {
        if (_slotId != null)
            return _slotId;

        if (_identifier != null)
            return (long) _identifier;

        return null;
    }

    public String getStage() {
        return _stage;
    }

    public long getTaskId() {
        return _taskId;
    }

    public Integer getTaskIdentifier() {
        return _taskIdentifier;
    }

    public Integer getTaskIsCompleted() {
        return _taskIsCompleted;
    }

    public String getTimeRelativeTo() {
        return _timeRelativeTo;
    }

    public String getType() {
        return _type;
    }

    public Integer getTypeId() {
        return _typeId;
    }

    public Integer getWorkorderTaskId() {
        return _workorderTaskId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Task task) {
        try {
            return Serializer.serializeObject(task);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Task fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Task.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-				Not Generated Code				-*/
    /*-*********************************************-*/
    public TaskType getTaskType() {
        return TaskType.fromId(_typeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel source) {
            try {
                return Task.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
