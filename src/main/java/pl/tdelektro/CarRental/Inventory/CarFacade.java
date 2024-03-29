package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;

    public Set<CarDTO> findAllCars() {
        Set<Car> carSet = carRepository.findByIsAvailableTrue();
        Set<CarDTO> carDtoSet = new HashSet<>();
        carSet.forEach(car -> {
            carDtoSet.add(new CarDTO(car));
        });
        return carDtoSet;
    }

    public CarDTO findCarById(Integer carId) {
        Optional<Car> carFromRepo = carRepository.findById(carId);
        if (carFromRepo == null) {
            throw new CarNotFoundException(carId);
        }
        return unwrapCarToCarDto(carFromRepo, carId);
    }

    public void saveCarStatus(Integer carId, String status) {

        boolean isAvailable = true;
        if (status.contains("ACTIVE")) {
            isAvailable = false;
        } else {
            isAvailable = true;
        }

        carRepository.findById(carId).get().setAvailable(isAvailable);
    }

    private void addNewCar(Car car) {
        carRepository.save(car);
    }


    private void removeCar(Integer carId) {
        carRepository.delete(unwrapCar(carId));
    }

    private Car unwrapCar(Integer carId) {
        if (carRepository.existsById(carId)) {
            return carRepository.findById(carId).get();
        } else {
            throw new CarNotFoundException(carId);
        }
    }

    private CarDTO unwrapCarToCarDto(Optional<Car> carFromRepo, Integer carId) {
        if (carFromRepo == null) {
            throw new CarNotFoundException(carId);
        }
        return new CarDTO(carFromRepo.get());
    }


}

