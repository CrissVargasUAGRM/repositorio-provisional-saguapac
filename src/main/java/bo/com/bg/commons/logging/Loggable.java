package bo.com.bg.commons.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca clases cuyas invocaciones deben registrarse en log.
 * En entorno corporativo suele sustituirse por {@code @Loggable} de libreria-conector con AOP.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}
