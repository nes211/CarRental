package pl.tdelektro.CarRental.Management;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;
import pl.tdelektro.CarRental.Exception.ReservationManagementProblem;
import pl.tdelektro.CarRental.Exception.ReservationNotFoundException;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagementFacadeTest {

    private final long yearsInPlus = 100;
    private final long daysInPlus = 10;
    private final LocalDateTime startReservation = LocalDateTime.now().plusYears(yearsInPlus);
    private final LocalDateTime endReservation = LocalDateTime.now().plusYears(yearsInPlus).plusDays(daysInPlus);
    private final int randomCarId = 5;

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
    @Mock
    private ManagementReservationRepository managementReservationRepository;
    @Mock
    private ManagementReservation reservation;
    @Spy
    @InjectMocks
    private ManagementFacade managementFacade;
    Integer carDtoId;

    @Before
    public void warmup() {

        MockitoAnnotations.openMocks(this);

        reservation = new ManagementReservation();
        reservation.setCarId(randomCarId);
        reservation.setStartDate(startReservation);
        reservation.setEndDate(endReservation);
        reservation.setStatus(ReservationStatus.REGISTERED);
        reservations.add(reservation);

    }

    @Test
    public void addReservationWithValidDataTest() {

        doReturn(true).when(managementFacade).checkReservationBeforeAdd(reservation);
        boolean result = managementFacade.addReservation(reservation);
        assertTrue(result);
        verify(managementFacade, times(1)).checkReservationBeforeAdd(reservation);
        verify(managementReservationRepository, times(0)).save(any(ManagementReservation.class));

    }

    @Test
    public void checkReservationBeforeAddTest() {
        doReturn(true)
                .when(managementFacade).isCarAvailable(
                        reservation.getCarId(),
                        reservation.getStartDate(),
                        reservation.getEndDate()
                );
        boolean result = managementFacade.checkReservationBeforeAdd(reservation);
        assertTrue(result);
        verify(managementReservationRepository, times(1)).save(reservation);
        verify(reservations, times(2)).add(reservation);
    }


    @Test
    public void removeReservationSuccessTest() {
        doReturn(Optional.of(reservation)).when(managementReservationRepository).findByReservationId(reservation.getReservationId());

        assertTrue(managementFacade.removeReservation(reservation));
        verify(reservations, times(1)).remove(reservation);
        verify(managementReservationRepository, times(1))
                .deleteByReservationId(reservation.getReservationId());
    }

    @Test
    public void removeReservationExceptionTest() {
        doReturn(Optional.of(reservation)).when(managementReservationRepository).findByReservationId(reservation.getReservationId());

        reservation.setReservationId("testId");
        assertThrows(ReservationNotFoundException.class, () -> managementFacade.removeReservation(reservation));
    }

    @Test
    public void isCarAvailableTest() {
        doNothing().when(managementFacade).checkInputData(
                reservation.getCarId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );

        Set<ManagementReservation> reservationSet = reservations.stream().collect(Collectors.toSet());
        doReturn(reservationSet).when(managementReservationRepository).findByCarIdAndStatusOrStatus(
                reservation.getCarId(),
                ReservationStatus.PENDING,
                ReservationStatus.ACTIVE
        );
        List<CarDTO> carDTOList = Arrays.asList(new CarDTO(
                randomCarId,
                "Test",
                "Test",
                "Test",
                "Test",
                1990,
                20,
                true,
                null)
        );

        reservations.add(reservation);
        doReturn(carDTOList).when(managementFacade).reservationsCheck(new HashSet<>(), reservation.getStartDate(), reservation.getEndDate());

        boolean result = managementFacade.isCarAvailable(
                reservation.getCarId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
        assertTrue(result);
    }

    @Test
    public void isCarAvailableCarNotAvailableTest() {

        doNothing().when(managementFacade).checkInputData(
                reservation.getCarId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );

        doReturn(reservations.stream().collect(Collectors.toSet())).when(managementReservationRepository).findByCarIdAndStatusOrStatus(
                reservation.getCarId(),
                ReservationStatus.PENDING,
                ReservationStatus.ACTIVE
        );

        List<CarDTO> carDTOList = Arrays.asList(new CarDTO(
                0,
                "Test",
                "Test",
                "Test",
                "Test",
                1990,
                20,
                true,
                null
                )
        );

        reservations.add(reservation);

        doReturn(carDTOList).when(managementFacade).reservationsCheck(new HashSet<>(), reservation.getStartDate(), reservation.getEndDate());

        boolean result = managementFacade.isCarAvailable(
                reservation.getCarId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
        assertFalse(result);
    }

    @Test
    public void checkInputDataSuccessTest() {

        when(carFacade.findCarById(randomCarId)).thenReturn(new CarDTO());
        assertDoesNotThrow(() ->
                managementFacade.checkInputData(
                        randomCarId,
                        reservation.getStartDate(),
                        reservation.getEndDate()
                ));
    }

    @Test
    public void checkInputDataFailsTest() {

        when(carFacade.findCarById(randomCarId)).thenReturn(new CarDTO());
        assertThrows(ReservationManagementProblem.class, () ->
                managementFacade.checkInputData(
                        randomCarId,
                        reservation.getEndDate(),
                        reservation.getStartDate()
                ));
    }

    @Test
    public void findAvailableCarsSuccessTest() {

        HashSet<ManagementReservation> reservationSet = new HashSet<>();
        reservationSet.add(reservation);
        HashSet<CarDTO> carDtoSet = new HashSet<>();
        carDtoSet.add(new CarDTO(randomCarId+1, "TEST", "TEST", "TEST", "TEST",1900,10, true, null));

        when(managementReservationRepository.findByStatusOrStatus(ReservationStatus.ACTIVE, ReservationStatus.PENDING)).thenReturn(reservationSet);
        when(carFacade.findAvailableCars().stream().collect(toSet())).thenReturn(carDtoSet);
        doReturn(Arrays.asList(new CarDTO())).when(managementFacade).reservationsCheck(reservationSet, startReservation, endReservation);

        List<CarDTO>carList = managementFacade.findAvailableCars(LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        assertFalse(carList.isEmpty());
        Assertions.assertEquals(randomCarId + 1, (int) carList.get(0).getId());

    }

    @Test
    public void findAvailableCarsFailsTest() {

        HashSet<ManagementReservation> reservationSet = new HashSet<>();
        reservationSet.add(reservation);
        HashSet<CarDTO> carDtoSet = new HashSet<>();
        carDtoSet.add(new CarDTO(randomCarId, "TEST", "TEST", "TEST", "TEST",1900,10, true, null));

        when(managementReservationRepository.findByStatusOrStatus(ReservationStatus.ACTIVE, ReservationStatus.PENDING)).thenReturn(reservationSet);
        when(carFacade.findAvailableCars().stream().collect(toSet())).thenReturn(carDtoSet);
        doReturn(Arrays.asList(new CarDTO())).when(managementFacade).reservationsCheck(reservationSet, startReservation, endReservation);

        List<CarDTO>carList = managementFacade.findAvailableCars(LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        assertTrue(carList.isEmpty());

    }

    @Test
    public void rentCarTest() {

        String customerEmail = "test@test.test";
        LocalDateTime startRent = LocalDateTime.now().plusDays(10);
        LocalDateTime endRent = LocalDateTime.now().plusDays(15);

        when(carFacade.findCarById(randomCarId))
                .thenReturn(
                        new CarDTO(randomCarId, "FSM", "126p", "C", "G3TTER", 1990, 10f, true, null)
                );

        when(customerDTO.getFunds()).thenReturn(2000f);
        when(customerFacade.findCustomerByName(customerEmail))
                .thenReturn(
                        new CustomerDTO(customerEmail, customerEmail, 2000f)
                );
        doReturn(true).when(managementFacade).isCarAvailable(randomCarId, startRent, endRent);
        managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId);

        assertEquals(Integer.valueOf(randomCarId), managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId).getCarId());
        assertEquals(startRent, managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId).getStartDate());
        assertEquals(endRent, managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId).getEndDate());
        assertEquals(50f, managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId).getTotalCost(), 0.001);
        assertEquals(ReservationStatus.PENDING.toString(), managementFacade.rentCar(customerEmail, startRent, endRent, randomCarId).getStatus());
    }

    @Test
    public void reservationReturnCarTest() {

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
        when(carFacade.findCarById(carDtoId)).thenReturn(new CarDTO(carDtoId, "FSM", "126p", "C", "MALUSZEK", 1990, 400f, true, null));

        LocalDateTime startRent = LocalDateTime.of(2024, 1, 10, 11, 30);
        LocalDateTime endRent = LocalDateTime.of(2024, 1, 11, 14, 30);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Completed", ReservationStatus.COMPLETED, reservationStatus);

        assertNotEquals(reservationStatus, ReservationStatus.UNKNOWN, "Reservation status Unknown");

        startRent = LocalDateTime.now().minusDays(2);
        endRent = LocalDateTime.now().plusDays(3);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Active", ReservationStatus.ACTIVE, reservationStatus);
        assertNotEquals(reservationStatus, ReservationStatus.UNKNOWN, "Reservation status Unknown");

        startRent = LocalDateTime.now().plusDays(5);
        endRent = LocalDateTime.now().plusDays(13);
        reservationStatus = managementFacade.setReservationStatus(startRent, endRent, carDtoId);
        assertEquals("Reservation status Pending", ReservationStatus.PENDING, reservationStatus);
        assertNotEquals(reservationStatus, ReservationStatus.UNKNOWN, "Reservation status Unknown");
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
        assertThrows(ReservationNotFoundException.class, () -> managementFacade.getReservations(statusString[4]));
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
        verify(managementReservationRepository).save(any(ManagementReservation.class));
    }

    @Test
    public void getListOfManufacturersTest(){

        List<String> listOfManufacturers = managementFacade.getListOfManufacturers();
        assertFalse(listOfManufacturers.isEmpty());
        assertTrue(listOfManufacturers.size() == 100);
    }
}
