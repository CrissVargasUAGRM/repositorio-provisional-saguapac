package bo.com.bg.domain.service;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;

public interface SaguapacAuthService {

    SaguapacTokenResponse authenticate(SaguapacAuthRequest request);
}
