package pl.tdelektro.CarRental.Management;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

interface ManagementReservationRepository extends CrudRepository<ManagementReservation, String> {

    Optional<ManagementReservation> findByReservationId(String reservationId);

    Set<ManagementReservation> findByStatus(ReservationStatus status);



}
