package pl.tdelektro.CarRental.Inventory;

import org.springframework.data.jpa.repository.Query;
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

    @Query("select (count(c) > 0) from Car c where c.make = ?1")
    boolean existsByMake(String make);

    Car findByMake(String make);

    long deleteByMake(String make);

    Car findByRegistration(String registration);
    Boolean existsByRegistration(String registration);

}
