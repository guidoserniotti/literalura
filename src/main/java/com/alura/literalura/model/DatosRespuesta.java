package com.alura.literalura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuesta(

        @JsonProperty("count")
        Integer cantidad,

        @JsonProperty("results")
        List<DatosLibro> resultados

) {
}
