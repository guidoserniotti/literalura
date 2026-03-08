# Literalura

Aplicación de consola en Java (Spring Boot) que consume una API pública de libros y persiste la información en una base de datos PostgreSQL. Permite buscar libros, almacenarlos y consultar libros/autores guardados.

## Tecnologías
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven

## Funcionalidades principales
- Buscar libros por título (consulta a API externa).
- Guardar libros y autores en la base de datos.
- Listar libros y autores almacenados.
- Filtrar autores vivos en un año determinado.
- Buscar libros por idioma.

## Requisitos
- Java 17+
- PostgreSQL en ejecución
- Maven (o usar `mvnw` / `mvnw.cmd`)

## Configuración
Editar `src/main/resources/application.properties` con tus credenciales y URL de PostgreSQL.

## Ejecución
```bash
./mvnw spring-boot:run
```

La aplicación inicia en modo consola y muestra un menú interactivo.
