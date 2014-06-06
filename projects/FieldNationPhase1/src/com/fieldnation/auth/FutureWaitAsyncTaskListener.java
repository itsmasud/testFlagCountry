package com.fieldnation.auth;

import android.os.Bundle;

public interface FutureWaitAsyncTaskListener {
	public void onComplete(Bundle bundle);

	public void onFail(Exception ex);
}
