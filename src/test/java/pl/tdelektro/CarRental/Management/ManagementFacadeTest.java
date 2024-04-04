package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private CustomerDTO customerDTO;
    @InjectMocks
    private ManagementFacade managementFacade;
    Integer carDtoId;

    @Before
    public void warmup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void rentCarTest() {

        String customerEmail = "test@test.test";
        LocalDateTime startRent = LocalDateTime.now().plusDays(10);
        LocalDateTime endRent = LocalDateTime.now().plusDays(15);
        Integer carId = 4;

        when(carFacade.findCarById(carId))
                .thenReturn(
                        new CarDTO(carId, "FSM", "126p", "C", 1990, 400f, true)
                );

        when(customerDTO.getFunds()).thenReturn(2000f);
        when(customerFacade.findCustomerByName(customerEmail))
                .thenReturn(
                        new CustomerDTO(customerEmail, customerEmail, 2000f)
                );
        managementFacade.rentCar(customerEmail, startRent, endRent, carId);

        assertEquals(carId, managementFacade.rentCar(customerEmail, startRent, endRent, carId).getCarId());
        assertEquals(startRent, managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStartDate());
        assertEquals(endRent, managementFacade.rentCar(customerEmail, startRent, endRent, carId).getEndDate());
        assertEquals(2000f, managementFacade.rentCar(customerEmail, startRent, endRent, carId).getTotalCost(), 0.001);
        assertEquals(ReservationStatus.PENDING.toString(), managementFacade.rentCar(customerEmail, startRent, endRent, carId).getStatus());
    }

    @Test
    public void reservationReturnCarTest() throws DocumentException, IOException {

        managementFacade = new ManagementFacade(reservations, managementReservationRepository, carFacade, customerFacade, managementInvoice);
        String reservationId = "testString";
        String customerEmail = "test@test.test";

        Integer carId = 5;
        LocalDateTime startRent = LocalDateTime.now().plusDays(5);
        LocalDateTime endRent = LocalDateTime.now().plusDays(6);
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
        String[] statusString = {"PENDING", "ACTIVE", "COMPLETED", "UNKNOWN", "ANY_OTHER_TEXT"};

        ManagementReservation reservation = ManagementReservation.builder()
                .reservationId("test reservation")
                .startDate(LocalDateTime.now().plusDays(2))
                .endDate(LocalDateTime.now().plusDays(5))
                .customerEmail("test@test.com")
                .carId(5)
                .build();

        when(managementReservationRepository.findByStatus(ReservationStatus.PENDING))
                .thenReturn(Set.of(reservation.setStatus(ReservationStatus.PENDING)));

        when(managementReservationRepository.findByStatus(ReservationStatus.ACTIVE))
                .thenReturn(Set.of(reservation.setStatus(ReservationStatus.ACTIVE)));

        when(managementReservationRepository.findByStatus(ReservationStatus.COMPLETED))
                .thenReturn(Set.of(reservation.setStatus(ReservationStatus.COMPLETED)));

        when(managementReservationRepository.findByStatus(ReservationStatus.UNKNOWN))
                .thenReturn(Set.of(reservation.setStatus(ReservationStatus.UNKNOWN)));

        assertFalse(managementFacade.getReservations(statusString[0]).isEmpty());
        assertFalse(managementFacade.getReservations(statusString[1]).isEmpty());
        assertFalse(managementFacade.getReservations(statusString[2]).isEmpty());
        assertFalse(managementFacade.getReservations(statusString[3]).isEmpty());
        assertThrows(ReservationNotFoundException.class, ()-> managementFacade.getReservations(statusString[4]));
    }

    @Test
    public void endReservationTest() {

        ManagementReservation reservation = ManagementReservation.builder()
                .reservationId("testId")
                .customerEmail("test@test.test")
                .carId(5)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now())
                .status(ReservationStatus.ACTIVE)
                .build();
        when(managementReservationRepository.findByReservationId(anyString())).thenReturn(Optional.ofNullable(reservation));

        managementFacade.endReservation(reservation);
        verify(managementReservationRepository).findByReservationId("testId");
        verify(managementReservationRepository).save(any(ManagementReservation.class));
    }
}
