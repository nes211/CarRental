package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;


    public List<CarDTO> findAvailableCars (LocalDateTime startRent, LocalDateTime endRent, Float maxCostPerDay){

        // TODO: 19.03.2024
        return Arrays.asList();
    }

    public CarDTO findCarById(Integer carId){
        Optional<Car> carFromRepo = carRepository.findById(carId);
        return unwrapCar(carFromRepo);
    }

    private CarDTO unwrapCar(Optional<Car> carFromRepo) {
        if(carFromRepo.isEmpty()){
            throw new CarNotFoundException("Car not find in repo. Please select correct car");
        }
        return new CarDTO(carFromRepo.get());
    }

    private void addNewCar(Car car){
        carRepository.save(car);

    }


    private void removeCar(Integer carId){

        // TODO: 19.03.2024
    }



}
