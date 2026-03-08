package com.alura.literalura.principal;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.DatosRespuesta;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    @Autowired
    private LibroService libroService;

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ===== LITERALURA =====
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;

            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private DatosLibro getDatosLibro(String titulo) {
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "%20"));
        DatosRespuesta respuesta = conversor.obtenerDatos(json, DatosRespuesta.class);
        if (respuesta.resultados() == null || respuesta.resultados().isEmpty()) {
            return null;
        }
        return respuesta.resultados().get(0);
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el titulo del libro:");
        String titulo = teclado.nextLine();

        LibroDTO libroEnBD = libroService.buscarEnBD(titulo);
        if (libroEnBD != null) {
            System.out.println("El libro ya se encuentra registrado en la base de datos.");
            imprimirLibro(libroEnBD);
            return;
        }

        DatosLibro datos = getDatosLibro(titulo);
        if (datos == null) {
            System.out.println("Libro no encontrado.");
            return;
        }

        LibroDTO libro = libroService.guardarLibro(datos);
        if (libro != null) {
            imprimirLibro(libro);
        } else {
            System.out.println("No se pudo guardar el libro.");
        }
    }

    private void listarLibros() {
        List<LibroDTO> libros = libroService.listarLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        libros.forEach(this::imprimirLibro);
    }

    private void listarAutores() {
        List<AutorDTO> autores = libroService.listarAutores();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }
        autores.forEach(this::imprimirAutor);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año:");
        int anio = Integer.parseInt(teclado.nextLine());
        List<AutorDTO> autores = libroService.autoresVivosEn(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
            return;
        }
        autores.forEach(this::imprimirAutor);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma del libro:
                en - Inglés
                es - Español
                fr - Francés
                pt - Portugués
                it - Italiano
                de - Alemán
                """);
        String idioma = teclado.nextLine();
        List<LibroDTO> libros = libroService.buscarPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
            return;
        }
        libros.forEach(this::imprimirLibro);
    }

    private void imprimirLibro(LibroDTO libro) {
        System.out.println("""
                ----- LIBRO -----
                Titulo: %s
                Autor: %s
                Idioma: %s
                Descargas: %.1f
                ------------------
                """.formatted(
                libro.titulo(),
                libro.autor(),
                libro.idioma(),
                libro.descargas()
        ));
    }

    private void imprimirAutor(AutorDTO autor) {
        System.out.println("""
                ----- AUTOR -----
                Nombre: %s
                Fecha de Nacimiento: %s
                Fecha de Fallecimiento: %s
                Libros: %s
                ------------------
                """.formatted(
                autor.nombre(),
                autor.fechaNacimiento(),
                autor.fechaFallecimiento(),
                autor.libros()
        ));
    }
}