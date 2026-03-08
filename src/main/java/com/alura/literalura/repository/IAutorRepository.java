package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);
    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllWithLibros();
    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.anioNacimiento <= :anio AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)")
    List<Autor> findAutoresVivosEnWithLibros(@Param("anio") int anio);
}