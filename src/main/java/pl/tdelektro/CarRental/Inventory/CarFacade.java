package pl.tdelektro.CarRental.Inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

@AllArgsConstructor
@Service
public class CarFacade {

    CarRepository carRepository;
    CustomerFacade customerFacade;




}
