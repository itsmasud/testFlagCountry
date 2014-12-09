package com.fieldnation.ui;

import android.content.Intent;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.profile.Notification;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;

import java.util.LinkedList;
import java.util.List;

public class NotificationListActivity extends ItemListActivity<Notification> {
    private static final String TAG = "ui.NotificationListActivity";

    // Data
    private ProfileService _service;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


    @Override
    public Intent requestData(int resultCode, int page, boolean allowCache) {
        if (_service == null)
            return null;

        return _service.getAllNotifications(resultCode, page, allowCache);
    }

    @Override
    public View getView(Notification object, View convertView, ViewGroup parent) {
        NotificationView v = null;
        if (convertView == null) {
            v = new NotificationView(parent.getContext());
        } else if (convertView instanceof NotificationView) {
            v = (NotificationView) convertView;
        } else {
            v = new NotificationView(parent.getContext());
        }

        v.setNotification(object);

        return v;
    }

    @Override
    public void onAuthentication(String username, String authToken, ResultReceiver resultReceiver) {
        _service = new ProfileService(this, username, authToken, resultReceiver);
    }

    @Override
    public List<Notification> onParseData(int page, boolean isCached, byte[] data) {
        JsonArray objects = null;
        try {
            objects = new JsonArray(new String(data));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        List<Notification> list = new LinkedList<Notification>();
        for (int i = 0; i < objects.size(); i++) {
            try {
                list.add(Notification.fromJson(objects.getJsonObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    @Override
    public void invalidateService() {
        _service = null;
    }
}
