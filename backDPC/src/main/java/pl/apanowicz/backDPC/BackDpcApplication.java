package pl.apanowicz.backDPC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackDpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackDpcApplication.class, args);
	}

}
