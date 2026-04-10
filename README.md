# 🍽️ Restaurante Pro - Backend System

> **Proyecto de Graduación** | Sistema integral para la automatización de reservaciones, gestión de mesas y control administrativo.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## 📌 Descripción del Proyecto
Este sistema fue desarrollado como mi **Trabajo Final de Graduación (2025)** para la Universidad Internacional San Isidro Labrador. El objetivo principal es digitalizar la gestión de reservaciones en restaurantes, eliminando los procesos manuales y centralizando la administración de mesas, clientes y menús en una plataforma web moderna y escalable.

---

## 🛠️ Especificaciones Técnicas

### Backend Architecture
* **Framework:** Spring Boot 3.x con Java 21.
* **Seguridad:** Implementación de **Spring Security** con control de acceso basado en roles (`ADMINISTRADOR`, `EMPLEADO`).
* **Autenticación:** Gestión de sesiones segura y cifrado de contraseñas con **BCrypt**.
* **Persistencia:** PostgreSQL como motor de base de datos relacional.
* **Documentación:** API diseñada bajo estándares RESTful.

### Frontend (Mencionado en Tesis)
* **Framework:** Angular (Interfaz administrativa intuitiva).

---

## 📂 Módulos del Sistema

* **📅 Gestión de Reservaciones:** Control total de fechas, horas y cantidad de personas, con validaciones para evitar sobrecupos.
* **🪑 Control de Mesas:** Módulo para administrar la disponibilidad y capacidad física del establecimiento.
* **👥 Administración de Usuarios:** Sistema de roles donde el Administrador gestiona el personal y reportes.
* **📜 Menú Digital:** CRUD completo para la gestión de platillos y categorías.
* **📊 Reportes:** Generación de información útil para la toma de decisiones gerenciales.

---

## 🏗️ Estructura de la Base de Datos

Basado en la arquitectura definida en mi tesis, el sistema utiliza el siguiente modelo relacional:

```mermaid
erDiagram
    USUARIO ||--o{ RESERVACION : gestiona
    MESA ||--o{ RESERVACION : asignada
    CLIENTE ||--o{ RESERVACION : realiza
    MENU ||--o{ CATEGORIA : pertenece
    
    USUARIO {
        string nombre
        string rol
        string password_hash
    }
    RESERVACION {
        datetime fecha_hora
        int cantidad_personas
        string estado
    }
    MESA {
        int numero
        int capacidad
    }
