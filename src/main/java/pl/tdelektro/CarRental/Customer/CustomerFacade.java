package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class CustomerFacade {

    private CustomerRepository customerRepository;

    public void addNewCustomer(Customer customer) {
        Optional<Customer> customerCheck = customerRepository.findByEmailAddress(customer.getEmailAddress());
        if (customerCheck.isEmpty()) {
            Customer customerToSave = new Customer.CustomerBuilder()
                    .name(customer.getEmailAddress())
                    .password(new BCryptPasswordEncoder().encode(customer.getPassword()))
                    .emailAddress(customer.getEmailAddress())
                    .funds(customer.getFunds())
                    .role(customer.getRole())
                    .authorities(customer.getAuthorities())
                    .build();
            customerRepository.save(customerToSave);
        }
    }

    //Second methode add new Customer because auth service doesn't have access to class Customer
    public Customer addNewCustomerWithData(String name, String emailAddress, String password, String role) {
        var customer = Customer.builder()
                .name(name)
                .emailAddress(emailAddress)
                .password(password)
                .role(role)
                .build();
        if(customerRepository.findByEmailAddress(emailAddress).isEmpty()) {
            customerRepository.save(customer);
        }else{
            throw new CustomerNotFoundException(customer.getUsername(), customer.getName());
        }
        return customer;
    }

    public boolean editCustomer(CustomerDTO customerDto) {
        Customer customer = unwrapCustomer(customerRepository.findByEmailAddress(customerDto.emailAddress));
        customer.setEmailAddress(customerDto.emailAddress);
        customer.setName(customer.getUsername());
        customer.setFunds(customerDto.funds);
        customerRepository.save(customer);
        return true;
    }

    public void deleteCustomer (Customer customer){
        customerRepository.delete(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findByIdNotNull();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        if (customerList.isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            for (Customer customer : customerList) {
                CustomerDTO customerDTO = new CustomerDTO(customer);
                customerDTOList.add(customerDTO);
            }
            return customerDTOList;
        }
    }

    public Customer findCustomer(Customer customer) {
        Optional<Customer> optional = customerRepository.findByEmailAddress(customer.getEmailAddress());
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not find in repo. Please register yourself");
        } else {
            return optional.get();
        }
    }

    public CustomerDTO findCustomerByName(String customerEmail) {
        Optional<Customer> optional = customerRepository.findByEmailAddress(customerEmail);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("Customer with name: " + customerEmail + " not find in repo. Please register yourself");
        }
        return new CustomerDTO(optional.get());
    }

    public UserDetails findCustomerForUserDetails(String emailAddress) {
        return customerRepository.findByEmailAddress(emailAddress)
                .orElseThrow(()-> new CustomerNotFoundException("Customer with " + emailAddress +"not found in repo"));
    }

    Customer unwrapCustomer(Optional<Customer> customer) {
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new UsernameNotFoundException("Customer not found in repository");
        }
    }
}
