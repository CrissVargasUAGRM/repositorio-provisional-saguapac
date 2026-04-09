# Documentación técnica — saguapac-connector

## Resumen

API REST basada en **Spring Boot 4.0.4**, **Java 21** y **Maven**, alineada al estándar de conectores del Banco Ganadero (`bo.com.bg`).

## Coordenadas del proyecto

| Campo | Valor |
|--------|--------|
| `groupId` | `bo.com.bg` |
| `artifactId` | `saguapac-connector` |
| `version` | `1.0` |
| Paquete base | `bo.com.bg` (el skill menciona `bo.con.bg`; se usa `bo.com.bg` por coherencia con `groupId` y estructura de carpetas) |

## Capas

| Capa | Paquete | Rol |
|------|---------|-----|
| App | `bo.com.bg.app.*` | Controllers REST, configuración web, DTOs de entrada/salida de la API |
| Commons | `bo.com.bg.commons.*` | Enums y utilidades compartidas (fuera del análisis de cobertura según JaCoCo) |
| Dominio | `bo.com.bg.domain.*` | Conectores, providers, servicios y configuración de dominio |

## Patrones

- **Controller** → delega en **Service**.
- **Service** → orquesta **Provider** y **Connector** vía interfaces.
- Implementaciones concretas bajo `*.impl`.

## Configuración

- `application.yml`: aplicación, import opcional de Spring Cloud Config, Actuator.
- `log4j2.yml`: logging Log4j2 (sin Logback).

## Dependencia interna

La librería `bo.com.bg:libreria-conector` se declara en el perfil Maven `enterprise-library` (activo por defecto). Requiere acceso al feed Azure definido en `pom.xml` y credenciales en `~/.m2/settings.xml`.

Compilar **sin** esa dependencia (por ejemplo en un entorno sin VPN):

```bash
mvn verify -P "!enterprise-library"
```

(PowerShell puede requerir comillas distintas; ver README del repositorio.)

## CI/CD

Valores orientativos en `cicd/env-data.yaml`.

## Ajustes respecto al skill (declarados)

| Tema | Decisión |
|------|-----------|
| Paquete `bo.con.bg` del skill | Se usa `bo.com.bg` alineado al `groupId` y a la estructura de carpetas. |
| Carpeta `conector` vs `connector` | En código Java se usa `connector` (convención y coherencia con tests del skill). |
| JUnit `5.9.3` explícito | No se fija en el POM: Spring Boot 4 exige una versión de JUnit Platform compatible con `SpringExtension`; la versión la gestiona el BOM de Spring Boot. |
| Imágenes en el `.md` del skill | El contenido útil (POM, JaCoCo, repositorio) está replicado en `pom.xml`; las referencias `![](...)` rotas del skill no afectan al build. |
| JaCoCo e interfaces puras | Se excluyen del informe `Connector`, `Provider` y `SaguapacService` (solo métodos abstractos, sin bytecode cubrible) para cumplir la regla de bundle del skill sin falsos negativos. |
| `log4j2.yml` | Se añade `jackson-dataformat-yaml` para que Log4j2 active el factory YAML. |
| `jsoup` | Versión explícita en propiedad `jsoup.version` (no viene gestionada por el parent en este árbol de dependencias). |
