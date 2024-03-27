package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/management")
@AllArgsConstructor
class ManagementController {

    private final ManagementFacade managementFacade;


    @PostMapping("/rent")
    ResponseEntity<ManagementReservationDTO> rentCar(
            @RequestBody ManagementReservation managementReservation) {

        return new ResponseEntity<>(managementFacade.rentCar
                (
                        managementReservation.getCustomerEmail(),
                        managementReservation.getStartDate(),
                        managementReservation.getEndDate(),
                        managementReservation.getCarId()
                ), HttpStatus.CREATED);
    }

    @PostMapping("/return")
    ResponseEntity<HttpStatus> returnCar(@RequestBody ManagementReservation managementReservation) throws DocumentException, IOException {
        managementFacade.returnCar(managementReservation.getCustomerEmail(),
                managementReservation.getCarId(),
                managementReservation.getReservationId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{reservations}")
    ResponseEntity<Set<ManagementReservationDTO>> getActiveReservations(@PathVariable String reservations) {

        return new ResponseEntity<>(managementFacade.getReservations(reservations.toUpperCase()), HttpStatus.OK);
    }


}
