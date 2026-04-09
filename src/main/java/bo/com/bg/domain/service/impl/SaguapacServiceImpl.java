package bo.com.bg.domain.service.impl;

import bo.com.bg.app.response.PingResponse;
import bo.com.bg.commons.utils.Messages;
import bo.com.bg.domain.connector.Connector;
import bo.com.bg.domain.provider.Provider;
import bo.com.bg.domain.service.SaguapacService;
import bo.com.bg.domain.service.request.ServiceCommand;
import org.springframework.stereotype.Service;

@Service
public class SaguapacServiceImpl implements SaguapacService {

    private final Connector connector;
    private final Provider provider;

    public SaguapacServiceImpl(Connector connector, Provider provider) {
        this.connector = connector;
        this.provider = provider;
    }

    @Override
    public PingResponse ping() {
        var command = new ServiceCommand("PING");
        if (!provider.ready()) {
            return new PingResponse("DOWN", connector.resourceName() + ":" + command.action());
        }
        return new PingResponse("UP", Messages.pingDetail());
    }
}
