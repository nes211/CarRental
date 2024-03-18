package pl.tdelektro.CarRental.Customer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findByIdNotNull();
}
