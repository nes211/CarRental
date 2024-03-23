package pl.tdelektro.CarRental.Managment;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ManagementReservationRepository extends CrudRepository<ManagementReservation, Integer> {

    Optional<ManagementReservation> findByReservationId(Integer reservationId);



}
