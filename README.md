# HireBeat Backend
HireBeat es una plataforma diseñada para conectar músicos profesionales con reclutadores y entusiastas de la música. Este repositorio contiene el backend robusto, escalable y modular desarrollado con **Kotlin** y **Ktor**.

## Tecnologías Principales
* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Framework:** [Ktor](https://ktor.io/) (Servidor asíncrono)
* **Base de Datos:** [PostgreSQL](https://www.postgresql.org/)
* **ORM / Query Builder:** [Exposed](https://github.com/JetBrains/Exposed)
* **Inyección de Dependencias:** [Koin](https://insert-koin.io/)
* **Seguridad:** JWT (JSON Web Tokens) & BCrypt para hashing de contraseñas.
* **Gestión de Media:** [Cloudinary](https://cloudinary.com/) para almacenamiento de imágenes.
* **Arquitectura:** Clean Architecture con enfoque modular.

## Arquitectura del Proyecto
El proyecto sigue los principios de **Clean Architecture** y **Arquitectura Hexagonal**, dividiendo cada módulo (User, Profile, GigRequests, etc.) en capas claras:
1. **Domain:** Contiene las entidades de negocio, modelos y definiciones de interfaces (repositorios). Es el corazón del sistema y es independiente de cualquier framework.
2. **Application:** Implementa los Casos de Uso (Use Cases). Orquesta el flujo de datos hacia y desde la capa de dominio.
3. **Infrastructure:** Detalles de implementación externa.
* **Persistence:** Repositorios implementados con Exposed y PostgreSQL.
* **Rest:** Definición de rutas (endpoints) y DTOs (Data Transfer Objects).
* **Security:** Manejo de tokens y encriptación.

## Estructura de Módulos

El código se organiza en módulos funcionales para facilitar el mantenimiento:

* `auth/`: Gestión de registro, login y seguridad.
* `users/`: Operaciones CRUD y administración de usuarios.
* `profile/`: Gestión de perfiles de músicos (instrumentos, géneros, links).
* `gig_requests/`: Gestión de solicitudes de servicios musicales y estados de contratos.
* `availability/`: Sistema de gestión de fechas y bloques de horarios.
* `reviews/`: Sistema de retroalimentación y valoraciones entre usuarios.
* `catalogs/`: Datos maestros (instrumentos, roles, géneros, niveles).

## Instalación y Configuración

### Prerrequisitos
* JDK 17 o superior.
* PostgreSQL instalado y en ejecución.
* Cuenta de Cloudinary (para la gestión de imágenes).

### Variables de Entorno
Crea un archivo de configuración o configura las siguientes variables en tu entorno para que `Application.kt` pueda conectarse a los servicios:

```env
# Database

DB_URL=jdbc:postgresql://localhost:5432/tu_bd
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña

# JWT
JWT_SECRET=tu_secreto_super_seguro
JWT_ISSUER= tu_issuer

# Cloudinary
CLOUDINARY_CLOUD_NAME= tu_nube
CLOUDINARY_API_KEY= tu_llave
CLOUDINARY_API_SECRET= tu_secreto
```

### Base de Datos
El proyecto incluye un DatabaseSeeder automático que inicializa los catálogos necesarios (Instrumentos, Roles, Géneros) al arrancar por primera vez, asegurando que el entorno esté listo para pruebas de inmediato.

# api

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                   | Description                                                                        |
| ------------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Call Logging](https://start.ktor.io/p/call-logging)                   | Logs client requests                                                               |
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Swagger](https://start.ktor.io/p/swagger)                             | Serves Swagger UI for your project                                                 |
| [Koin](https://start.ktor.io/p/koin)                                   | Provides dependency injection                                                      |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [Postgres](https://start.ktor.io/p/postgres)                           | Adds Postgres database to your application                                         |
| [Exposed](https://start.ktor.io/p/exposed)                             | Adds Exposed database to your application                                          |
| [CORS](https://start.ktor.io/p/cors)                                   | Enables Cross-Origin Resource Sharing (CORS)                                       |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                                    | Description                                                          |
| -----------------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`                        | Run the tests                                                        |
| `./gradlew build`                       | Build everything                                                     |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `./gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `./gradlew run`                         | Run the server                                                       |
| `./gradlew runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

