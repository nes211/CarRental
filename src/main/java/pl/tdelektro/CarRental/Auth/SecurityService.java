package pl.tdelektro.CarRental.Auth;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
class SecurityService implements UserDetailsService {

    CustomerFacade customerFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final String[] customerName = new String[1];
        final String[] customerPassword = new String[1];

        List<CustomerDTO> customerList = customerFacade.getAllCustomers();
        List<GrantedAuthority> authority = new ArrayList<>();
        customerList.stream().forEach(customerDTO -> {
            if(customerDTO.getName().equals(username)){
                authority.add(new SimpleGrantedAuthority("USER"));
                customerName[0] = customerDTO.getName();
                customerPassword[0] = customerDTO.getPassword();
                new User(customerName[0], customerPassword[0], authority);
            }

        });
        return new User(customerName[0], customerPassword[0], authority);
    }
}
