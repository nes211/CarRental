package pl.tdelektro.CarRental.Project;

import org.springframework.data.jpa.repository.JpaRepository;

interface CarRentalRepository extends JpaRepository<Car, Long> {
}
