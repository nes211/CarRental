package pl.tdelektro.CarRental.Customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tdelektro.CarRental.CarRentalApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CarRentalApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    private String newCustomerEmail = "test2@test.test";
    private String password = "test";
    private String newPassword = "newTest";
    private String role = "USER";

    private Customer customer = new Customer(customerEmail, password, role);

//    @BeforeClass
//    public static void checkBeforeTest(){
//
//    }


    @Test
    @Order(0)
    public void addCustomerTest() {
        customerFacade.addNewCustomer(customer);

        assertTrue(customerRepository.findByEmailAddress(customerEmail).isPresent());
        assertEquals(customer.getEmailAddress(), customerRepository.findByEmailAddress(customerEmail).get().getEmailAddress());
        assertEquals(customer.getName(), customerRepository.findByEmailAddress(customerEmail).get().getName());
        assertTrue(passwordEncoder.matches(password, customerRepository.findByEmailAddress(customerEmail).get().getPassword()));
    }

    @Test
    @Order(1)
    public void addNewCustomerWithDataTest() {
        Customer newCustomer = customerFacade.addNewCustomerWithData(
                newName,
                newCustomerEmail,
                passwordEncoder.encode(newPassword),
                role);

        assertTrue(customerRepository.findByEmailAddress(newCustomerEmail).isPresent());
        assertEquals(customerRepository.findByEmailAddress(newCustomerEmail).get().getName(), newName);
        assertEquals(customerRepository.findByEmailAddress(newCustomerEmail).get().getEmailAddress(), newCustomerEmail);
        assertTrue(passwordEncoder.matches(
                newPassword,
                customerRepository.findByEmailAddress(newCustomerEmail).get().getPassword()));
    }

    @Test
    @Order(2)
    public void editCustomerWithDataTest() {
        String testName = "Kazik";
        Customer customer = customerRepository.findByEmailAddress(newCustomerEmail).get();
        CustomerDTO customerDTO = new CustomerDTO(customer);
        customerDTO.setName(testName);
        assertTrue(customerFacade.editCustomer(customerDTO));
        CustomerDTO customerDTOWithWrongEmail = new CustomerDTO("Pawel", "pawel@pawel.com", 0f);
        assertThrows(RuntimeException.class, () -> customerFacade.editCustomer(customerDTOWithWrongEmail));
        assertEquals(testName, customerRepository.findByEmailAddress(newCustomerEmail).get().getName());
    }

    @Test
    @Order(3)
    public void getAllCustomersTest() {
        List<CustomerDTO> customerList = customerFacade.getAllCustomers();
        assertNotNull(customerList);
        customerList.stream().forEach(customerDTO -> {
            assertNotNull(customerDTO.name);
            assertNotNull(customerDTO.emailAddress);
            assertNull(customerDTO.password);
            assertTrue(customerDTO.funds >= 0);
        });
    }

    @Test
    @Order(4)
    public void findCustomerTest() {
        Customer customerToCheck = customerFacade.findCustomer(customer);
        assertTrue(customerToCheck.getEmailAddress().matches(customer.getEmailAddress()));
    }

    @Test
    @Order(5)
    public void findCustomerByNameTest() {
        CustomerDTO customerToCheck = customerFacade.findCustomerByName(customerEmail);
        assertFalse(customerToCheck.getEmailAddress().isEmpty());
    }

    @Test
    @Order(6)
    public void deleteCustomerTest() {
        customerFacade.deleteCustomer(customerEmail);
        customerFacade.deleteCustomer(newCustomerEmail);
        List<CustomerDTO> customerDTOList = customerFacade.getAllCustomers();
        List<CustomerDTO> needEmplyList = customerDTOList.stream().filter(object -> {
            if (object.getEmailAddress().contains(customerEmail) || object.getEmailAddress().contains(newCustomerEmail)) {
                return true;
            } else return false;
        }).toList();
        assertTrue(needEmplyList.isEmpty());
    }


    @Test
    @Order(7)
    public void checkData() {

        List<CustomerDTO> customerDtoList = customerFacade.getAllCustomers();
        customerDtoList.stream().dropWhile(customer -> {
            if (customer.getName().equals(name)) {
                return true;
            } else return false;
        });
    }
}
