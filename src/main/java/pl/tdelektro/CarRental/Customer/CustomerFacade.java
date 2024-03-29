package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerFacade {

    CustomerRepository customerRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;


    //For test purpose
    public CustomerFacade() {
    }

    public void addNewCustomer(Customer customer) {
        Optional<Customer> customerCheck = customerRepository.findByEmailAddress(customer.getEmailAddress());
        if (customerCheck.isEmpty()) {
            Customer customerToSave = new Customer.CustomerBuilder()
                    .name(customer.getEmailAddress())
                    .password(bCryptPasswordEncoder.encode(customer.getPassword()))
                    .emailAddress(customer.getEmailAddress())
                    .funds(customer.getFunds())
                    .build();
            customerRepository.save(customerToSave);
        }
    }

    public boolean editCustomer(CustomerDTO customerDto) {
        Customer customer = unwrapCustomer(customerRepository.findByEmailAddress(customerDto.emailAddress));
        customer.setEmailAddress(customerDto.emailAddress);
        customer.setName(customer.getUsername());
        customer.setFunds(customerDto.funds);
        customerRepository.save(customer);
        return true;
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
        }else{
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
    Customer unwrapCustomer(Optional<Customer> customer){
        if(customer.isPresent()){
            return customer.get();
        }else{
            throw new UsernameNotFoundException("Customer not found in repository");
        }

    }
}
