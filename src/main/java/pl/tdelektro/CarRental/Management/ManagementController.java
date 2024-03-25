package pl.tdelektro.CarRental.Management;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/management")
@AllArgsConstructor
class ManagementController {

    private final ManagementFacade managementFacade;
    private final CustomerFacade customerFacade;
    private final CarFacade carFacade;

    @GetMapping("/rentCar")
    ResponseEntity<ManagementReservationDTO> rentCar(
            @RequestBody ManagementReservation managementReservation) {

        return new ResponseEntity<>(managementFacade.rentCar(
                managementReservation.getCustomerEmail(),
                managementReservation.getStartDate(),
                managementReservation.getEndDate(),
                managementReservation.getCarId()), HttpStatus.CREATED);
    }


}
