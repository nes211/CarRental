package pl.tdelektro.CarRental.Auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.tdelektro.CarRental.Customer.CustomerRepository;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository
                .findByEmailAddress(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
