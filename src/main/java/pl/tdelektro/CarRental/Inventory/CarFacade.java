package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarFacade {

    private final CarRepository carRepository;

    public boolean addNewCar(Car car) {
        carRepository.save(car);
        return true;
    }

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
        if (carFromRepo.isEmpty()) {
            throw new CarNotFoundException(carId);
        }
        return unwrapCarToCarDto(carFromRepo, carId);
    }

    //Management status Active means that car is in use so isAvailable is set to false
    public void saveCarStatus(Integer carId, String managementStatus) {

        boolean isAvailable;
        if (managementStatus.matches("ACTIVE")) {
            isAvailable = false;
        } else {
            isAvailable = true;
        }
        carRepository.save(carRepository.findById(carId).get().setAvailable(isAvailable));
    }

    public void removeCar(Integer carId) {
        carRepository.delete(unwrapCar(carId));
    }

    public Set<CarDTO> findAllCars() {
        Set<CarDTO> carDtoSet = new HashSet<>();
        carRepository.findAll().forEach(car -> {
            carDtoSet.add(new CarDTO(car));
        });
        return carDtoSet;
    }

    Car unwrapCar(Integer carId) {
        if (carRepository.existsById(carId)) {
            return carRepository.findById(carId).get();
        } else {
            throw new CarNotFoundException(carId);
        }
    }

    CarDTO unwrapCarToCarDto(Optional<Car> carFromRepo, Integer carId) {
        if (carFromRepo.isEmpty()) {
            throw new CarNotFoundException(carId);
        }
        return new CarDTO(carFromRepo.get());
    }

    HttpStatus saveFile(MultipartFile file, String carRegistration) throws IOException {
        byte[] imageFile = file.getBytes();

        Car carFromRepo = carRepository.findByRegistration(carRegistration);
        carFromRepo.setImage(imageFile);
        carRepository.save(carFromRepo);
        return HttpStatus.CREATED;
    }
}

