package pl.tdelektro.CarRental.Management;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Transactional
interface ManagementReservationRepository extends CrudRepository<ManagementReservation, String> {

    Optional<ManagementReservation> findByReservationId(String reservationId);

    Set<ManagementReservation> findByStatus(ReservationStatus status);

    Set<ManagementReservation> findByCarIdAndStatusOrStatus(Integer carId, ReservationStatus status, ReservationStatus status1);

    Set<ManagementReservation> findByStatusOrStatus(ReservationStatus status, ReservationStatus status1);

    long deleteByReservationId(String reservationId);

    boolean existsByReservationId(String s);

    @Override
    Set<ManagementReservation> findAll();

    Stream<ManagementReservation> findByReservationIdOrderByReservationIdAsc(String reservationId);

    boolean existsByCustomerEmail(String customerEmail);

    long deleteByCustomerEmail(String customerEmail);
}
