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

        randomCarId = reservations.stream().findFirst().get().getId();
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
        managementReservationRepository.deleteByReservationId(reservationId);
    }

    @Test
    @Order(0)
    public void addReservationTest() {

    }

    @Test
    @Order(1)
    public void removeReservationTest() {

    }

    @Test
    public void isCarAvailableTest() {


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
