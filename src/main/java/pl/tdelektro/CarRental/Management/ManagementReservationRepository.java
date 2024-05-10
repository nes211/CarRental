package pl.tdelektro.CarRental.Management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

interface ManagementReservationRepository extends CrudRepository<ManagementReservation, String> {

    Optional<ManagementReservation> findByReservationId(String reservationId);

    Set<ManagementReservation> findByStatus(ReservationStatus status);

    Set<ManagementReservation> findByCarIdAndStatusOrStatus(Integer carId, ReservationStatus status, ReservationStatus status1);

    Set<ManagementReservation> findByStatusOrStatus(ReservationStatus status, ReservationStatus status1);

    long deleteByReservationId(String reservationId);

    @Override
    Set<ManagementReservation> findAll();
}
