package pl.tdelektro.CarRental.Project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CarRentalRepository extends CrudRepository<Car, Long> {

    @Override
    Optional<Car> findById(Long aLong);
}
