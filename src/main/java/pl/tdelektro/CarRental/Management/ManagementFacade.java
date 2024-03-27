package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public ManagementReservationDTO rentCar(
            String customerEmail,
            LocalDateTime startRent,
            LocalDateTime endRent,
            Integer carId) {

        CarDTO carDTO = carFacade.findCarById(carId);
        CustomerDTO customerDTO = customerFacade.findCustomerByName(customerEmail);

        float totalRentCost = calculateRentalFee(startRent, endRent, carDTO.getOneDayCost());
        float foundsTotal = customerDTO.getFunds() - totalRentCost;

        if (foundsTotal < 0) {
            throw new NotEnoughFoundsException("Your account balance is insufficient by"
                    + foundsTotal + ". Please recharge your account.");
        }
        processingPayment(customerDTO, foundsTotal);


        ManagementReservation reservation = new ManagementReservation.ManagementReservationBuilder()
                .reservationId(new String(String.valueOf(LocalDate.now() + " " + customerDTO.getName())))
                .customerEmail(customerEmail)
                .carId(carId)
                .startDate(startRent)
                .endDate(endRent)
                .totalReservationCost(totalRentCost)
                .customerEmail("dawid@tdelektro.pl")
                .status(setReservationStatus(startRent, endRent, carId))
                .build();

        managementReservationRepository.save(reservation);
        ManagementReservationDTO managementReservationDTO = new ManagementReservationDTO(reservation);

        return managementReservationDTO;
    }

    public void returnCar(String customerEmail, Integer carId, String reservationId) throws DocumentException, IOException {

        ManagementReservation reservationEnd = findReservation(reservationId);
        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customerEmail);
        CarDTO carToReturn = carFacade.findCarById(carId);
        setReservationStatus(reservationEnd.getStartDate(), reservationEnd.getEndDate(), carId);
        generateInvoice(reservationEnd);
    }

    private ManagementReservation findReservation(String reservationId) {
        Optional<ManagementReservation> reservation = managementReservationRepository.findByReservationId(reservationId);
        if (reservation.isPresent()) {
            return reservation.get();
        } else {
            throw new ManagementReservationNotFound(reservationId);
        }

    }

    public float calculateRentalFee(LocalDateTime startRent, LocalDateTime endRent, float oneDayCost) {

        return ChronoUnit.DAYS.between(startRent, endRent) * oneDayCost;

    }

    public void processingPayment(CustomerDTO customerFromRepo, float founds) {

        customerFromRepo.setFunds(customerFromRepo.getFunds() - founds);

    }

    private void generateInvoice(ManagementReservation reservation) throws DocumentException, IOException {

        managementInvoice.createInvoice(reservation);

    }

    private ReservationStatus setReservationStatus(LocalDateTime startRent, LocalDateTime endRent, Integer carDtoId) {
        ReservationStatus reservationStatus;

        if (startRent.isBefore(LocalDateTime.now())) {
            reservationStatus = ReservationStatus.PENDING;
        } else if (startRent.isBefore(LocalDateTime.now()) && endRent.isAfter(LocalDateTime.now())) {
            reservationStatus = ReservationStatus.ACTIVE;
        } else {
            reservationStatus = ReservationStatus.COMPLETED;
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
        }
        Set<ManagementReservation> managementReservationSet = managementReservationRepository.findByStatus(reservationStatus);
        Set<ManagementReservationDTO> managementReservationDTOSet = new HashSet<>();

        if (managementReservationSet.isEmpty()) {
            return new HashSet<>();
        } else {
            for (ManagementReservation reservation : managementReservationSet) {
                managementReservationDTOSet.add(new ManagementReservationDTO(reservation));
            }
        }
        return managementReservationDTOSet;
    }
}
