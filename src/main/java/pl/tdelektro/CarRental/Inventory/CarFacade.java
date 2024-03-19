package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;


    public List<CarDTO> findAvailableCars (LocalDateTime startRent, LocalDateTime endRent, Float maxCostPerDay){

        // TODO: 19.03.2024
        return Arrays.asList();
    }

    private void addNewCar(){
        // TODO: 19.03.2024
    }


    private void removeCar(Integer carId){

        // TODO: 19.03.2024
    }

}
