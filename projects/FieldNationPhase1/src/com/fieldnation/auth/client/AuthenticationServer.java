package com.fieldnation.auth.client;

import com.fieldnation.GlobalState;

/**
 * <p>
 * An interface that is implemented by any class that will be handling
 * authentication requests.
 * </p>
 * 
 * <p>
 * The class that implements this interface should register itself with
 * {@link GlobalState}
 * </p>
 * 
 * @see GlobalState
 * @see AuthenticationClient
 * @author michael.carver
 * 
 */
public interface AuthenticationServer {
	public void requestAuthentication(AuthenticationClient client);

	public void invalidateToken(String token);

	public void removeAccount(AuthenticationClient client);
}
