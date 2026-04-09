package bo.com.bg.domain.connector.impl;

import bo.com.bg.domain.connector.Connector;
import bo.com.bg.domain.connector.request.OperationContext;
import bo.com.bg.domain.connector.response.OperationResult;
import org.springframework.stereotype.Component;

@Component
public class DefaultConnector implements Connector {

    @Override
    public String resourceName() {
        return "default-connector";
    }

    public OperationResult execute(OperationContext context) {
        boolean accepted = context != null && !context.operationId().isBlank();
        return new OperationResult(accepted);
    }
}
