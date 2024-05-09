package pl.tdelektro.CarRental.Customer;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findByIdNotNull();

    Optional<Customer> findByEmailAddress(String emailAddress);

    @Transactional
    long deleteByEmailAddress(String emailAddress);
}
