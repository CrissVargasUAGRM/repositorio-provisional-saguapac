# saguapac-connector

Proyecto **Spring Boot 4.0.4** + **Java 21** + **Maven**, generado según el skill de dependencias y estructura corporativa (`bo.com.bg`).

## Requisitos

- JDK 21
- Maven 3.9+
- Acceso al feed **Azure Artifacts** `integration-library` (para `libreria-conector`), o compilar sin ese perfil.

## Compilar y tests

Con librería interna (perfil `enterprise-library` activo por defecto):

```bash
mvn verify
```

Sin dependencia interna (por ejemplo sin credenciales Maven para Azure):

```bash
mvn verify "-P!enterprise-library"
```

En **PowerShell**, si `!` da problemas:

```powershell
mvn verify '-P!enterprise-library'
```

## Documentación

- [DOCUMENTACION_TECNICA.md](DOCUMENTACION_TECNICA.md)

## Estructura

Paquete base `bo.com.bg`: capas `app`, `commons`, `domain` (connector, provider, service), YAML en `src/main/resources`, metadatos CI en `cicd/env-data.yaml`.
