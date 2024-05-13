package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tdelektro.CarRental.Inventory.CarDTO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/management")
@AllArgsConstructor
class ManagementController {

    private ManagementFacade managementFacade;

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

    @PostMapping("/start")
    ResponseEntity<HttpStatus> startRegisteredReservation(@RequestBody ManagementReservation managementReservation) {
        managementFacade.startReservation(managementReservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/return")
    ResponseEntity<HttpStatus> returnCar(@RequestBody ManagementReservation managementReservation) throws DocumentException, IOException {
        managementFacade.returnCar(managementReservation.getCustomerEmail(),
                managementReservation.getCarId(),
                managementReservation.getReservationId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/find")
    ResponseEntity<List<CarDTO>> findFilter(@Valid @RequestBody ManagementReservation reservationAvailable) {
        if (reservationAvailable.getCarId() != null) {
            return new ResponseEntity<>(managementFacade.findAvailableCars
                    (
                            reservationAvailable.getStartDate(),
                            reservationAvailable.getEndDate()
                    )
                    , HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(managementFacade.findAvailableCars
                    (
                            reservationAvailable.getStartDate(),
                            reservationAvailable.getEndDate()
                    )
                    , HttpStatus.OK);
        }
    }

    @GetMapping("/reservations/{reservationType}")
    ResponseEntity<Set<ManagementReservationDTO>> getActiveReservations(@PathVariable String reservationType) {

        return new ResponseEntity<>(managementFacade.getReservations(reservationType.toUpperCase()), HttpStatus.OK);
    }
}
