package com.southarmsite.backend;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Log
@EnableCaching
public class SoutharmStatsApplication {

	public static void main(String[] args) {

		SpringApplication.run(SoutharmStatsApplication.class, args);
	}

}
