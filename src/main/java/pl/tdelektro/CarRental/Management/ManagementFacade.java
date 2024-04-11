package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Exception.CarNotAvailableException;
import pl.tdelektro.CarRental.Exception.NotEnoughFoundsException;
import pl.tdelektro.CarRental.Exception.ReservationManagementProblem;
import pl.tdelektro.CarRental.Exception.ReservationNotFoundException;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManagementFacade {

    private final List<ManagementReservation> reservations;
    private final ManagementReservationRepository managementReservationRepository;
    private final CarFacade carFacade;
    private final CustomerFacade customerFacade;
    private final ManagementInvoice managementInvoice;

    public void addReservation(ManagementReservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(ManagementReservation reservation) {
        reservations.remove(reservation);
    }

    public List<CarDTO> findAvailableCars(Integer carId, LocalDateTime startDate, LocalDateTime endDate) {

        Set<ManagementReservation> reservations = managementReservationRepository.findByCarIdAndStatusOrStatus(
                carId,
                ReservationStatus.PENDING,
                ReservationStatus.ACTIVE
        );
        return reservationsCheck(reservations, startDate, endDate);
    }

    public List<CarDTO> findAvailableCars(LocalDateTime startDate, LocalDateTime endDate) {
        Set<ManagementReservation> reservations = managementReservationRepository.findByStatusOrStatus(
                ReservationStatus.ACTIVE,
                ReservationStatus.PENDING
        );
        return reservationsCheck(reservations, startDate, endDate);
    }

    public List<CarDTO> reservationsCheck(Set<ManagementReservation> reservationSet, LocalDateTime startDate, LocalDateTime endDate) {
        List<ManagementReservation> reservationList = reservations.stream().filter(reservation ->
                reservation.getStartDate().isBefore(startDate)
                        && reservation.getEndDate().isBefore(startDate)
                        && reservation.getStartDate().isBefore(endDate)
                        && reservation.getEndDate().isBefore(endDate)
        ).toList();

        if (reservationList.isEmpty()) {
            return carFacade.findAllCars().stream().toList();
        } else {
            return new ArrayList<>();
        }
    }

    public ManagementReservationDTO rentCar(
            String customerEmail,
            LocalDateTime startRent,
            LocalDateTime endRent,
            Integer carId) {

        CarDTO carDTO = carFacade.findCarById(carId);
        CustomerDTO customerDTO = customerFacade.findCustomerByName(customerEmail);

        float totalRentCost = calculateRentalFee(startRent, endRent, carDTO.getOneDayCost());
        float foundsTotal = customerDTO.getFunds() - totalRentCost;


        if(!findAvailableCars(carId, startRent, endRent).isEmpty()){
            throw new CarNotAvailableException(carId, startRent, endRent);
        }

        if (foundsTotal < 0) {
            throw new NotEnoughFoundsException("Your account balance is insufficient by"
                    + foundsTotal + ". Please recharge your account.");
        }

        processingPayment(customerDTO, foundsTotal);

        ManagementReservation reservation = new ManagementReservation.ManagementReservationBuilder()
                .reservationId(
                        LocalDate.now() + " " +
                                customerDTO.getName() + " " +
                                UUID.randomUUID().toString().substring(0, 8))
                .customerEmail(customerEmail)
                .carId(carId)
                .startDate(startRent)
                .endDate(endRent)
                .totalReservationCost(totalRentCost)
                .status(setReservationStatus(startRent, endRent, carId))
                .build();

        managementReservationRepository.save(reservation);

        return new ManagementReservationDTO(reservation);
    }

    public void returnCar(String customerEmail, Integer carId, String reservationId) throws DocumentException, IOException {

        ManagementReservation reservationEnd = findReservation(reservationId);

        //Checking that  CustomerDTO and CarDTO exist in repo
        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customerEmail);
        CarDTO carToReturn = carFacade.findCarById(carId);
        setReservationStatus(reservationEnd.getStartDate(), reservationEnd.getEndDate(), carId);
        endReservation(reservationEnd);
        managementReservationRepository.save(reservationEnd);
        generateInvoice(reservationEnd);
    }

    public ManagementReservation findReservation(String reservationId) throws ReservationNotFoundException {
        Optional<ManagementReservation> reservation = managementReservationRepository.findByReservationId(reservationId);
        if (reservation.isPresent()) {
            ManagementReservation reservationToReturn = reservation.get();
            return reservationToReturn;
        } else {
            throw new ReservationNotFoundException(reservationId);
        }
    }

    public float calculateRentalFee(LocalDateTime startRent, LocalDateTime endRent, float oneDayCost) {

        return ChronoUnit.DAYS.between(startRent, endRent) * oneDayCost;
    }

    public boolean processingPayment(CustomerDTO customerFromRepo, float founds) {

        customerFromRepo.setFunds(customerFromRepo.getFunds() - founds);
        return customerFacade.editCustomer(customerFromRepo);
    }

    void generateInvoice(ManagementReservation reservation) throws DocumentException, IOException {

        managementInvoice.createInvoice(reservation);
    }

    ReservationStatus setReservationStatus(LocalDateTime startRent, LocalDateTime endRent, Integer carDtoId) {

        ReservationStatus reservationStatus;

        if (startRent.isBefore(LocalDateTime.now()) && endRent.isBefore(LocalDateTime.now())) {
            reservationStatus = ReservationStatus.COMPLETED;
        } else if (startRent.isBefore(LocalDateTime.now()) && endRent.isAfter(LocalDateTime.now())) {
            reservationStatus = ReservationStatus.ACTIVE;
        } else if (startRent.isAfter(LocalDateTime.now()) && endRent.isAfter(LocalDateTime.now())) {
            reservationStatus = ReservationStatus.PENDING;
        } else {
            reservationStatus = ReservationStatus.UNKNOWN;
        }

        carFacade.saveCarStatus(carDtoId, reservationStatus.toString());
        return reservationStatus;
    }

    Set<ManagementReservationDTO> getReservations(String status) {

        ReservationStatus reservationStatus = null;
        switch (status.toUpperCase()) {
            case "ACTIVE":
                reservationStatus = ReservationStatus.ACTIVE;
                break;
            case "PENDING":
                reservationStatus = ReservationStatus.PENDING;
                break;
            case "COMPLETED":
                reservationStatus = ReservationStatus.COMPLETED;
                break;
            case "UNKNOWN":
                reservationStatus = ReservationStatus.UNKNOWN;
                break;
            default:
                throw new ReservationNotFoundException();
        }
        Set<ManagementReservation> managementReservationSet = managementReservationRepository.findByStatus(reservationStatus);
        Set<ManagementReservationDTO> managementReservationDTOSet = new HashSet<>();

        if (managementReservationSet.isEmpty()) {
            throw new ReservationNotFoundException(status);
        } else {
            for (ManagementReservation reservation : managementReservationSet) {
                managementReservationDTOSet.add(new ManagementReservationDTO(reservation));
            }
        }
        return managementReservationDTOSet;
    }

    void startReservation(ManagementReservation reservation) {

        //Rest endpoint has ability to start
        ManagementReservation reservationFromRepo = findReservation(reservation.getReservationId());
        //Reservation can be started 2h before declared in reservation start time
        if (ChronoUnit.HOURS.between(reservationFromRepo.getStartDate(), LocalDateTime.now()) < 2) {
            reservationFromRepo.setStatus(ReservationStatus.ACTIVE);
            managementReservationRepository.save(reservationFromRepo);
        } else {
            throw new ReservationManagementProblem(
                    """
                            Reservation starts too early.
                            API accepts rent start only 2h before reservation date.
                            If its needed place new earlier reservation.
                            """);
        }
    }

    void endReservation(ManagementReservation reservation) {
        ManagementReservation reservationFromRepo = findReservation(reservation.getReservationId());
        reservationFromRepo.setStatus(ReservationStatus.COMPLETED);
        managementReservationRepository.save(reservationFromRepo);
    }
}
