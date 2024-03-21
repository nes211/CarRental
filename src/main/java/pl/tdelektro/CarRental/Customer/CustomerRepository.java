package pl.tdelektro.CarRental.Customer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findByIdNotNull();
    Optional<Customer> findByEmailAddress(String emailAddress);
    Optional<Customer> findByName(String username);
}
