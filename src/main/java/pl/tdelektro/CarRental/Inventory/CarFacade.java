package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarFacade {

    private final CarRepository carRepository;

    public Set<CarDTO> findAvailableCars() {
        Set<Car> carSet = carRepository.findByIsAvailableTrue();
        Set<CarDTO> carDtoSet = new HashSet<>();
        carSet.forEach(car -> {
            carDtoSet.add(new CarDTO(car));
        });
        return carDtoSet;
    }

    public CarDTO findCarById(Integer carId) {
        Optional<Car> carFromRepo = carRepository.findById(carId);
        //Null check for test purpose
        if (carFromRepo.get() == null) {
            throw new CarNotFoundException(carId);
        }
        return unwrapCarToCarDto(carFromRepo, carId);
    }

    public void saveCarStatus(Integer carId, String status) {

        boolean isAvailable;
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


    public void removeCar(Integer carId) {
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
        if (carFromRepo.isEmpty()) {
            throw new CarNotFoundException(carId);
        }
        return new CarDTO(carFromRepo.get());
    }

    public Set<CarDTO> findAllCars() {
        Set<CarDTO> carDtoSet = new HashSet<>();
        carRepository.findAll().forEach(car -> {
            carDtoSet.add(new CarDTO(car));
        });
        return carDtoSet;
    }
}

