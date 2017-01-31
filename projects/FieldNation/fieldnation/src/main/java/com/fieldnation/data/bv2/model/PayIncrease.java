package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class PayIncrease implements Parcelable {
    private static final String TAG = "PayIncrease";

    @Json(name = "status_description")
    private String _statusDescription;

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "description")
    private String _description;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "status")
    private StatusEnum _status;

    public PayIncrease() {
    }

    public void setStatusDescription(String statusDescription) {
        _statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public PayIncrease statusDescription(String statusDescription) {
        _statusDescription = statusDescription;
        return this;
    }

    public void setAuthor(User author) {
        _author = author;
    }

    public User getAuthor() {
        return _author;
    }

    public PayIncrease author(User author) {
        _author = author;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public PayIncrease created(Date created) {
        _created = created;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public PayIncrease description(String description) {
        _description = description;
        return this;
    }

    public void setPay(Pay pay) {
        _pay = pay;
    }

    public Pay getPay() {
        return _pay;
    }

    public PayIncrease pay(Pay pay) {
        _pay = pay;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public PayIncrease id(Integer id) {
        _id = id;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public PayIncrease actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public PayIncrease status(StatusEnum status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayIncrease[] fromJsonArray(JsonArray array) {
        PayIncrease[] list = new PayIncrease[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayIncrease fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayIncrease.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayIncrease payIncrease) {
        try {
            return Serializer.serializeObject(payIncrease);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayIncrease> CREATOR = new Parcelable.Creator<PayIncrease>() {

        @Override
        public PayIncrease createFromParcel(Parcel source) {
            try {
                return PayIncrease.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayIncrease[] newArray(int size) {
            return new PayIncrease[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
