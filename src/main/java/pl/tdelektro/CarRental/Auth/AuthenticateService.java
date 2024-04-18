package pl.tdelektro.CarRental.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.Customer;
import pl.tdelektro.CarRental.Customer.CustomerRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
class AuthenticateService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    List<GrantedAuthority> auths = new ArrayList<>();
    Set<String> roleSet = new HashSet<>();

    AuthenticationResponse register(RegisterRequest request) {
        auths.add(new SimpleGrantedAuthority("USER"));
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));

        var customer = Customer.builder()
                .name(request.getName())
                .emailAddress(request.getEmailAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .authorities(auths)
                .build();

        customerRepository.save(customer);

        var jwtToken = jwtService.generateToken(customer);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailAddress(), request.getPassword()
                )
        );

        var customer = customerRepository.findByEmailAddress(request.getEmailAddress()).orElseThrow();
        var jwtToken = jwtService.generateToken(customer);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}