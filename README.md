# PRACTICA-N-2

Proyecto: Sistema de Registro Universitario

### Funcionalidades desarrolladas

1. **Autenticación y Autorización**

   * Registro y login de usuarios con roles (`ADMIN`, `DOCENTE`, `ESTUDIANTE`).
   * Uso de **JWT** para proteger los endpoints privados.
   * Control de acceso basado en roles.

2. **Gestión de Estudiantes**

   * CRUD completo de estudiantes con validaciones (`@NotNull`, `@Email`, etc.).

3. **Gestión de Materias**

   * CRUD completo de materias.
   * Asignación de materias a docentes.
   * Validaciones en los campos.

4. **Registro de Inscripciones**

   * CRUD para que los estudiantes se inscriban a materias.
   * Validaciones para evitar inscripciones duplicadas o inconsistentes.


### Tecnologías utilizadas

* **Backend:** Spring Boot
* **Base de datos:** PostgreSQL
* **Seguridad:** Spring Security + JWT
* **Documentación:** Swagger
* **Cache:** Redis
* **Validaciones:** Bean Validation (`@Valid`, `@NotBlank`, `@Email`, etc.)
* **Manejo de errores:** `@RestControllerAdvice`
* **Documentación adicional:** Markdown, capturas de Swagger, ejemplos de uso.



###  Estructura del proyecto


src/
├── controller/
├── service/
├── repository/
├── model/
├── dto/
├── validation/
├── config/
└── registro/ (lógica principal del sistema)

### Configuración y conexión

* Se configuró la conexión a **PostgreSQL** y **Redis** en `application.properties`.
* Todos los endpoints protegidos requieren token JWT para acceder.
* Swagger disponible en:
  `http://localhost:8080/swagger-ui/index.html`

###  Verificación de funcionalidades

* Se realizaron pruebas de todos los endpoints mediante Swagger.
* Se capturaron las respuestas y se documentaron con breves explicaciones de los resultados.
* La base de datos fue poblada con datos de ejemplo para pruebas funcionales.

###  Documentación entregada

* Manual técnico: estructura del sistema, endpoints, configuración, roles.
* Capturas de pantalla de pruebas en Swagger.
* Datos de ejemplo cargados en la base de datos.
