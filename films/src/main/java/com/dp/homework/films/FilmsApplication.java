package com.dp.homework.films;

import com.dp.homework.films.model.Film;
import com.dp.homework.films.repo.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmsApplication  {

		@Autowired
		private FilmRepository filmRepository;


	public static void main(String[] args) {
		SpringApplication.run(FilmsApplication.class, args);


	}



}
