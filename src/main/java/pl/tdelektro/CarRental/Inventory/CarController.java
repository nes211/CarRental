package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
class CarController {

    private final CarRepository carRepository;
    private final CarFacade carFacade;

    @GetMapping
    ResponseEntity<Set<CarDTO>> getAvailableCars() {
        return new ResponseEntity<>(carFacade.findAllCars(), HttpStatus.OK);
    }

    @GetMapping("/{carId}")
    ResponseEntity<CarDTO> getCarById(@PathVariable Integer carId) {
        Optional<Car> car = carRepository.findById(carId);
        if(car.isPresent()) {
            return new ResponseEntity<>(new CarDTO(car.get()), HttpStatus.OK);
        }else{
            throw new CarNotFoundException(carId);
        }
    }

    @PostMapping("/addNew")
    ResponseEntity addNewCar(@RequestBody Car car) {
        carRepository.save(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    ResponseEntity<HttpStatus> findMyCustomer(){
        System.out.println("Service unavailable ");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
