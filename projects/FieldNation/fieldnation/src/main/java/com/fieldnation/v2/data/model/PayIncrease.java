package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class PayIncrease implements Parcelable {
    private static final String TAG = "PayIncrease";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "status_description")
    private String _statusDescription;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayIncrease() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public PayIncrease actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        return _author;
    }

    public PayIncrease author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public PayIncrease created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        return _description;
    }

    public PayIncrease description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public PayIncrease id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setPay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
    }

    public Pay getPay() {
        return _pay;
    }

    public PayIncrease pay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public PayIncrease status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setStatusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public PayIncrease statusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "accepted")
        ACCEPTED("accepted"),
        @Json(name = "declined")
        DECLINED("declined"),
        @Json(name = "pending")
        PENDING("pending");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "accept")
        ACCEPT("accept"),
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayIncrease[] array) {
        JsonArray list = new JsonArray();
        for (PayIncrease item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
