package pl.tdelektro.CarRental.Inventory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
interface CarRepository extends CrudRepository<Car, Integer> {
    @Override
    Optional<Car> findById(Integer integer);

    List<Car> findByIdNotNull();

    Set<Car> findByIsAvailableTrue();
}
