package bo.com.bg.domain.service.response;

public record ServiceOutcome(String code) {

    public ServiceOutcome {
        code = code == null ? "OK" : code;
    }
}
