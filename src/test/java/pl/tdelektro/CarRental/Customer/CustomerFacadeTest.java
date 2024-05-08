package pl.tdelektro.CarRental.Customer;


import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@ContextConfiguration(classes = CarRentalApplication.class)
public class CustomerFacadeTest {


    private CustomerFacade customerFacade;
    private CustomerRepository customerRepository;
    //ApplicationContext context =


    @Before
    public void warmup() {
//        customerRepository = context.getBean(CustomerRepository.class);
//        customerFacade = context.getBean(CustomerFacade.class);
    }

    @Test
    public void addNewCustomerTest() {

        Customer customer = new Customer("test@test.test", "test", "USER");

        customerFacade.addNewCustomer(customer);

//        assertEquals(customerRepository.findByEmailAddress("test@test.test"), customer);
//
//
//        assertSame(customer, customerRepository.findByEmailAddress(customer.getEmailAddress()).get());
//        assertFalse(customerRepository.findByEmailAddress(customer.getEmailAddress()).isEmpty());

    }

    @Test
    public void addNewCustomerWithDataTest() {

        String name = "testString";
        String customerEmail = "test@test.test";
        String password = "test";
        String role = "USER";
//
//        assertTrue(customerRepository.findByEmailAddress(customerEmail).isEmpty());
//        customerFacade.addNewCustomerWithData(name, customerEmail, password, role);
//        assertTrue(customerRepository.findByEmailAddress(customerEmail).isPresent());
//
//        Customer customer = customerRepository.findByEmailAddress(customerEmail).orElseThrow(CustomerNotFoundException::new);
//
//        Assertions.assertSame(customer, customerRepository.findByEmailAddress(customerEmail).get());


    }



    @Test
    public void getReservationsTest() {
        String[] statusString = {"PENDING", "ACTIVE", "COMPLETED", "UNKNOWN", "ANY_OTHER_TEXT"};

//        ManagementReservation reservation = ManagementReservation.builder()
//                .reservationId("test reservation")
//                .startDate(LocalDateTime.now().plusDays(2))
//                .endDate(LocalDateTime.now().plusDays(5))
//                .customerEmail("test@test.com")
//                .carId(5)
//                .build();


//        assertFalse(managementFacade.getReservations(statusString[0]).isEmpty());
//        assertFalse(managementFacade.getReservations(statusString[1]).isEmpty());
//        assertFalse(managementFacade.getReservations(statusString[2]).isEmpty());
//        assertFalse(managementFacade.getReservations(statusString[3]).isEmpty());
//        assertThrows(ReservationNotFoundException.class, () -> managementFacade.getReservations(statusString[4]));
    }

    @Test
    public void endReservationTest() {

//        ManagementReservation reservation = ManagementReservation.builder()
//                .reservationId("testId")
//                .customerEmail("test@test.test")
//                .carId(5)
//                .startDate(LocalDateTime.now().minusDays(2))
//                .endDate(LocalDateTime.now())
//                .status(ReservationStatus.ACTIVE)
//                .build();
//        when(managementReservationRepository.findByReservationId(anyString())).thenReturn(Optional.ofNullable(reservation));
//
//        managementFacade.endReservation(reservation);
//        verify(managementReservationRepository).findByReservationId("testId");
//        verify(managementReservationRepository).save(any(ManagementReservation.class));
    }
}
