package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;


    public List<CarDTO> findAvailableCars (LocalDateTime startRent, LocalDateTime endRent, Float maxCostPerDay){
        return Arrays.asList();
    }

    public CarDTO findCarById(Integer carId){
        Optional<Car> carFromRepo = carRepository.findById(carId);
        return unwrapCar(carFromRepo, carId);
    }

    private CarDTO unwrapCar(Optional<Car> carFromRepo, int carId) {
        if(carFromRepo.isEmpty()){
            throw new CarNotFoundException(carId);
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

