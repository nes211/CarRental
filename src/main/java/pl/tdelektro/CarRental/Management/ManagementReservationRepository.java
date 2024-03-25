package pl.tdelektro.CarRental.Management;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface ManagementReservationRepository extends CrudRepository<ManagementReservation, String> {

    Optional<ManagementReservation> findByReservationId(String reservationId);



}
