package com.fieldnation.rpc.client;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class ProfileService extends WebService {

	public ProfileService(Context context, String username, String authToken, ResultReceiver callback) {
		super(context, username, authToken, callback);
	}

	public Intent getUserInformation(int resultCode, long userId,
			boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/profile/" + userId, allowCache);
	}

	public Intent getAllNotifications(int resultCode, long userId, int page,
			boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/profile/notifications/" + userId,
				"?page=" + page, allowCache);
	}

	public Intent getNewNotifications(int resultCode, long userId, int page,
			boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/profile/notifications/new/" + userId,
				"?page=" + page, allowCache);
	}

	public Intent getAllMessages(int resultCode, long userId, int page,
			boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/profile/messages/unread/" + userId,
				"?page=" + page, allowCache);
	}

	public Intent getNewMessages(int resultCode, long userId, int page,
			boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/profile/messages/" + userId,
				"?page=" + page, allowCache);
	}
}
