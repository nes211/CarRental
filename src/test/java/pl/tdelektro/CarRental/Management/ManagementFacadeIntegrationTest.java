package pl.tdelektro.CarRental.Management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagementFacadeIntegrationTest {

    @Autowired
    private ManagementFacade managementFacade;

    @Autowired
    private ManagementReservationRepository managementReservationRepository;


    private final long yearsInPlus = 100;
    private final long daysInPlus = 10;
    private final int oneDayCost = 10;
    private final ReservationStatus status = ReservationStatus.REGISTERED;
    private final String customerEmail = "test@test.test";
    private final String reservationId = "id123456test";
    private final String reservationId2 = "2id123456test";

    private int randomCarId = 0;

    @BeforeEach
    public void warmup() {

        Set<ManagementReservation> reservations = managementReservationRepository.findAll();

        randomCarId = reservations.stream().findFirst().get().getCarId();
        ManagementReservation reservation = ManagementReservation.builder()
                .reservationId(reservationId)
                .customerEmail(customerEmail)
                .carId(randomCarId)
                .startDate(LocalDateTime.now().plusYears(yearsInPlus))
                .endDate(LocalDateTime.now().plusYears(yearsInPlus).plusDays(daysInPlus))
                .totalReservationCost(oneDayCost * daysInPlus)
                .status(status)
                .build();

        managementReservationRepository.save(reservation);
    }

    @AfterEach
    public void cleanup() {
        if(managementReservationRepository.existsByCustomerEmail(customerEmail)) {
            managementReservationRepository.deleteByCustomerEmail(customerEmail);
        }
    }

    @Test
    @Order(0)
    public void addReservationTest() {

        ManagementReservation newReservation = ManagementReservation.builder()
                .reservationId(reservationId2)
                .customerEmail(customerEmail)
                .carId(randomCarId)
                .startDate(LocalDateTime.now().plusYears(yearsInPlus))
                .endDate(LocalDateTime.now().plusYears(yearsInPlus).plusDays(daysInPlus))
                .totalReservationCost(oneDayCost * daysInPlus)
                .status(status)
                .build();

        assertTrue(managementFacade.addReservation(newReservation));
        ManagementReservation reservationForTests = managementReservationRepository.findByReservationId(reservationId2).get();

        assertTrue(managementReservationRepository.existsByReservationId(reservationId2));
        assertNotEquals(reservationId, reservationForTests.getReservationId());
        assertEquals(oneDayCost * daysInPlus, reservationForTests.getTotalReservationCost());
        assertFalse(reservationForTests.getCarId()<0);
        assertFalse(reservationForTests.getEndDate().isBefore(LocalDateTime.now()));

    }

    @Test
    @Order(1)
    public void removeReservationTest() {
        Set<ManagementReservation> allReservations = managementReservationRepository.findAll();

        ManagementReservation reservationToRemove = allReservations
                .stream().filter(reservation -> reservation.getReservationId().equals(reservationId)).findFirst().get();
        assertTrue(managementFacade.removeReservation(reservationToRemove));
        assertFalse(managementReservationRepository.existsByReservationId(reservationId));

    }

    @Test
    public void isCarAvailableTest() {
        Set<ManagementReservation> allReservations = managementReservationRepository.findAll();
        ManagementReservation reservation = allReservations.stream().findFirst().get();

        assertFalse(managementFacade.isCarAvailable(
                reservation.getCarId(),
                reservation.getStartDate(),
                reservation.getEndDate()));
        assertTrue(managementFacade.isCarAvailable(reservation.getCarId(),
                reservation.getStartDate().plusYears(10),
                reservation.getEndDate().plusYears(10).plusDays(10)));
    }

    @Test
    public void findAvailableCarsTest() {


    }

    @Test
    public void reservationsCheckTest() {

    }

    @Test
    public void rentCarTest() {

    }

    @Test
    public void returnCarTest() {

    }

    @Test
    public void findReservationTest() {


    }

    @Test
    public void calculateRentalFeeTest() {

    }

    @Test
    public void processingPaymentTest() {

    }

    @Test
    public void generateInvoiceTest() {

    }

    @Test
    public void setReservationStatusTest() {

    }

    @Test
    public void getReservationsTest() {

    }

    @Test
    public void startReservationTest() {

    }

    @Test
    public void endReservationTest() {

    }


}
