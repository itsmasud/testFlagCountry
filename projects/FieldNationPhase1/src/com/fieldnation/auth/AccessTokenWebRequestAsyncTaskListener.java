package com.fieldnation.auth;

import com.fieldnation.webapi.AccessToken;

public interface AccessTokenWebRequestAsyncTaskListener {
	public void onComplete(AccessToken token);

	public void onFail(Exception e);
}
