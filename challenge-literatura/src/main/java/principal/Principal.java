package principal;

import com.aluracursos.challenge_literatura.model.Autor;
import com.aluracursos.challenge_literatura.model.Datos;
import com.aluracursos.challenge_literatura.model.DatosLibros;
import com.aluracursos.challenge_literatura.model.Libros;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.repository.LibrosRepository;
import com.aluracursos.challenge_literatura.service.ConsumoApi;
import com.aluracursos.challenge_literatura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibrosRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libros> libros;
    private List<Autor> autores;

    public Principal(LibrosRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestrarMenu(){

        var  opcion = -1;

        while (opcion != 0){
            var menu = """
                    1.- Ingresar un Libro
                    2.- Consultar libros
                    3.- Consultar autores
                    4.- Top libros 
                    5.- Características de libros
                    6.- Libros por idioma
                    7.- Listar autores vivos en un año
                    0.- Salir""";

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    incresarLibro();
                    break;
                case 2:
                    motrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    topDescargas();
                    break;
                case 5:
                    caracteristicaslibros();
                    break;
                case 6:
                    LibrosPorIdioma();
                    break;

                case 7:
                    listarAutoresVivos();
                    break;

                case 0:
                    System.out.println("Cerrando...");

                default:
                    System.out.println("Opcion invalida");

            }

        }
    }

    private void incresarLibro() {
        DatosLibros datos = getDatosLibros();

        if (datos == null) {
            System.out.println("NO SE ENCONTRO EL LIBRO");
            return;
        }
        List<Autor> autores = datos.autor().stream()
                .map(datosAutor -> autorRepository.findByNombre(datosAutor.nombre())
                        .orElseGet(() -> {
                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombre(datosAutor.nombre());
                            nuevoAutor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            autorRepository.save(nuevoAutor);
                            return nuevoAutor;
                        })
                ).collect(Collectors.toList());
        Libros libro = new Libros(datos);
        libro.setAutores(autores);
        libroRepository.save(libro);
        System.out.println(": " + datos);
    }

    private void motrarLibrosBuscados() {
        List<Libros> libros = libroRepository.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libros::toString))
                .forEach(System.out::println);
    }

    private void mostrarAutoresBuscados() {
        List<Autor> autores = autorRepository.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::toString))
                .forEach(System.out::println);
    }

    private void topDescargas() {
        List<Libros> toplibros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();
        toplibros.forEach(s->
                System.out.println("""
                        TOP 5 DESCARGADOS:
                        
                        LIBRO: %s
                        TOTAL: %s
                        
                        """.formatted(s.getTitulo(), s.getNumeroDeDescargas())));

    }

    private void caracteristicaslibros() {
        List<Libros> caracateristicas = libroRepository.findAll();
        caracateristicas.forEach(s->
                System.out.println("""
                        LIBRO:  %s
                        CARACTERISTICAS: %s

                       """.formatted(s.getTitulo(),s.getDetallesLibro())));
    }


    private void LibrosPorIdioma() {
        System.out.print("Ingresa el idioma ('en', 'es', 'fr' etc): ");
        String idioma = teclado.nextLine().trim();

        List<Libros> libros = libroRepository.findByIdioma(idioma);
        libros.stream().forEach(l -> {
            System.out.println("""    
                            Titulo: %s
                            Autor: %s
                            Idioma: %s
                            Descargas: %s
                        """.formatted(l.getTitulo(),
                    l.getAutores(),
                    l.getIdiomas(),
                    l.getNumeroDeDescargas().toString()));
        });
    }

    private void listarAutoresVivos() {
        System.out.print("Ingresa el año: ");

        String input = teclado.nextLine();
        int anio;
        try {
            anio = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido de año. Saliendo...");
            return;
        }

        List<Autor> vivos = autorRepository.autorPorFecha(anio);

        if (vivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("Autores vivos en " + anio + ":");
            vivos.forEach(System.out::println);
        }
        System.out.println();
    }

    private DatosLibros getDatosLibros() {
        System.out.print("Escribe tu libro Favorito | ");
        var tituloIngresado = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloIngresado.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        return datosBusqueda.listaLibros().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(tituloIngresado.toUpperCase()))
                .findFirst()
                .orElse(null);
    }


}
