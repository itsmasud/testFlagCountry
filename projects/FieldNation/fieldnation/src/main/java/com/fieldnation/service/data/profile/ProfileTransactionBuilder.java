package com.fieldnation.service.data.profile;

import android.content.Context;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class ProfileTransactionBuilder implements ProfileConstants {
    private static final String TAG = "ProfileTransactionBuilder";

    public static void get(Context context, long profileId, boolean isSync) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET");

            if (profileId > 0) {
                http.path("/api/rest/v1/profile/" + profileId);
            } else {
                http.path("/api/rest/v1/profile");
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pGet(profileId))
                    .key((isSync ? "Sync/" : "") + "ProfileGet")
                    .isSyncCall(isSync)
                    .useAuth(true)
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listNotifications(Context context, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/notifications")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pListNotifications(page))
                    .key((isSync ? "Sync/" : "") + "NotificationPage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/notifications")
                                    .urlParams("?page=" + page)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listMessages(Context context, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/messages")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pListMessages(page))
                    .key((isSync ? "Sync/" : "") + "MessagePage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/messages")
                                    .urlParams("?page=" + page)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void switchUser(Context context, long userId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/[userId]/switch")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pSwitchUser(userId))
                    .key("SwitchUser / " + userId)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/" + userId + "/switch")
                    ).build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void action(Context context, long profileId, String action, String params,
                              String contentType, String body) {
        action(context, profileId, "POST/api/rest/v1/profile/[profileId]/" + action, action, params, contentType, body);
    }

    public static void action(Context context, long profileId, String timingKey, String action, String params,
                              String contentType, String body) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/profile/" + profileId + "/" + action);

            if (params != null) {
                http.urlParams(params);
            }

            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey(timingKey)
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pAction(profileId, action))
                    .useAuth(true)
                    .key("Profile/" + profileId + "/" + action)
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void actionAcceptTos(Context context, long profileId) {
        action(context, profileId, "accept-toc", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, null);
    }

    public static void actionBlockCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        action(context, profileId, "POST/api/rest/v1/profile/[profileId]/block/[CompanyId]", "block/" + companyId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "eventReasonId=" + eventReasonId
                        + "&explanation=" + misc.escapeForURL(explanation));
    }

    public static void actionRegisterPhone(Context context, String deviceId, long profileId) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/api/record");

            JsonObject body = new JsonObject();
            body.put("ref", 1);
            body.put("record", "register-mobile-device");
            body.put("data.item_type", "gcm");
            body.put("data.device_id", deviceId);
            body.put("data.user_id", profileId);

            http.body("[" + body.toString() + "]");

            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, "application/json");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/api/record")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pAction(0, "register_device"))
                    .useAuth(true)
                    .key("Profile/RegisterDevice")
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the deliverable details
    public static void uploadProfilePhoto(Context context, String filename, String filePath, long profileId) {
        Log.v(TAG, "uploadProfilePhoto file");
        try {
            StoredObject upFile = StoredObject.put(context, App.getProfileId(), "TempFile", filePath, new FileInputStream(new File(filePath)), "uploadTemp.dat");
            uploadProfilePhoto(context, upFile, filename, filePath, profileId);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void uploadProfilePhoto(Context context, InputStream inputStream, String filename, String filePath, long profileId) {
        Log.v(TAG, "uploadProfilePhoto uri");
        StoredObject upFile = StoredObject.put(context, App.getProfileId(), "TempFile", filePath, inputStream, "uploadTemp.dat");
        uploadProfilePhoto(context, upFile, filename, filePath, profileId);
    }

    public static void uploadProfilePhoto(Context context, StoredObject upFile, String filename, String filePath, long profileId) {
        Log.v(TAG, "uploadProfilePhoto uri");

        if (upFile == null) {
            ToastClient.toast(context, "Unknown error uploading file, please try again", Toast.LENGTH_SHORT);
            Log.logException(new Exception("PA-332 - UpFile is null"));
            ProfileDispatch.uploadProfilePhoto(context, filePath, false, true);
            return;
        }


        if (upFile.size() > 100000000) { // 100 MB?
            StoredObject.delete(context, upFile);
            ToastClient.toast(context, "File is too long: " + filePath, Toast.LENGTH_LONG);
            ProfileDispatch.uploadProfilePhoto(context, filePath, false, true);
            return;
        }

/* This will dump the file into Downloads/FieldNation for debugging purposes.
        if (upFile.isFile() && upFile.getFile() != null) {
            try {
                FileUtils.copyFile(upFile.getFile(), new File(App.get().getDownloadsFolder() + "/" + filename));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fo = new FileOutputStream(new File(App.get().getDownloadsFolder() + "/" + filename));
                fo.write(upFile.getData());
                fo.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
*/

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + profileId + "/profile/avatar")
                    .multipartFile("file", filename, upFile)
                    .doNotRead();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v2/users/[profileId]/profile/avatar")
                    .priority(Priority.LOW)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pUploadPhoto(profileId, filename))
                    .useAuth(true)
                    .request(builder)
                    .setWifiRequired(App.get().onlyUploadWithWifi())
                    .setTrack(true)
                    .setTrackType(TrackerEnum.PHOTOS)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


}
