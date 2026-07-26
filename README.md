# Mauro Sport Club Challenge

Challenge fullstack construido con React, Spring Boot y H2 para gestionar pedidos de clientes.

## Stack tecnológico

- Frontend: React + Vite
- Backend: Java 17 + Spring Boot
- Base de datos: H2 en memoria

## Estructura del proyecto

- `backend/`: API REST en Spring Boot
- `frontend/`: aplicación React

## Funcionalidades implementadas

- Página de listado de pedidos
- Página de detalle de pedido
- Cambio de estado de un pedido desde `PENDING` a `PAID` o `CANCELLED`
- Filtros por estado y rango de fechas
- Filtros reflejados en la URL del navegador mediante query params
- Carga automática de datos iniciales al iniciar la aplicación
- Manejo básico de errores y estados de UI (`loading`, `empty state`, errores de validación)

## Resumen del backend

El backend expone los siguientes endpoints:

- `GET /orders`
- `GET /orders/{id}`
- `POST /orders`
- `PATCH /orders/{id}/status`

### Filtros soportados

`GET /orders` soporta los siguientes query params opcionales:

- `status`
- `dateFrom`
- `dateTo`

Ejemplo:

```http
GET /orders?status=PENDING&dateFrom=2026-07-20&dateTo=2026-07-29
```

## Datos iniciales

La aplicación carga información inicial automáticamente usando `CommandLineRunner`.

Datos incluidos:

- 5 clientes
- 20 pedidos
- 60 items de pedido

## Cómo ejecutar el proyecto

### Prerrequisitos

- Java 17
- Node.js 18 o superior
- npm

### 1. Levantar el backend

Desde la raíz del proyecto:

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

### 2. Levantar el frontend

Abrir una segunda terminal desde la raíz del proyecto:

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

Valores de conexión por defecto:

- JDBC URL: `jdbc:h2:mem:sportclubdb`
- Usuario: `sa`
- Password: vacío

## Rutas del frontend

- `/orders`: listado de pedidos con filtros
- `/orders/:id`: detalle de pedido

## Decisiones técnicas

- **Uso de DTOs en el backend**
  - Se usan DTOs para no exponer las entidades directamente y para devolver respuestas enfocadas en lo que necesita el frontend.

- **Carga inicial con `CommandLineRunner`**
  - Esta opción mantiene la puesta en marcha simple y permite que quien evalúe el challenge vea datos disponibles inmediatamente.

- **Filtrado en el backend**
  - Los filtros se implementaron mediante query params en `GET /orders`, evitando que el frontend aplique la lógica solo en memoria.

- **Filtros reflejados en la URL**
  - El listado usa query params en la URL del navegador para que el estado de los filtros se mantenga al refrescar y también pueda compartirse.

- **Regla de negocio para actualización de pedidos**
  - Los pedidos en estado `PAID` o `CANCELLED` se consideran estados finales y no pueden modificarse nuevamente.

- **Uso de H2 para evaluación local**
  - Se eligió H2 porque cumple con el requerimiento del challenge y mantiene la configuración local rápida y liviana.

## Qué se priorizó

- Separación clara entre frontend y backend
- Diseño de API REST simple y entendible
- UI funcional con estados útiles para el usuario
- Estructura prolija y legible por sobre complejidad innecesaria

## Notas

- Paginación, Docker y tests quedaron como posibles mejoras siguientes.
- No se agregó autenticación ni una UI compleja porque no son requerimientos necesarios para este challenge.
