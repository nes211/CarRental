package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerFacade {

        CustomerRepository customerRepository;
        BCryptPasswordEncoder bCryptPasswordEncoder;


        public void addNewCustomer(Customer customer){
            Customer customerToSave = new Customer.CustomerBuilder()
                    .name(customer.name)
                    .password(bCryptPasswordEncoder.encode(customer.password))
                    .emailAddress(customer.emailAddress)
                    .funds(customer.funds)
                    .build();
            
            customerRepository.save(customerToSave);

        }
        public Customer editCustomer(CustomerDTO customerDTO){

            // TODO: 19.03.2024  
            return new Customer();
        }

        public List<CustomerDTO> getAllCustomers (){
            List <Customer> customerList = customerRepository.findByIdNotNull();
            List <CustomerDTO> customerDTOList = new ArrayList<>();
                if(customerList.isEmpty()){
                    return Arrays.asList();
                }else{
                    for(Customer customer : customerList){
                        CustomerDTO customerDTO = new CustomerDTO(customer);
                        customerDTOList.add(customerDTO);
                    }
                    return customerDTOList;
                }
        }

}
