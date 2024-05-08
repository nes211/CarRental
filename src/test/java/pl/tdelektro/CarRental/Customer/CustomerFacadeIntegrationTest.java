package pl.tdelektro.CarRental.Customer;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tdelektro.CarRental.CarRentalApplication;
import pl.tdelektro.CarRental.CarRentalApplicationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = CarRentalApplication.class)
public class CustomerFacadeIntegrationTest {

    @Autowired
    private CustomerFacade customerFacade;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String name = "testName";
    private String newName = "newTestName";
    private String customerEmail = "test@test.test";
    private String password = "test";
    private String newPassword = "newTest";
    private String role = "USER";

    private Customer customer = new Customer(customerEmail, password, role);

    @Test
    @Order(1)
    public void addCustomerTest() {

        customerFacade.addNewCustomer(customer);

        assertTrue(customerRepository.findByEmailAddress(customerEmail).isPresent());
        assertEquals(customer.getEmailAddress(), customerRepository.findByEmailAddress(customerEmail).get().getEmailAddress());
        assertEquals(customer.getName(), customerRepository.findByEmailAddress(customerEmail).get().getName());
        assertTrue(passwordEncoder.matches(password, customerRepository.findByEmailAddress(customerEmail).get().getPassword()));
    }

    @Test
    @Order(2)
    public void editCustomerWithDataTest() {




    }

    @Test
    @Order(3)
    public void editCustomerTest() {
    }

    @Test
    @Order(4)
    public void getAllCustomers() {
    }

    @Test
    @Order(5)
    public void findCustomer() {
    }

    @Test
    @Order(6)
    public void findCustomerByName() {
    }

    @Test
    @Order(7)
    public void deleteCustomerTest() {
    }

    @Test
    @Order(8)
    public void checkData(){

    }

}
