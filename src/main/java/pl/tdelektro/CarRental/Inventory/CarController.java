package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
class CarController {

    CarRepository carRepository;

    @GetMapping
    ResponseEntity<List<Car>>getAvailableCars(){
        List<Car> availableCarList = (List<Car>) carRepository.findAll();
        return new ResponseEntity<>(availableCarList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<CarDTO> getCarById (@PathVariable int id){
        Optional<Car> car = carRepository.findById(id);
        CarDTO carDTO = new CarDTO(car.get());
        return new ResponseEntity<>(carDTO,HttpStatus.OK);
    }

    @PostMapping("/addNew")
    ResponseEntity addNewCar(@RequestBody Car car){
        carRepository.save(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }






}
