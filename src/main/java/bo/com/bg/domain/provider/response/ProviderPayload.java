package bo.com.bg.domain.provider.response;

public record ProviderPayload(String body) {

    public ProviderPayload {
        body = body == null ? "" : body;
    }
}
