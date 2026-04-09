package bo.com.bg.commons.enums;

public enum ErrorMessage {
    AUTHENTICATION_ERROR("ERR1001", "Generar token - Error generando el token"),
    CRITERIA_SEARCH_ERROR("ERR1002", "Buscar criterios - Sin criterios encontrados"),
    VALIDATION_ERROR("ERR1001", "Parametros enviados invalidos"),
    ERROR_SERVICE_EXTERNO("ERR2000", "Error en el servicio externo"),
    ERROR_SERVICE_INVALIDO("ERR1003", "Error servicio no disponible");

    private final String code;
    private final String message;

    private ErrorMessage(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCod() { return code; }

    public String getMsg()
    {
        return message;
    }

}
