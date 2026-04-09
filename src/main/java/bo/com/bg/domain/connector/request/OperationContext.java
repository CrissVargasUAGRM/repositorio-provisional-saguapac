package bo.com.bg.domain.connector.request;

public record OperationContext(String operationId) {

    public OperationContext {
        operationId = operationId == null ? "" : operationId;
    }
}
