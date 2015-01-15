package com.fieldnation.ui;

import android.content.Intent;
import android.os.ResultReceiver;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;
import com.fieldnation.ui.workorder.WorkorderActivity;

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
    protected void onResume() {
        super.onResume();
        Topics.dispatchGaEvent(this, Topics.GA_CATEGORY_GENERAL, Topics.GA_ACTION_VIEW, "MessageListActivity", 1);

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
        v.setOnClickListener(_message_onClick);

        return v;
    }

    private View.OnClickListener _message_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageCardView mv = (MessageCardView) v;
            Intent intent = new Intent(MessageListActivity.this, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_MESSAGE);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, mv.getMessage().getWorkorderId());
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages, menu);

        _notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));

        return true;
    }


    @Override
    public void onAuthentication(String username, String authToken, boolean isNew, ResultReceiver resultReceiver) {
        if (_service == null || isNew) {
            _service = new ProfileService(this, username, authToken, resultReceiver);
        }
    }

    @Override
    public void invalidateService() {
        _service = null;
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

        // tell the system that the profile info is now invalid
        TopicService.dispatchTopic(this, ProfileService.TOPIC_PROFILE_INVALIDATED, null);

        return list;
    }

}
