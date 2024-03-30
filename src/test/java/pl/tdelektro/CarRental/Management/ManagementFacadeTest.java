package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;
import pl.tdelektro.CarRental.Exception.ReservationNotFoundException;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class ManagementFacadeTest {

    @Mock
    private List<ManagementReservation> reservations;

    @Mock
    private ManagementReservationRepository managementReservationRepository;

    @Mock
    private CarFacade carFacade;

    @Mock
    private CustomerFacade customerFacade;

    @Mock
    private ManagementInvoice managementInvoice;

    @InjectMocks
    ManagementFacade managementFacade;

    @Before
    public void warmup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void rentCarTest() {

        managementFacade = new ManagementFacade(reservations, managementReservationRepository, carFacade, customerFacade, managementInvoice);

        String customerEmail = "test@test.test";
        LocalDateTime startRent = LocalDateTime.of(2024, 06, 10, 11, 30);
        LocalDateTime endRent = LocalDateTime.of(2024, 06, 11, 14, 30);
        Integer carId = 4;

        when(carFacade.findCarById(carId)).thenReturn(new CarDTO(4, "FSM", "126p", "C", 1990, 400f, true));
        when(customerFacade.findCustomerByName(customerEmail)).thenReturn(new CustomerDTO(customerEmail, customerEmail, 2000f));

        ManagementReservationDTO reservationDTO = new ManagementReservationDTO("test reservation", 4, startRent, endRent, 400f, "COMPLETED");

        assertEquals("CarId not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getCarId(), reservationDTO.getCarId());
        assertEquals("StartDate not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStartDate(), reservationDTO.getStartDate());
        assertEquals("EndDate not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getEndDate(), reservationDTO.getEndDate());
        assertEquals("TotalCost not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getTotalCost(), reservationDTO.getTotalCost());
        assertEquals("Status not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStatus(), reservationDTO.getStatus());

    }

    @Test
    public void returnCarTest() throws DocumentException, IOException {

        managementFacade = new ManagementFacade(reservations, managementReservationRepository, carFacade, customerFacade, managementInvoice);
        String reservationId = "testString";
        String customerEmail = "test@test.test";

        Integer carId = 5;
        LocalDateTime startRent = LocalDateTime.of(2024, 06, 10, 11, 30);
        LocalDateTime endRent = LocalDateTime.of(2024, 06, 11, 14, 30);
        float totalReservationCost = 500f;
        ReservationStatus status = ReservationStatus.COMPLETED;

        assertThrows(ReservationNotFoundException.class, () -> managementFacade.returnCar(customerEmail, carId, reservationId));

        when(managementReservationRepository.findByReservationId(reservationId)).thenReturn(Optional.ofNullable(new ManagementReservation.ManagementReservationBuilder()
                .reservationId(reservationId)
                .customerEmail(customerEmail)
                .carId(carId)
                .startDate(startRent)
                .endDate(endRent)
                .totalReservationCost(totalReservationCost)
                .status(status)
                .build())
        );
        when(carFacade.findCarById(0)).thenThrow(new CarNotFoundException(carId));

        assertDoesNotThrow(() -> managementFacade.returnCar(customerEmail, carId, reservationId));
        assertThrows(CarNotFoundException.class, () -> managementFacade.returnCar(customerEmail, 0, reservationId));
    }
}
