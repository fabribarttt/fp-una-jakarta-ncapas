# Sistema CRUD de Registro de Productos (Arquitectura N-Capas)
> **Práctica de laboratorio** — Facultad Politécnica, Universidad Nacional de Asunción (FP-UNA)

El presente proyecto consiste en el desarrollo de una aplicación web para la gestión y registro de productos (operaciones CRUD: Crear, Leer, Actualizar y Eliminar), diseñada bajo un modelo de arquitectura cliente-servidor en **N-capas**. Esta estructura garantiza una clara separación de responsabilidades, facilitando la mantenibilidad, escalabilidad y pruebas independientes de cada componente.

---

## Stack Tecnológico

* **Presentación:** Thymeleaf, HTMX, Bootstrap
* **Lógica de Aplicación:** Jakarta EE
* **Acceso a Datos:** Hibernate / JPA
* **Base de Datos:** PostgreSQL
* **Despliegue/Entorno:** Docker & Docker Compose

---

## Justificación Arquitectónica

Siguiendo las pautas del modelo N-capas, esta distribución garantiza que cada capa cumpla un rol delimitado dentro del ciclo de procesamiento:

* **Presentación (Thymeleaf):** Encargada exclusivamente de gestionar la interfaz de usuario (UI) y la renderización de vistas.
* **Lógica de Aplicación (Jakarta EE):** Procesa la lógica de negocio, reglas de validación y control de flujo.
* **Acceso a Datos (Hibernate / JPA):** Abstrae y gestiona la comunicación objeto-relacional (ORM) entre Java y la base de datos.
* **Base de Datos (PostgreSQL):** Garantiza la persistencia física, la integridad relacional y la consistencia de la información.

Esta modularidad permite que modificaciones en la interfaz web o cambios en el motor de base de datos no impacten directamente en la lógica central del negocio, logrando un software robusto, mantenible y alineado con los estándares del diseño cliente-servidor.

---

## Guía de Uso con Docker

### Comandos Principales

```bash
# Iniciar la aplicación por primera vez o recompilar cambios en el código
docker compose up --build

# Detener los contenedores temporalmente (sin eliminarlos)
docker compose stop

# Iniciar nuevamente los contenedores detenidos (sin recompilar)
docker compose startss