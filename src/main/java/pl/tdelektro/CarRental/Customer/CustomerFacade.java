package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerFacade {

        CustomerRepository customerRepository;



        public Customer addNewCustomer(CustomerDTO customerDTO){
            Customer customer = new Customer.CustomerBuilder()
                    .name(customerDTO.name)
                    .password(customerDTO.password)
                    .emailAddress(customerDTO.emailAddress)
                    .funds(customerDTO.funds)
                    .build();
            
            customerRepository.save(customer);
            return customer;
        }
        public Customer editCustomer(CustomerDTO customerDTO){

            // TODO: 19.03.2024  
            return new Customer();
        }

}
