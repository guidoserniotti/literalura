package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(

        @JsonProperty("title")
        String titulo,

        @JsonProperty("authors")
        List<DatosAutor> autores,

        @JsonProperty("languages")
        List<String> idiomas,

        @JsonProperty("download_count")
        Double numeroDescargas

) {

    public DatosAutor primerAutor() {
        if (autores != null && !autores.isEmpty()) {
            return autores.get(0);
        }
        return null;
    }

    public String primerIdioma() {
        if (idiomas != null && !idiomas.isEmpty()) {
            return idiomas.get(0);
        }
        return "desconocido";
    }
}