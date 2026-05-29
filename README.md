# Unit Testing Empresa API

Proyecto Spring Boot para practicar pruebas unitarias y analisis de calidad con Maven, JaCoCo, SonarCloud y Qodana.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- PostgreSQL en ejecucion local
- H2 para pruebas automatizadas
- JUnit 5 y Mockito
- JaCoCo
- SonarCloud
- Qodana

## Ejecutar pruebas

```bash
./mvnw verify
```

En Windows:

```powershell
.\mvnw.cmd verify
```

Las pruebas usan H2 en memoria, por lo que no requieren PostgreSQL.

## Analisis con SonarCloud

```powershell
.\mvnw.cmd verify sonar:sonar -Dsonar.token=TU_TOKEN
```

El token no debe guardarse en el repositorio. En GitHub Actions se debe configurar como secret con el nombre `SONAR_TOKEN`.

## Analisis con Qodana

El proyecto incluye `qodana.yaml`, por lo que puede analizarse desde IntelliJ IDEA o con Qodana en CI.

## Configuracion local

La aplicacion usa PostgreSQL al ejecutarse localmente. Ajusta `src/main/resources/application.properties` segun tu base de datos:

```properties
DB_URL=jdbc:postgresql://localhost:5432/empresa-db
DB_USERNAME=postgres
DB_PASSWORD=tu_password
```
