package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
class CustomerController {
    
    CustomerFacade customerFacade;

    @PostMapping("/register")
    ResponseEntity<HttpStatus> createCustomer (@RequestBody Customer customer){
        customerFacade.addNewCustomer(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<HttpStatus> logInCustomer (@RequestBody Customer customer){
        customerFacade.findCustomer(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
