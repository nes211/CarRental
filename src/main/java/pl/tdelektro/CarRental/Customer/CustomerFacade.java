package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFacade {

    CustomerRepository customerRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public void addNewCustomer(Customer customer) {
        Optional<Customer> customerCheck = customerRepository.findByEmailAddress(customer.emailAddress);
        if (customerCheck.isEmpty()) {
            Customer customerToSave = new Customer.CustomerBuilder()
                    .name(customer.emailAddress)
                    .password(bCryptPasswordEncoder.encode(customer.password))
                    .emailAddress(customer.emailAddress)
                    .funds(customer.funds)
                    .build();
            customerRepository.save(customerToSave);
        }
    }

    public Customer editCustomer(Customer customer) {

        // TODO: 19.03.2024
        return new Customer();
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findByIdNotNull();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        if (customerList.isEmpty()) {
            return Arrays.asList();
        } else {
            for (Customer customer : customerList) {
                CustomerDTO customerDTO = new CustomerDTO(customer);
                customerDTOList.add(customerDTO);
            }
            return customerDTOList;
        }
    }

    public void findCustomer(Customer customer) {
        Optional<Customer> optional = customerRepository.findByEmailAddress(customer.emailAddress);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not find in repo. Please register yourself");
        }
    }

    public CustomerDTO findCustomerByName(String username) {
        Optional<Customer> optional = customerRepository.findByName(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("Customer with name: " + username + " not find in repo. Please register yourself");
        }
        return new CustomerDTO(optional.get());
    }
}
