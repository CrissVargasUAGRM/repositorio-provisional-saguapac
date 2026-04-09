package bo.com.bg.app.response;

public record PingResponse(String status, String detail) {

    public PingResponse {
        status = status == null ? "UNKNOWN" : status;
        detail = detail == null ? "" : detail;
    }
}
