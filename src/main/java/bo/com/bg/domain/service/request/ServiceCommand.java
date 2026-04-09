package bo.com.bg.domain.service.request;

public record ServiceCommand(String action) {

    public ServiceCommand {
        action = action == null ? "PING" : action;
    }
}
