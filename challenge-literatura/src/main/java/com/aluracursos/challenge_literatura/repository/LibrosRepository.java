package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libros, Long> {


    List<Libros> findTop10ByOrderByNumeroDeDescargasDesc();
    List<Libros> findAll();

    @Query("SELECT l FROM Libros l WHERE l.idiomas ILIKE %:idioma%")
    List<Libros> findByIdioma(String idioma);

}
