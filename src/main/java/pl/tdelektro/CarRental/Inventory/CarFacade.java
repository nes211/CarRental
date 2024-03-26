package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

@Service
@AllArgsConstructor
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;


    public Set<CarDTO> findAvailableCars (LocalDateTime startRent, LocalDateTime endRent, Float maxCostPerDay){
        Set<Car> carSet = carRepository.findByIsAvailableTrue();
        Set<CarDTO> carDtoSet = new HashSet<>();

        carSet.forEach(car -> {carDtoSet.add(new CarDTO(car));
        });
        return carDtoSet;
    }

    public CarDTO findCarById(Integer carId){
        Optional<Car> carFromRepo = carRepository.findById(carId);
        return unwrapCar(carFromRepo, carId);
    }

    public void saveCarChanges(Integer carId,String status){
        carRepository.findById(carId).get().setAvailable(status.contains("ACTIVE"));
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

