# Task Manager Backend

Este directorio contiene el backend de la aplicación Task Manager, desarrollado con Spring Boot.

## Requisitos

- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 17 o superior
- [Maven](https://maven.apache.org/) 3.6 o superior

## Configuración

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/pruebatecnicamiguelf/task-manager-backend.git
   cd task-manager/backend

2. **Configurar la base de datos**

   La aplicación está configurada para utilizar una base de datos en memoria H2 por defecto. Si deseas utilizar otra base de datos, edita el archivo src/main/resources/application.properties.

   ```properties
   # Configuración predeterminada para H2
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console

3. **Compilar y ejecutar el backend**

   Compila y ejecuta la aplicación usando Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run


### Documentación de la API

Swagger está configurado para la documentación de la API. Puedes acceder a ella en http://localhost:8080/swagger-ui/index.html.

### Notas

   - **JWT:** La aplicación utiliza JWT para la autenticación.
   - **CORS:** Asegúrate de que el CORS esté configurado correctamente si cambias el origen del frontend.

