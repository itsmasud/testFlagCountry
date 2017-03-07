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

public class Problem implements Parcelable {
    private static final String TAG = "Problem";

    @Json(name = "author")
    private User _author;

    @Json(name = "comments")
    private String _comments;

    @Json(name = "created")
    private Date _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "message")
    private ProblemMessage _message;

    @Json(name = "resolution")
    private ProblemResolution _resolution;

    @Json(name = "type")
    private ProblemType _type;

    @Source
    private JsonObject SOURCE;

    public Problem() {
        SOURCE = new JsonObject();
    }

    public Problem(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author != null)
                return _author;

            if (SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _author;
    }

    public Problem author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setComments(String comments) throws ParseException {
        _comments = comments;
        SOURCE.put("comments", comments);
    }

    public String getComments() {
        try {
            if (_comments != null)
                return _comments;

            if (SOURCE.has("comments") && SOURCE.get("comments") != null)
                _comments = SOURCE.getString("comments");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _comments;
    }

    public Problem comments(String comments) throws ParseException {
        _comments = comments;
        SOURCE.put("comments", comments);
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created != null)
                return _created;

            if (SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _created;
    }

    public Problem created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Problem id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setMessage(ProblemMessage message) throws ParseException {
        _message = message;
        SOURCE.put("message", message.getJson());
    }

    public ProblemMessage getMessage() {
        try {
            if (_message != null)
                return _message;

            if (SOURCE.has("message") && SOURCE.get("message") != null)
                _message = ProblemMessage.fromJson(SOURCE.getJsonObject("message"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _message;
    }

    public Problem message(ProblemMessage message) throws ParseException {
        _message = message;
        SOURCE.put("message", message.getJson());
        return this;
    }

    public void setResolution(ProblemResolution resolution) throws ParseException {
        _resolution = resolution;
        SOURCE.put("resolution", resolution.getJson());
    }

    public ProblemResolution getResolution() {
        try {
            if (_resolution != null)
                return _resolution;

            if (SOURCE.has("resolution") && SOURCE.get("resolution") != null)
                _resolution = ProblemResolution.fromJson(SOURCE.getJsonObject("resolution"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _resolution;
    }

    public Problem resolution(ProblemResolution resolution) throws ParseException {
        _resolution = resolution;
        SOURCE.put("resolution", resolution.getJson());
        return this;
    }

    public void setType(ProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
    }

    public ProblemType getType() {
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = ProblemType.fromJson(SOURCE.getJsonObject("type"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public Problem type(ProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Problem[] array) {
        JsonArray list = new JsonArray();
        for (Problem item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Problem[] fromJsonArray(JsonArray array) {
        Problem[] list = new Problem[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Problem fromJson(JsonObject obj) {
        try {
            return new Problem(obj);
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
    public static final Parcelable.Creator<Problem> CREATOR = new Parcelable.Creator<Problem>() {

        @Override
        public Problem createFromParcel(Parcel source) {
            try {
                return Problem.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Problem[] newArray(int size) {
            return new Problem[size];
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
