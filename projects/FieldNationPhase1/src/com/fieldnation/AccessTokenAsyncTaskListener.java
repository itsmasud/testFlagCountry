package com.fieldnation;

import com.fieldnation.webapi.AccessToken;

public interface AccessTokenAsyncTaskListener {
	public void onComplete(AccessToken token);

	public void onFail(Exception e);
}
