package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerFacade {

        CustomerRepository customerRepository;
//        CustomerDTO customerDTO;
//
//
//        public Customer addNewCustomer(CustomerDTO customerDTO){
//            Customer customer = new Customer.CustomerBuilder()
//                    .name(customerDTO.name)
//                    .password(customerDTO.password)
//                    .emailAddress(customerDTO.emailAddress)
//                    .funds(customerDTO.funds)
//                    .build();
//            return customer;
//        }


}
