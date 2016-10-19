package com.fieldnation.service.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class OAuth implements Parcelable {
    private static final String TAG = "OAuth";

//    public static final String KEY_OAUTH = TAG;

    @Json(name = "id")
    private long _id = -1;
    @Json(name = "expires_in")
    private long _expiresIn;
    @Json(name = "refresh_token")
    private String _refreshToken;
    @Json(name = "scope")
    private String _scope;
    @Json(name = "token_type")
    private String _tokenType;
    @Json(name = "access_token")
    private String _accessToken;
    @Json(name = "username")
    private String _username;
    @Json(name = "host")
    private String _host;
    @Json(name = "error")
    private String _error;
    @Json(name = "error_description")
    private String _errorDescription;

    public OAuth() {
    }

    public long getExpiresIn() {
        return _expiresIn;
    }

    public String getRefreshToken() {
        return _refreshToken;
    }

    public String getScope() {
        return _scope;
    }

    public String getTokenType() {
        return _tokenType;
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public String getUsername() {
        return _username;
    }

    public String getHost() {
        return _host;
    }

    public boolean hasError() {
        try {
            return _error != null;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }

    public String getErrorType() {
        try {
            return _error;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public String getErrorDescription() {
        try {
            return _errorDescription;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(OAuth oauth) {
        try {
            return Serializer.serializeObject(oauth);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static OAuth fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(OAuth.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************-*/
    /*-             Human Code          -*/
    /*-*********************************-*/
    public void delete() {
        if (_id != -1)
            StoredObject.delete(App.get(), _id);
    }

    public void save() {
        StoredObject obj = StoredObject.put(App.get(), 0, "OAuthToken", _username, null);
        _id = obj.getId();
        obj.setData(toJson().toByteArray());
        obj.save(App.get());
    }

    public void applyToRequest(JsonObject request) throws ParseException {
        String params = null;
        if (request.has(HttpJsonBuilder.PARAM_WEB_URL_PARAMS))
            params = request.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);

        if (params == null || params.equals("")) {
            request.put(HttpJsonBuilder.PARAM_WEB_URL_PARAMS, "?access_token=" + getAccessToken());
            return;
        } else if (params.startsWith("?")) { // if options already specified
            request.put(HttpJsonBuilder.PARAM_WEB_URL_PARAMS, "?access_token=" + getAccessToken() + "&" + params.substring(1));
            return;
        }
        throw new ParseException("Options must be nothing, or start with '?'. Got: " + params, 0);
    }

    public static void flushAll() {
        StoredObject.flushAllOfType(App.get(), "OAuthToken");
    }

    public static OAuth lookup(String username) {
        try {
            StoredObject obj = StoredObject.get(App.get(), 0, "OAuthToken", username);

            if (obj == null)
                return null;

            return fromJson(new JsonObject(obj.getData()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static List<OAuth> list() {
        try {
            List<StoredObject> objs = StoredObject.list(App.get(), 0, "OAuthToken");
            List<OAuth> list = new LinkedList<>();
            for (int i = 0; i < objs.size(); i++) {
                try {
                    list.add(fromJson(new JsonObject(objs.get(i).getData())));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
            return list;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static OAuth authenticate(String host, String path, String grantType,
                                     String clientId, String clientSecret, String username,
                                     String password) throws Exception {

        HttpJsonBuilder builder = new HttpJsonBuilder()
                .method("POST")
                .protocol("https")
                .host(host)
                .path(path)
                .urlParams("?as_provider=1")
                .timingKey("POST" + path)
                .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                .body("grant_type=" + grantType +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&username=" + misc.escapeForURL(username) +
                        "&password=" + misc.escapeForURL(password));

        HttpResult result = HttpJson.run(App.get(), builder.build());

        Log.v(TAG, result.getResponseCode() + "");
        Log.v(TAG, result.getResponseMessage());

        JsonObject token = result.getJsonObject();
        token.put("username", username);
        token.put("host", host);
        OAuth auth = OAuth.fromJson(token);
        auth.save();
        return auth;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<OAuth> CREATOR = new Parcelable.Creator<OAuth>() {

        @Override
        public OAuth createFromParcel(Parcel source) {
            try {
                return OAuth.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
        }

        @Override
        public OAuth[] newArray(int size) {
            return new OAuth[size];
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

