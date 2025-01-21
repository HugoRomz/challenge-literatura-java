package com.aluracursos.challenge_literatura.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaNacimiento;


    @OneToMany(fetch = FetchType.EAGER)
    private List<Libros> librosDelAutor = new ArrayList<>();

    public Autor() {
    }
    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<Libros> getLibrosDelAutor() {
        return librosDelAutor;
    }

    public void setLibrosDelAutor(List<Libros> librosDelAutor) {
        this.librosDelAutor = librosDelAutor;
    }
    @Override
    public String toString() {
        return """
                El Autor es: Nombre Autor: %s |  Fecha de Nacimiento: %s""".formatted(nombre, fechaNacimiento);
    }
}
