package com.aluracursos.challenge_literatura.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Column(name = "idiomas")
    private String idiomas;
    private Double numeroDeDescargas;
    private String detallesLibro;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private List<Autor> autores = new ArrayList<>();

    public Libros(){}

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.detallesLibro = String.join(",",datosLibros.detallesLibro());
        this.idiomas = String.join(",", datosLibros.idiomas());
        this.numeroDeDescargas = datosLibros.numeroDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public String getDetallesLibro() {
        return detallesLibro;
    }

    public void setDetallesLibro(String detallesLibro) {
        this.detallesLibro = detallesLibro;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    @Override
    public String toString() {
        return """
               Libro: Titulo: %s | Autores: %s  |  Idioma: %s |  Descargas Totales: %f"""
                .formatted(titulo, autores, idiomas, numeroDeDescargas);
    }
}
