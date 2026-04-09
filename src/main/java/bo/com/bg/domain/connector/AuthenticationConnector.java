package bo.com.bg.domain.connector;

import bo.com.bg.domain.connector.request.SaguapacLoginRequest;

public interface AuthenticationConnector {

    String getToken(SaguapacLoginRequest request);
}
