package pl.tdelektro.CarRental.Project;

import org.springframework.data.repository.CrudRepository;

interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
