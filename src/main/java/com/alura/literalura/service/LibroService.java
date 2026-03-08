package com.alura.literalura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.IAutorRepository;
import com.alura.literalura.repository.ILibroRepository;

@Service
public class LibroService {

    @Autowired
    private ILibroRepository libroRepository;

    @Autowired
    private IAutorRepository autorRepository;

    public LibroDTO buscarEnBD(String titulo) {
        return libroRepository.findByTituloContainsIgnoreCase(titulo)
                .map(this::toLibroDTO)
                .orElse(null);
    }

    public LibroDTO guardarLibro(DatosLibro datosLibro) {
        if (datosLibro.primerAutor() == null) {
            return null;
        }

        var datosAutor = datosLibro.primerAutor();

        Autor autor = autorRepository.findAll().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(datosAutor.nombre()))
                .findFirst()
                .orElseGet(() -> autorRepository.save(
                        new Autor(datosAutor.nombre(),
                                  datosAutor.anioNacimiento(),
                                  datosAutor.anioFallecimiento())));

        Libro libro = new Libro(datosLibro.titulo(), datosLibro.primerIdioma(),
                                datosLibro.numeroDescargas(), autor);
        libroRepository.save(libro);

        return toLibroDTO(libro);
    }


    public List<LibroDTO> listarLibros() {
        return libroRepository.findAll().stream()
                .map(this::toLibroDTO)
                .collect(Collectors.toList());
    }


    public List<AutorDTO> listarAutores() {
        return autorRepository.findAll().stream()
                .map(this::toAutorDTO)
                .collect(Collectors.toList());
    }

    public List<AutorDTO> autoresVivosEn(int anio) {
        return autorRepository
                .findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(anio, anio)
                .stream()
                .map(this::toAutorDTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma).stream()
                .map(this::toLibroDTO)
                .collect(Collectors.toList());
    }

    private LibroDTO toLibroDTO(Libro libro) {
        String nombreAutor = libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido";
        return new LibroDTO(libro.getTitulo(), nombreAutor, libro.getIdioma(), libro.getNumeroDescargas());
    }

    private AutorDTO toAutorDTO(Autor autor) {
        List<String> titulos = autor.getLibros().stream()
                .map(Libro::getTitulo)
                .collect(Collectors.toList());
        return new AutorDTO(autor.getNombre(), autor.getAnioNacimiento(),
                            autor.getAnioFallecimiento(), titulos);
    }
}
