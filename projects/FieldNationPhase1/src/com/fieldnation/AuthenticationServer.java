package com.fieldnation;

import android.accounts.Account;

public interface AuthenticationServer {
	public void requestAuthentication(AuthenticationClient client);

	public void invalidateToken(String token);
}
