package pl.tdelektro.CarRental.Inventory;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface CarRepository extends CrudRepository<Car, Integer> {
    @Override
    Optional<Car> findById(Integer integer);

    List<Car> findByIdNotNull();
}
