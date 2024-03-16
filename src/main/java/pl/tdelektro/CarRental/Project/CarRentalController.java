package pl.tdelektro.CarRental.Project;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController("/car")
@AllArgsConstructor
class CarRentalController {

    CarRentalRepository carRentalRepository;

    @GetMapping
    ResponseEntity<List<Car>>getAvailableCars(){
        List<Car> availableCarList = (List<Car>) carRentalRepository.findAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
