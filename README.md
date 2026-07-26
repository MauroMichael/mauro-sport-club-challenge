# Mauro Sport Club Challenge

Challenge fullstack construido con React, Spring Boot y H2 para gestionar pedidos de clientes.

## Stack tecnologico

- Frontend: React + Vite
- Backend: Java 17 + Spring Boot
- Base de datos: H2 en memoria
- Contenedores: Docker + Docker Compose

## Estructura del proyecto

- `backend/`: API REST en Spring Boot
- `frontend/`: aplicacion React

## Funcionalidades implementadas

- Listado de pedidos
- Detalle de pedido
- Cambio de estado de un pedido desde `PENDING` a `PAID` o `CANCELLED`
- Filtros por estado y rango de fechas
- Filtros reflejados en la URL mediante query params
- Paginacion en backend y frontend
- Carga automatica de datos iniciales al iniciar la aplicacion
- Manejo basico de errores y estados de UI (`loading`, `empty state`, validaciones)
- Ejecucion local con Docker Compose

## Resumen del backend

El backend expone los siguientes endpoints:

- `GET /orders`
- `GET /orders/{id}`
- `POST /orders`
- `PATCH /orders/{id}/status`

### Query params soportados en `GET /orders`

- `status`
- `dateFrom`
- `dateTo`
- `page`
- `size`

Ejemplo:

```http
GET /orders?status=PENDING&dateFrom=2026-07-20&dateTo=2026-07-29&page=0&size=10
```

### Respuesta paginada

`GET /orders` devuelve una estructura con contenido y metadata de paginacion:

```json
{
  "content": [
    {
      "id": 1,
      "date": "2026-07-26",
      "status": "PENDING",
      "total": 74.00,
      "customer": "Juan Perez"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 20,
  "totalPages": 2,
  "last": false
}
```

## Datos iniciales

La aplicacion carga informacion inicial automaticamente usando `CommandLineRunner`.

Datos incluidos:

- 5 clientes
- 20 pedidos
- 60 items de pedido

## Como ejecutar el proyecto

### Opcion 1. Docker Compose

Desde la raiz del proyecto:

```bash
docker compose up --build
```

Servicios disponibles:

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`

Para detenerlos:

```bash
docker compose down
```

### Opcion 2. Ejecucion manual

#### Prerrequisitos

- Java 17
- Node.js 18 o superior
- npm

#### 1. Levantar el backend

Desde la raiz del proyecto:

```bash
cd backend
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

El backend corre en:

```text
http://localhost:8080
```

#### 2. Levantar el frontend

Abrir una segunda terminal desde la raiz del proyecto:

```bash
cd frontend
npm install
npm run dev
```

El frontend corre en:

```text
http://localhost:5173
```

## Base de datos H2

El proyecto usa una base de datos H2 en memoria.

Consola H2:

```text
http://localhost:8080/h2-console
```

Valores de conexion por defecto:

- JDBC URL: `jdbc:h2:mem:sportclubdb`
- Usuario: `sa`
- Password: vacio

## Rutas del frontend

- `/orders`: listado de pedidos con filtros y paginacion
- `/orders/:id`: detalle de pedido

## Decisiones tecnicas

- **Uso de DTOs en el backend**
  - Se usan DTOs para no exponer las entidades directamente y para devolver respuestas enfocadas en lo que necesita el frontend.

- **Carga inicial con `CommandLineRunner`**
  - Esta opcion mantiene la puesta en marcha simple y permite que quien evalua el challenge vea datos disponibles inmediatamente.

- **Filtrado en el backend**
  - Los filtros se implementaron mediante query params en `GET /orders`, evitando que el frontend aplique la logica solo en memoria.

- **Paginacion en backend**
  - La paginacion se resolvio con `Pageable` para evitar devolver toda la coleccion y para mantener un contrato de API mas escalable.

- **Filtros y paginacion reflejados en la URL**
  - El listado usa query params en la URL para que el estado se mantenga al refrescar y tambien pueda compartirse.

- **Regla de negocio para actualizacion de pedidos**
  - Los pedidos en estado `PAID` o `CANCELLED` se consideran estados finales y no pueden modificarse nuevamente.

- **Uso de H2 para evaluacion local**
  - Se eligio H2 porque cumple con el requerimiento del challenge y mantiene la configuracion local rapida y liviana.

## Que se priorizo

- Separacion clara entre frontend y backend
- Diseno de API REST simple y entendible
- UI funcional con estados utiles para el usuario
- Estructura prolija y legible por sobre complejidad innecesaria

## Notas

- No se agrego autenticacion ni una UI compleja porque no son requerimientos necesarios para este challenge.
- Los tests automatizados quedan como una mejora posible siguiente.
