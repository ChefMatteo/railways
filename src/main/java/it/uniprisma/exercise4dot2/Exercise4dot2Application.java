package it.uniprisma.exercise4dot2;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Exercise4dot2Application {
	public static void main(String[] args) {
		SpringApplication.run(Exercise4dot2Application.class, args);
	}


	@Bean
	public static Gson gson() {
		return new Gson();
	}

}
