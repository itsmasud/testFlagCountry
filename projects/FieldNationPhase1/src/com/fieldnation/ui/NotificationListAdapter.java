package com.fieldnation.ui;

import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.rpc.client.ProfileService;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

public class NotificationListAdapter extends PagingListAdapter<Notification> {
	private static final String TAG = "ui.NotificationListAdapter";

	private ProfileService _profileService = null;

	public NotificationListAdapter(Activity activity) {
		super(activity, Notification.class);
	}

	@Override
	public View getView(Notification obj, View convertView, ViewGroup parent) {
		NotificationView note = null;

		if (convertView == null) {
			note = new NotificationView(getContext());
		} else if (convertView instanceof MessageCardView) {
			note = (NotificationView) convertView;
		} else {
			note = new NotificationView(getContext());
		}

		note.setNotification(obj);

		return note;
	}

	@Override
	public void invalidateWebervice() {
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
		getContext().startService(_profileService.getAllNotifications(resultCode, page, allowCache));
	}
}
