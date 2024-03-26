package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component("carWarmup")
@AllArgsConstructor
class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    CarRepository carRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (carRepository.findByIdNotNull().isEmpty()) {


            carRepository.save(new Car.CarBuilder()
                    .make("Mercedes")
                    .model("W211T")
                    .modelYear(2005)
                    .oneDayCost(300f)
                    .type("D")
                    .isAvailable(true)
                    .build());

            carRepository.save(new Car.CarBuilder()
                    .make("Polonez")
                    .model("1500 GLE")
                    .modelYear(1990)
                    .oneDayCost(500f)
                    .type("D")
                    .isAvailable(true)
                    .build());
        }
    }
}
