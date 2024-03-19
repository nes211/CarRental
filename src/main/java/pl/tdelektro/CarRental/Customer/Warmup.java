package pl.tdelektro.CarRental.Customer;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("customerWarmup")
@AllArgsConstructor
class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //Fill warmup data if repository is empty
        if (customerRepository.findByIdNotNull().isEmpty()) {

            customerRepository.save(new Customer.CustomerBuilder()
                    .name("Dawid")
                    .emailAddress("dawid@tom.com")
                    .password(passwordEncoder.encode("dawid"))
                    .funds(10000.2f)
                    .build());

            customerRepository.save(new Customer.CustomerBuilder()
                    .name("Tomasz")
                    .emailAddress("tom@tdelektro.pl")
                    .password(passwordEncoder.encode("tom"))
                    .funds(10000.2f)
                    .build());

        }

    }
}
