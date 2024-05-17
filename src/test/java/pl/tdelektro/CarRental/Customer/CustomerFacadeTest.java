package pl.tdelektro.CarRental.Customer;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerFacadeTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerFacade customerFacade;

    private final String customerName = "testString";
    private final String customerPassword = "test";
    private final String customerEmail = "test@test.test";
    private final String customerPhoneNumber = "100200100";
    private final String customerRole = "USER";
    private final float customerFounds = 2000f;
    private Customer customer = new Customer(
            0,
            customerName,
            customerPassword,
            customerEmail,
            customerPhoneNumber,
            customerRole,
            new ArrayList<>(),
            customerFounds
    );

    @Before
    public void warmup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addNewCustomerSuccessTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(empty());
        customerFacade.addNewCustomer(customer);
        verify(customerRepository, times(1)).findByEmailAddress(customerEmail);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void addNewCustomerFailedTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(of(customer));
        assertThrows(CustomerNotFoundException.class, () -> customerFacade.addNewCustomer(customer));
        verify(customerRepository, times(1)).findByEmailAddress(any());
        verify(customerRepository, times(0)).save(customer);

    }

    @Test
    public void addNewCustomerWithDataSuccessTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(empty());

        try {
            customerFacade.addNewCustomerWithData(customerName, customerEmail, customerPassword, customerRole);
        } catch (Exception e) {
            fail("Exception was thrown");
        }

        verify(customerRepository, times(1)).findByEmailAddress(customerEmail);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void addNewCustomerWithDataFailedTest() {
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(empty());

        try {
            customerFacade.addNewCustomerWithData(customerName, customerEmail, customerPassword, customerRole);
        } catch (Exception e) {
            assertThrows(CustomerNotFoundException.class, () -> customerRepository.findByEmailAddress(customerEmail));
        }

        verify(customerRepository, times(1)).findByEmailAddress(customerEmail);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void editCustomerCustomerExistsTest() {

        CustomerDTO customerDto = new CustomerDTO(customerName, customerEmail, customerFounds);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(of(customer));
        assertTrue(customerFacade.editCustomer(customerDto));
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void editCustomerButCustomerDoesNotExistsTest() {

        CustomerDTO customerDto = new CustomerDTO(customerName, customerEmail, customerFounds);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(empty());
        try {
            customerFacade.editCustomer(customerDto);
        } catch (Exception e) {
            assertTrue(true);
        }
        verify(customerRepository, times(0)).save(any(Customer.class));

    }

    @Test
    public void getAllCustomersTest() {

        Customer customer1 = new Customer(customerEmail, customerPassword, customerRole);
        Customer customer2 = new Customer("2" + customerEmail, customerPassword, customerRole);

        when(customerRepository.findByIdNotNull()).thenReturn(Arrays.asList(customer1, customer2));

        List<CustomerDTO> allCustomers = customerFacade.getAllCustomers();

        assertEquals(2, allCustomers.size());
        assertTrue(allCustomers.stream().anyMatch(user -> user.getEmailAddress().equals(customer1.getEmailAddress())));
        assertTrue(allCustomers.stream().anyMatch(user -> user.getEmailAddress().equals(customer2.getEmailAddress())));
        verify(customerRepository, times(1)).findByIdNotNull();

    }

    @Test
    public void getAllCustomersNoCustomerInRepoTest() {

        when(customerRepository.findByIdNotNull()).thenReturn(new ArrayList<>());
        assertThrows(CustomerNotFoundException.class, () -> customerFacade.getAllCustomers());
        verify(customerRepository, times(1)).findByIdNotNull();

    }

    @Test
    public void findCustomerTest() {

        Customer customerForTest = new Customer(customerEmail, customerPassword, customerRole);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(of(customerForTest));
        try {
            customerFacade.findCustomer(customerForTest);
        } catch (Exception e) {
            fail("Exception found");
        }
        verify(customerRepository, times(1)).findByEmailAddress(any(String.class));

    }

    @Test
    public void findCustomerFailTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(empty());
        assertThrows(UsernameNotFoundException.class, () -> customerFacade.findCustomer(customer));
        verify(customerRepository, times(1)).findByEmailAddress(any(String.class));

    }

    @Test
    public void findCustomerByNameTest() {

        Customer customerForTests = new Customer(customerName, customerEmail, customerRole);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(of(customerForTests));
        CustomerDTO customerDTO = null;
        try {
            customerDTO = customerFacade.findCustomerByName(customerEmail);
        } catch (Exception e) {
            fail("Customer not found");
        }
        assertTrue(customerDTO.getEmailAddress().equals(customerName));
    }

    @Test
    public void findCustomerByNameFailTest() {

        Customer customerForTests = new Customer(customerName, customerEmail, customerRole);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());
        CustomerDTO customerDTO = null;
        try {
            customerDTO = customerFacade.findCustomerByName(customerEmail);
        } catch (Exception e) {
            assertThrows(CustomerNotFoundException.class, () -> customerFacade.findCustomerByName(customerEmail));
            assertTrue(true);
        }
        assertNull(customerDTO);
    }

    @Test
    public void findCustomerForUserDetailsTest() {
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(of(customer));

        try {
            customerFacade.findCustomerForUserDetails(customerEmail);
        } catch (Exception e) {
            fail("Exception found");
        }
        verify(customerRepository, times(1)).findByEmailAddress(customerEmail);
    }

    @Test
    public void findCustomerForUserDetailsFailTest() {
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());

        try {
            customerFacade.findCustomerForUserDetails(customerEmail);
        } catch (Exception e) {
            assertThrows(UsernameNotFoundException.class, ()-> customerFacade.findCustomerForUserDetails(customerEmail));
        }
        verify(customerRepository, times(2)).findByEmailAddress(customerEmail);
    }
}