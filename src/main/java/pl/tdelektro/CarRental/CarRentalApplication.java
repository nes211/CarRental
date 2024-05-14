package pl.tdelektro.CarRental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"pl.tdelektro.CarRental.Customer", "pl.tdelektro.CarRental", "pl.tdelektro.CarRental.test"})
@EntityScan("pl.tdelektro.CarRental")
	public class CarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}
}
