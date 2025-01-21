package com.aluracursos.challenge_literatura;

import org.springframework.boot.CommandLineRunner;
import principal.Principal;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {

	@Autowired
	private LibrosRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;


	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Principal Principal = new Principal(libroRepository, autorRepository);
		Principal.muestrarMenu();
	}
}
