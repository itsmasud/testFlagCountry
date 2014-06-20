package com.fieldnation;

public interface AuthenticationServer {
	public void requestAuthentication(AuthenticationClient client);

	public void invalidateToken(String token);
}
