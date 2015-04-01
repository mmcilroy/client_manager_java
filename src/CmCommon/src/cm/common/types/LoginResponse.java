package cm.common.types;

import java.io.Serializable;

import cm.common.types.LoginResult;

@SuppressWarnings("serial")
public class LoginResponse implements Serializable
{
	public LoginResponse( LoginResult result )
	{
		this.result = result;
	}

	public LoginResult result;

	public String customerServiceId;
	public String logServiceId;
	public Integer subscriptionPort;
}
