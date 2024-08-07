# Task Manager Backend

Este directorio contiene el backend de la aplicación Task Manager, desarrollado con Spring Boot.

## Requisitos

- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 17 o superior
- [Maven](https://maven.apache.org/) 3.6 o superior

## Configuración

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/pruebatecnicamiguelf/task-manager-backend.git
   cd task-manager-backend

   ```

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

   ```

3. **Compilar y ejecutar el backend**

   Compila y ejecuta la aplicación usando Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Documentación de la API

Swagger está configurado para la documentación de la API. Puedes acceder a ella en http://localhost:8080/swagger-ui/index.html.

### Configuración de Token Blacklist

El sistema de tokens de la aplicación soporta dos implementaciones para la blacklist de tokens: en memoria y en base de datos. Puedes configurar la implementación deseada en el archivo application.properties ubicado en src/main/resources.

Por defecto, el archivo de configuración application.properties incluye las siguientes propiedades:

```properties
   spring.application.name=task-manager
   spring.datasource.url=jdbc:h2:mem:task-manager-db
   spring.datasource.driver-class-name=org.h2.Driver
   spring.datasource.username=user
   spring.datasource.password=user123
   spring.h2.console.enabled=true
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=your-very-secret-key-which-should-be-long-enough
   jwt.expiration=3600000

   # Para usar token black-list en memoria
   #token.blacklist.implementation=memory

   # Para usar token black-list en base de datos
   token.blacklist.implementation=database
```

Para alternar entre las implementaciones, comenta o descomenta las líneas correspondientes:

Para usar la blacklist en memoria, descomenta #token.blacklist.implementation=memory.
Para usar la blacklist en base de datos, descomenta token.blacklist.implementation=database.

### Consola H2

La consola de la base de datos H2 está habilitada por defecto. Para acceder a la consola H2:

   1. Asegúrate de que la aplicación esté en ejecución.
   2. Abre tu navegador y ve a http://localhost:8080/h2-console.
   3. Usa la siguiente configuración para conectarte:
      - JDBC URL: jdbc:h2:mem:task-manager-db
      - User Name: user
      - Password: user123

### Notas

- **JWT:** La aplicación utiliza JWT para la autenticación.
- **CORS:** Asegúrate de que el CORS esté configurado correctamente si cambias el origen del frontend.
