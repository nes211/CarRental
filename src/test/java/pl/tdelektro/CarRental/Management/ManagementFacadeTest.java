package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ManagementFacadeTest {
    @Mock
    private ManagementReservationRepository managementReservationRepository;
    @Mock
    private List<ManagementReservation> reservations;

    @Mock
    private CarFacade carFacade;

    @Mock
    private CustomerFacade customerFacade;

    @Mock
    private ManagementInvoice managementInvoice;

    @Mock
    CustomerDTO customerDTO;

    @InjectMocks
    private ManagementFacade managementFacade;

    @Before
    public void warmup() {
        MockitoAnnotations.initMocks(this);
    }

    Integer carDtoId;
    boolean isAvailable;


    @Test
    public void rentCarTest() {

        String customerEmail = "test@test.test";
        customerDTO = new CustomerDTO(customerEmail, customerEmail, 2000f);
        LocalDateTime startRent = LocalDateTime.now().plusDays(10);
        LocalDateTime endRent = LocalDateTime.now().plusDays(15);
        Integer carId = 4;
        ManagementFacade managementFacade = Mockito.mock(ManagementFacade.class);
        //Mockito.when(managementReservation.).thenReturn(customerEmail);

        managementFacade.rentCar(customerEmail, startRent, endRent, carId);


//        when(carFacade.findCarById(carId)).thenReturn(new CarDTO(carId, "FSM", "126p", "C", 1990, 400f, true));
//        when(customerFacade.findCustomerByName("test")).thenReturn(customerDTO);




        assertEquals("CarId not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getCarId() , carId);
        assertEquals("StartDate not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStartDate(), startRent);
        assertEquals("EndDate not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getEndDate(), endRent);
        assertEquals("TotalCost not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getTotalCost(), 2000f);
        assertEquals("Status not the same", managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStatus(), ReservationStatus.ACTIVE);
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

    @Test
    public void setReservationStatusTest() {

        carDtoId = 0;
        ReservationStatus reservationStatus;
        when(carFacade.findCarById(carDtoId)).thenReturn(new CarDTO(carDtoId, "FSM", "126p", "C", 1990, 400f, true));

        LocalDateTime startRent = LocalDateTime.of(2024, 01, 10, 11, 30);
        LocalDateTime endRent = LocalDateTime.of(2024, 01, 11, 14, 30);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Completed", ReservationStatus.COMPLETED, reservationStatus);
        assertFalse(reservationStatus.equals(ReservationStatus.UNKNOWN), "Reservation status Unknown");

        startRent = LocalDateTime.now().minusDays(2);
        endRent = LocalDateTime.now().plusDays(3);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Active", ReservationStatus.ACTIVE, reservationStatus);
        assertFalse(reservationStatus.equals(ReservationStatus.UNKNOWN), "Reservation status Unknown");

        startRent = LocalDateTime.now().plusDays(5);
        endRent = LocalDateTime.now().plusDays(13);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Pending", ReservationStatus.PENDING, reservationStatus);
        assertFalse(reservationStatus.equals(ReservationStatus.UNKNOWN), "Reservation status Unknown");
    }

    @Test
    public void getReservationsTest() {

    }


}
