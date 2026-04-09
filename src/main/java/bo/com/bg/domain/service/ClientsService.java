package bo.com.bg.domain.service;

import bo.com.bg.app.rest.request.ClientRequest;
import bo.com.bg.app.rest.response.ClientResponseData;

public interface ClientsService {

    ClientResponseData getClient(ClientRequest payload);
}
