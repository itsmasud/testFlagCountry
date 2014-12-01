package com.fieldnation.ui;

import android.content.Intent;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.profile.Message;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;

import java.util.LinkedList;
import java.util.List;

public class MessageListActivity extends ItemListActivity<Message> {
    private static final String TAG = "ui.MessageListActivity";

    // Data
    private ProfileService _service;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/


    @Override
    public Intent requestData(int resultCode, int page, boolean allowCache) {
        if (_service == null)
            return null;

        return _service.getAllMessages(resultCode, page, allowCache);
    }

    @Override
    public View getView(Message object, View convertView, ViewGroup parent) {
        MessageCardView v = null;
        if (convertView == null) {
            v = new MessageCardView(parent.getContext());
        } else if (convertView instanceof MessageCardView) {
            v = (MessageCardView) convertView;
        } else {
            v = new MessageCardView(parent.getContext());
        }

        v.setMessage(object);

        return v;
    }

    @Override
    public void onAuthentication(String username, String authToken, ResultReceiver resultReceiver) {
        _service = new ProfileService(this, username, authToken, resultReceiver);
    }

    @Override
    public List<Message> onParseData(int page, boolean isCached, byte[] data) {
        JsonArray objects = null;
        try {
            objects = new JsonArray(new String(data));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        List<Message> list = new LinkedList<Message>();
        for (int i = 0; i < objects.size(); i++) {
            try {
                list.add(Message.fromJson(objects.getJsonObject(i)));
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
