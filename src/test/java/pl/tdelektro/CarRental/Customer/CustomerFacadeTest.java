package pl.tdelektro.CarRental.Customer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

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
        assertThrows(CustomerNotFoundException.class, ()->customerFacade.addNewCustomer(customer));
        verify(customerRepository, times(1)).findByEmailAddress(any());
        verify(customerRepository, times(0)).save(customer);

    }

    @Test
    public void addNewCustomerWithDataSuccessTest() {

    }

    @Test
    public void addNewCustomerWithDataFailedTest() {

    }


    @Test
    public void getReservationsTest() {

        String[] statusString = {"PENDING", "ACTIVE", "COMPLETED", "UNKNOWN", "ANY_OTHER_TEXT"};

    }

    @Test
    public void endReservationTest() {

    }
}
