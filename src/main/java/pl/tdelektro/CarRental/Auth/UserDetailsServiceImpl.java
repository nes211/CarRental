package pl.tdelektro.CarRental.Auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

@Component
@AllArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {
    CustomerFacade customerFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerFacade.findCustomerForUserDetails(username);
    }
}
