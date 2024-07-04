package dev.pasq.deal_track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class DealTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealTrackApplication.class, args);
	}

}
