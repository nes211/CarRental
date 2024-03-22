package pl.tdelektro.CarRental.Auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.util.Optional;

@Component
class CustomAuthenticationManager implements AuthenticationManager {


    CustomerFacade customerFacade = new CustomerFacade();
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomerDTO customerDTO = customerFacade.findCustomerByName(authentication.getName());

        System.out.println(" =========================== ");
        System.out.println(customerDTO.getName());
        System.out.println(customerDTO.getPassword());

        System.out.println(" =========================== ");
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getCredentials().toString());
        System.out.println(" =========================== ");

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), customerDTO.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), customerDTO.getPassword());
    }
}
