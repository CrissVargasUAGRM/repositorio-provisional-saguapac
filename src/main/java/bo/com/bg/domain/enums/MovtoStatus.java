package bo.com.bg.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovtoStatus {
    ACTIVO(1);

    private final int code;
}
