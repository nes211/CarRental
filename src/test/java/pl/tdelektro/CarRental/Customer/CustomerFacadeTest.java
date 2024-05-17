package pl.tdelektro.CarRental.Customer;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

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
    CustomerFacade customerFacade;

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

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());
        customerFacade.addNewCustomer(customer);
        verify(customerRepository, times(1)).findByEmailAddress(customerEmail);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void addNewCustomerFailedTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.of(customer));
        assertThrows(CustomerNotFoundException.class, () -> customerFacade.addNewCustomer(customer));
        verify(customerRepository, times(1)).findByEmailAddress(any());
        verify(customerRepository, times(0)).save(customer);

    }

    @Test
    public void addNewCustomerWithDataSuccessTest() {

        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());

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
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());

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
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());
        assertTrue(customerFacade.editCustomer(customerDto));
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void editCustomerButCustomerDoesNotExistsTest() {

        CustomerDTO customerDto = new CustomerDTO(customerName, customerEmail, customerFounds);
        when(customerRepository.findByEmailAddress(customerEmail)).thenReturn(Optional.empty());
        try {
            customerFacade.editCustomer(customerDto);
        } catch (Exception e) {
            assertTrue(true);
        }
        verify(customerRepository, times(0)).save(any(Customer.class));

    }

    @Test
    public void getAllCustomersTest() {

    }

    @Test
    public void findCustomerTest() {

    }

    @Test
    public void findCustomerByNameTest() {

    }


    @Test
    public void findCustomerForUserDetailsTest() {

    }
}