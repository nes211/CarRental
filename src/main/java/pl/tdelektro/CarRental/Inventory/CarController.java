package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Set;

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

    @GetMapping("/find/{carId}")
    ResponseEntity<CarDTO> findCarById(@PathVariable Integer carId) throws Throwable {
        CarDTO car = new CarDTO(carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId)));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }
    @GetMapping("/{carRegistration}")
    ResponseEntity<CarDTO> findCarByRegistration(@PathVariable String carRegistration){
        CarDTO car = new CarDTO(carRepository.findByRegistration(carRegistration));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/image/{carRegistration}")
    ResponseEntity<byte[]> getImage(@PathVariable String carRegistration) {
        CarDTO carDto = new CarDTO(carRepository.findByRegistration(carRegistration));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(carDto.getImage());
    }

    @PostMapping("/image/{carRegistration}/upload")
    ResponseEntity uploadCarImage(@RequestParam MultipartFile file, @PathVariable String carRegistration) {

        try {
            HttpStatus status = carFacade.saveFile(file, carRegistration);
            return new ResponseEntity("Image uploaded successfully.", status);
        } catch (Exception e) {
            throw new CarNotFoundException(carRegistration);
        }
    }
}
