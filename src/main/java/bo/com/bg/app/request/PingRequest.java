package bo.com.bg.app.request;

public record PingRequest(String traceId) {

    public PingRequest {
        traceId = traceId == null ? "" : traceId;
    }
}
