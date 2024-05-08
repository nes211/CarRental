package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Set;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
class CarController {

    private final CarRepository carRepository;
    private final CarFacade carFacade;
    private ApplicationContext applicationContext;

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
        if (carRepository.existsByRegistration(car.getRegistration())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(carRepository.save(car), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{registration}")
    ResponseEntity deleteCar(@PathVariable String registration) {
        if (carRepository.existsByRegistration(registration)) {
            Car car = carRepository.findByRegistration(registration);
            carRepository.delete(car);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{carId}")
    ResponseEntity<CarDTO> findCar(@PathVariable Integer carId) throws Throwable {
        CarDTO car = new CarDTO(carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId)));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }
    @GetMapping("/context")
    ResponseEntity<String[]> applicationContext() {
        String[] stringOfBeans = applicationContext.getBeanDefinitionNames();
        return new ResponseEntity<>(stringOfBeans, HttpStatus.OK);
    }
}
