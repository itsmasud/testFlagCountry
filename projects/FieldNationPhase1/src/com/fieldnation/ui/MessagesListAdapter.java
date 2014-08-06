package com.fieldnation.ui;

import com.fieldnation.data.profile.Message;
import com.fieldnation.rpc.client.ProfileService;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

public class MessagesListAdapter extends PagingListAdapter<Message> {
	private static final String TAG = "ui.MessagesListAdapter";

	private ProfileService _profileService = null;

	public MessagesListAdapter(Activity activity) {
		super(activity, Message.class);
	}

	@Override
	public View getView(int position, Message obj, View convertView, ViewGroup parent) {
		MessageCardView mv = null;

		if (convertView == null) {
			mv = new MessageCardView(getContext());
		} else if (convertView instanceof MessageCardView) {
			mv = (MessageCardView) convertView;
		} else {
			mv = new MessageCardView(getContext());
		}

		mv.setMessage(obj);

		return mv;
	}

	@Override
	public void invalidateWebService() {
		_profileService = null;
	}

	@Override
	public void getWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		if (_profileService == null) {
			_profileService = new ProfileService(context, username, authToken, resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		_profileService = new ProfileService(context, username, authToken, resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		getContext().startService(_profileService.getUnreadMessages(resultCode, page, allowCache));
	}
}
