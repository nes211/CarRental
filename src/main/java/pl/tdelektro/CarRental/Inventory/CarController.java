package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Set;
import java.util.function.Supplier;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
class CarController {

    private final CarRepository carRepository;
    private final CarFacade carFacade;

    @GetMapping
    ResponseEntity<Set<CarDTO>> getAvailableCars() {
        return new ResponseEntity<>(carFacade.findAvailableCars(), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<Set<CarDTO>> getAllCarsFromRepo() {
        return new ResponseEntity<>(carFacade.findAllCars(), HttpStatus.OK);
    }

    @PostMapping("/addNew")
    ResponseEntity addNewCar(@RequestBody Car car) {
        carRepository.save(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{carId}")
    ResponseEntity deleteCar(@PathVariable Integer carId) {
        Car car = carRepository.findById(carId).get();
        carRepository.delete(car);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{carId}")
    ResponseEntity<CarDTO> findCar(@PathVariable Integer carId) throws Throwable {
        CarDTO car = new CarDTO(carRepository.findById(carId)
                .orElseThrow(()-> new CarNotFoundException(carId)));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }
}
