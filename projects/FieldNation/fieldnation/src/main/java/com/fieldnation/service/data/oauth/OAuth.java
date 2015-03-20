package com.fieldnation.service.data.oauth;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class OAuth implements Parcelable {
    private static final String TAG = "rpc.server.auth.OAuth";

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
            ex.printStackTrace();
        }
        return false;
    }

    public String getErrorType() {
        try {
            return _error;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getErrorDescription() {
        try {
            return _errorDescription;
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
            return null;
        }
    }

    public static OAuth fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(OAuth.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*-*********************************-*/
    /*-             Human Code          -*/
    /*-*********************************-*/
    public void delete(Context context) {
        if (_id != -1)
            StoredObject.delete(context, _id);
    }

    public void save(Context context) {
        StoredObject obj = StoredObject.put(context, "OAuthToken", _username, (byte[]) null);
        _id = obj.getId();
        obj.setData(toJson().toByteArray());
        obj.save(context);
    }

    public String applyToRequest(JsonObject request) throws ParseException {
        String params = null;
        if (request.has(HttpJsonBuilder.PARAM_WEB_URL_PARAMS))
            params = request.getString(HttpJsonBuilder.PARAM_WEB_URL_PARAMS);

        if (params == null || params.equals("")) {
            return "?access_token=" + getAccessToken();
        } else if (params.startsWith("?")) { // if options already specified
            return "?access_token=" + getAccessToken() + "&" + params.substring(1);
        }
        throw new ParseException("Options must be nothing, or start with '?'. Got: " + params, 0);
    }

    public static OAuth lookup(Context context, String username) {
        try {
            StoredObject obj = StoredObject.get(context, "OAuthToken", username);

            if (obj == null)
                return null;

            return fromJson(new JsonObject(obj.getData()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static OAuth authenticate(Context context, String host, String path, String grantType,
                                     String clientId, String clientSecret, String username,
                                     String password) throws ParseException, IOException {

        HttpJsonBuilder builder = new HttpJsonBuilder()
                .method("POST")
                .protocol("https")
                .host(host)
                .path(path)
                .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                .body("grant_type=" + grantType +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&username=" + misc.escapeForURL(username) +
                        "&password=" + misc.escapeForURL(password));

        HttpResult result = HttpJson.run(context, builder.build());

        Log.v(TAG, result.getResponseCode() + "");

        JsonObject token = result.getResultsAsJsonObject();
        token.put("username", username);
        token.put("host", host);
        OAuth auth = OAuth.fromJson(token);
        auth.save(context);
        return auth;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<OAuth> CREATOR = new Parcelable.Creator<OAuth>() {

        @Override
        public OAuth createFromParcel(Parcel source) {
            try {
                return OAuth.fromJson(new JsonObject(source.readString()));
            } catch (Exception e) {
                e.printStackTrace();
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
        dest.writeString(toJson().toString());
    }


}

