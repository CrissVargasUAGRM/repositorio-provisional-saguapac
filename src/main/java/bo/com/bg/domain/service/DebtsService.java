package bo.com.bg.domain.service;

import bo.com.bg.app.request.DebtsRequest;
import bo.com.bg.app.response.DebtsResponse;

public interface DebtsService {

    DebtsResponse getDebts(DebtsRequest payload);
}
