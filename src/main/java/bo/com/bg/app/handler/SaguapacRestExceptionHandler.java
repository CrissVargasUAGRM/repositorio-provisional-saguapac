package bo.com.bg.app.handler;

import bo.com.bg.commons.dto.Response;
import bo.com.bg.commons.exceptions.ProviderException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SaguapacRestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        if (ex.getMessage() != null && ex.getMessage().startsWith("406")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Response.<Void>builder()
                            .success(false)
                            .message(ex.getMessage().replaceFirst("^406:\\s*", ""))
                            .code("406")
                            .build());
        }
        return ResponseEntity.badRequest()
                .body(Response.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .code("400")
                        .build());
    }

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<Response<Void>> handleProviderException(ProviderException ex) {
        org.springframework.http.HttpStatus status =
                ex.getStatus() != null ? ex.getStatus() : org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
        return ResponseEntity.status(status)
                .body(Response.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .code(ex.getCodeError())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Datos de entrada invalidos");
        return ResponseEntity.badRequest()
                .body(Response.<Void>builder()
                        .success(false)
                        .message(msg)
                        .code("400")
                        .build());
    }
}
