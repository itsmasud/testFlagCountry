package com.fieldnation;

import com.fieldnation.data.messages.Message;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;

import android.content.Context;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MessagesListAdapter extends PagingListAdapter<Message> {
	private static final String TAG = "MessagesListAdapter";

	private ProfileService _profileService = null;

	public MessagesListAdapter(Context context) {
		super(context, Message.class);
	}

	@Override
	public View getView(Message obj, View convertView, ViewGroup parent) {
		MessageView mv = null;

		if (convertView == null) {
			mv = new MessageView(getContext());
		} else if (convertView instanceof MessageView) {
			mv = (MessageView) convertView;
		} else {
			mv = new MessageView(getContext());
		}

		mv.setMessage(obj);

		return mv;
	}

	@Override
	public void invalidateWebervice() {
		_profileService = null;
	}

	@Override
	public void getWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		if (_profileService == null) {
			_profileService = new ProfileService(context, username, authToken,
					resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		_profileService = new ProfileService(context, username, authToken,
				resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		getContext().startService(
				_profileService.getAllMessages(resultCode, page, allowCache));
	}
}
