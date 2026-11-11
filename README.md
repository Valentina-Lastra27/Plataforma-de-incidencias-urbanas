# Plataforma-de-Reporte-de-Incidencias-Urbanas 

> **Alumnos:** *Lastra, Valentina y Medina, Lautaro*

# 📌 Descripción del Proyecto

Como proyecto final para la materia "Backend" este grupo propuso la idea de una **Plataforma de Reporte de Incidencias Urbanas** es un sistema distribuido basado en una arquitectura de **Microservicios** desarrollada con **Java 25** y **Spring Boot** ademas para cada entidad carasteristica de este sistema se usó **PostgreSQL** para almacenar datos de cada entidad. Su objetivo principal es gestionar de forma centralizada, trazable y automatizada los reportes de problemas en la vía pública (baches, luminarias, basura, etc.), vinculando a los ciudadanos, las zonas geográficas y las cuadrillas operativas mediante un motor de asignación inteligente.

El sistema implementa el patrón **Database per Service** (una base de datos independiente por cada microservicio) y utiliza **Apache Kafka** como broker de eventos para garantizar una comunicación asíncrona, escalable y desacoplada entre los distintos dominios de la aplicación.

---
# 📌 Sprints review
29/10: presentamos este readme con el objetivo de avanzar en el codigo, para la siguiente sprint que será el siguiente jueves 

5/11: Codigo ya esquematizado con las entidades ya creadas con sus respectivas caracteristicas y sus DTOs correspondientes, faltan pruebas de Junit y Conexion con Postman 

PROXIMAMENTE 12/11: Proyecto deployado con su documentacion correspondiente y el backtesting mejor realizado

#  Arquitectura del Sistema

A continuación se presenta el diagrama de la arquitectura, detallando el flujo desde el API Gateway hasta las bases de datos, los atributos principales de cada microservicio y la integración de eventos mediante Kafka:

```mermaid
flowchart LR
 subgraph Microservicios["ARQUITECTURA DE MICROSERVICIOS<br>s6"]
    direction TB
     M1["<b>1. MS Ciudadanos</b><br/>────────────<br/>id<br/>nombre<br/>email<br/>teléfono<br/>dni<br/>fecha_registro"]
    M2["<b> MS Incidencias</b><br/>────────────<br/>id<br/>título<br/>descripción<br/>tipo_incidencia<br/>foto<br/>prioridad<br/>estado_actual<br/>id_ciudadano<br/>id_zona<br/>id_cuadrilla<br/>fecha_creación<br/>fecha_cierre"]
    M3["<b>3. MS Asignaciones</b><br/>────────────<br/>id<br/>nombre<br/>especialidad<br/>id_zona<br/>estado<br/>reportes_activos"]
    M4["<b>4. MS Seguimiento</b><br/>────────────<br/>id<br/>id_reporte<br/>estado_anterior<br/>estado_nuevo<br/>fecha_cambio<br/>observaciones<br/>usuario_responsable<br/>foto_evidencia"]
    M5["<b>5. MS Zona</b><br/>────────────<br/>id<br/>nombre<br/>código_postal<br/>comuna<br/>límites_geográficos"]
  end
 subgraph Eventos["INTEGRACIÓN DE EVENTOS"]
    direction TB
        K["⚙️ KAFKA<br>Integracion de microservicios"]
  end
 subgraph BasesDatos["COMUNICACIÓN ENTRE BDs (PostgreSQL)"]
    direction TB
        D_USR[("<b>db_usuarios</b><br/>────────────<br/><b>Ciudadano</b><br/>id<br/>nombre<br/>email<br/>teléfono<br/>dni<br/>fecha_registro")]
        D_INC[("<b>db_incidencias</b><br/>────────────<br/><b>Reporte</b><br/>id<br/>título<br/>descripción<br/>tipo_incidencia<br/>foto<br/>prioridad<br/>estado_actual<br/>id_ciudadano<br/>id_zona<br/>id_cuadrilla<br/>fecha_creación<br/>fecha_cierre")]
        D_ASI[("<b>db_asignaciones</b><br/>────────────<br/><b>Cuadrilla</b><br/>id<br/>nombre<br/>especialidad<br/>id_zona<br/>estado<br/>reportes_activos")]
        D_SEG[("<b>db_seguimiento</b><br/>────────────<br/><b>Historial de Seguimiento</b><br/>id<br/>id_reporte<br/>estado_anterior<br/>estado_nuevo<br/>fecha_cambio<br/>observaciones<br/>usuario_responsable<br/>foto_evidencia")]
        D_ZONA[("<b>db_zonas</b><br/>────────────<br/><b>Zona</b><br/>id<br/>nombre<br/>código_postal<br/>comuna<br/>límites_geográficos")]
    end
    GW["<b>🛡️ API GATEWAY</b><br/>────────────<br/>Puerto: 8080<br/>Rol: Punto de Entrada Único<br/>────────────<br/>• Enrutamiento Dinámico<br/>• Seguridad (JWT)<br/>• Balanceo de Carga<br/>• Rate Limiting<br/>"] -- "sync-service" --> M1 & M2 & M3 & M4 & M5
    M1 -. "async-event" .-> K
    M2 -. "async-event" .-> K
    M3 -. "async-event" .-> K
    M4 -. "async-event" .-> K
    M5 -. "async-event" .-> K
    M1 --> D_USR
    M2 --> D_INC
    M3 --> D_ASI
    M4 --> D_SEG
    M5 --> D_ZONA

    
   classDef default fill:#fff,stroke:#333,stroke-width:2px;

## Cómo usar la plataforma

### Requisitos

- Java 25
- Maven (o el Maven integrado de IntelliJ)
- Docker Desktop

### 1. Levantar la infraestructura

En la raíz del proyecto:

```bash
docker compose up -d
```

Esto crea cinco bases PostgreSQL (una por microservicio) y Kafka. Compruébalo con `docker ps`.

| Base | Puerto en tu PC | Usuario / contraseña |
|---|---|---|
| `db_usuarios` | 5433 | postgres / postgres |
| `db_incidencias` | 5434 | postgres / postgres |
| `db_asignaciones` | 5435 | postgres / postgres |
| `db_seguimiento` | 5436 | postgres / postgres |
| `db_zonas` | 5437 | postgres / postgres |

### 2. Compilar el proyecto

La primera vez hay que instalar el módulo `common`, del que dependen todos los servicios:

```bash
mvn clean install -DskipTests
```

En IntelliJ: pestaña **Maven** → proyecto raíz → **Lifecycle** → **install**.

### 3. Ejecutar un microservicio

Ejecuta la clase `...Application` de cada servicio desde IntelliJ (botón verde de play), o con Maven:

```bash
mvn -pl ms-ciudadanos spring-boot:run
```

| Servicio | Puerto | Swagger UI |
|---|---|---|
| `ms-ciudadanos` | 8081 | http://localhost:8081/swagger-ui.html |
| `ms-incidencias` | 8082 | http://localhost:8082/swagger-ui.html |
| `ms-asignaciones` | 8083 | http://localhost:8083/swagger-ui.html |
| `ms-seguimiento` | 8084 | http://localhost:8084/swagger-ui.html |
| `ms-zonas` | 8085 | http://localhost:8085/swagger-ui.html |
| `api-gateway` | 8080 | punto de entrada único: `http://localhost:8080/api/...` |

> **Si usas Windows en Argentina** y ves el error `invalid value for parameter "TimeZone"`, agrega la VM option `-Duser.timezone=America/Argentina/Buenos_Aires` a la configuración de ejecución de cada servicio.

### 4. Probar los endpoints con Swagger

1. Abre la URL de Swagger del servicio.
2. Despliega un endpoint, por ejemplo `POST /api/ciudadanos`.
3. Pulsa **Try it out**, edita el JSON y pulsa **Execute**.
4. Revisa el código de respuesta y el cuerpo devuelto.

También se puede usar Postman o curl contra `http://localhost:808X/api/...` o a través del gateway en el puerto 8080.

### 5. Endpoints principales

| Servicio | Ruta base | Operaciones |
|---|---|---|
| Ciudadanos | `/api/ciudadanos` | `POST`, `GET`, `GET /{id}`, `GET /dni/{dni}`, `PUT /{id}`, `DELETE /{id}` |
| Zonas | `/api/zonas` | `POST`, `GET` (filtro opcional `?codigoPostal=`), `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| Cuadrillas | `/api/cuadrillas` | `POST`, `GET` (filtros `?idZona=&estado=`), `GET /{id}`, `PUT /{id}`, `DELETE /{id}`, `GET /asignacion-sugerida?idZona=&especialidad=`, `POST /{id}/asignar-reporte`, `POST /{id}/liberar-reporte` |
| Reportes | `/api/reportes` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `PATCH /{id}/estado`, `PUT /{id}/cuadrilla`, `DELETE /{id}` |
| Seguimiento | `/api/seguimientos` | `POST`, `GET`, `GET /{id}`, `GET /reporte/{idReporte}` |

### 6. Ejemplo de flujo completo

Los ids de los pasos siguientes son los que devuelve cada respuesta (en un sistema vacío, `1`).

**a) Crear una zona** (`POST /api/zonas`)

```json
{
  "nombre": "Centro",
  "codigoPostal": "1000",
  "comuna": "Comuna 1",
  "limitesGeograficos": "Av. Corrientes, Av. 9 de Julio"
}
```

**b) Crear un ciudadano** (`POST /api/ciudadanos`)

```json
{
  "nombre": "Lautaro Medina",
  "email": "lautaro@mail.com",
  "telefono": "1122334455",
  "dni": "40123456"
}
```

El DNI debe tener 7 u 8 dígitos y el email debe ser válido, si no la API responde `400`.

**c) Crear una cuadrilla** (`POST /api/cuadrillas`)

```json
{
  "nombre": "Cuadrilla Bacheo Centro",
  "especialidad": "BACHEO",
  "idZona": 1
}
```

Especialidades: `BACHEO`, `ALUMBRADO`, `LIMPIEZA`, `ARBOLADO`, `SEMAFORIZACION`. El estado inicial es `DISPONIBLE`.

**d) Crear un reporte** (`POST /api/reportes`)

```json
{
  "titulo": "Bache en Av. Corrientes 1234",
  "descripcion": "Bache profundo que afecta el carril derecho",
  "tipoIncidencia": "BACHE",
  "prioridad": "ALTA",
  "idCiudadano": 1,
  "idZona": 1
}
```

Tipos: `BACHE`, `LUMINARIA`, `BASURA`, `ARBOLADO`, `SEMAFORO`, `AGUA_CLOACAS`, `OTRO`. Prioridades: `BAJA`, `MEDIA` (por defecto), `ALTA`, `CRITICA`. Nace en estado `CREADO`.

**e) Pedir una cuadrilla sugerida** (`GET /api/cuadrillas/asignacion-sugerida?idZona=1&especialidad=BACHEO`)

Devuelve la cuadrilla disponible de esa zona, con preferencia por la especialidad exacta y, en caso de empate, la de menor carga.

**f) Asignar la cuadrilla al reporte** (`PUT /api/reportes/1/cuadrilla`)

```json
{
  "idCuadrilla": 1,
  "usuarioResponsable": "operador1"
}
```

**g) Cambiar el estado del reporte** (`PATCH /api/reportes/1/estado`)

```json
{
  "nuevoEstado": "EN_PROCESO",
  "observaciones": "La cuadrilla llegó al lugar",
  "usuarioResponsable": "operador1"
}
```

**h) Consultar el historial** (`GET /api/seguimientos/reporte/1`)

Lista los cambios de estado registrados para ese reporte. Si Kafka está levantado, `ms-seguimiento` los registra solo; también se puede crear un registro a mano con `POST /api/seguimientos`.

### 7. Estados del reporte

`CREADO → ASIGNADO → EN_PROCESO → RESUELTO → CERRADO`, con `RECHAZADO` y reapertura controlada. Una transición no permitida (por ejemplo de `CREADO` a `CERRADO`) devuelve `400`.

### 8. Ver los datos en la base

Desde la terminal:

```bash
docker exec -it db_usuarios psql -U postgres -d db_usuarios -c "SELECT * FROM ciudadanos;"
```

O conecta el panel **Database** de IntelliJ (o DBeaver) a `localhost` con el puerto, la base y las credenciales de la tabla del paso 1.

### 9. Ejecutar los tests

```bash
mvn clean test              # todos los módulos
mvn -pl ms-ciudadanos test  # un solo servicio
```

En IntelliJ: clic derecho sobre `src/test` del módulo → **Run 'Tests in ...'**. No necesitan Docker ni bases de datos (usan JUnit 5, Mockito y MockMvc).

### 10. Respuestas de error

| Código | Cuándo ocurre |
|---|---|
| `400` | Datos inválidos (validaciones) o transición de estado no permitida |
| `404` | El recurso con ese id no existe |
| `409` | Conflicto, por ejemplo un DNI o email duplicado |

### 11. Problemas frecuentes

| Síntoma | Causa y solución |
|---|---|
| `Connection to localhost:5433 refused` | Docker Desktop cerrado o contenedores detenidos. Ejecuta `docker compose up -d`. |
| `Could not find artifact ...:common:jar` | Falta compilar el módulo común. Ejecuta `mvn clean install -DskipTests`. |
| `invalid value for parameter "TimeZone"` | Agrega la VM option `-Duser.timezone=America/Argentina/Buenos_Aires`. |
| `Port 808X was already in use` | Ya hay una copia del servicio corriendo. Detenla y vuelve a ejecutar. |
| `mvn` no se reconoce | Usa el Maven integrado de IntelliJ (pestaña **Maven**) o instala Maven y agrégalo al `Path`. |
