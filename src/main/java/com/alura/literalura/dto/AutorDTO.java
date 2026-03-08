package com.alura.literalura.dto;

import java.util.List;

public record AutorDTO(
        String nombre,
        Integer fechaNacimiento,
        Integer fechaFallecimiento,
        List<String> libros
) {
}