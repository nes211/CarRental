package pl.tdelektro.CarRental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CarRentalApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}
}
