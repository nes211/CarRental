package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public ManagementReservationDTO rentCar(String customerEmail, LocalDateTime startRent, LocalDateTime endRent, Integer carId) {

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
                .car(carDTO)
                .startDate(startRent)
                .endDate(endRent)
                .totalReservationCost(totalRentCost)
                .customerEmail("dawid@tdelektro.pl")
                .build();

        ManagementReservation managementReservation = new ManagementReservation();
        ManagementReservationDTO managementReservationDTO = new ManagementReservationDTO(    reservation.getReservationId(),
                carDTO,
                customerDTO,
                startRent,
                endRent,
                totalRentCost);

        //managementReservationRepository.save(reservation);

        return managementReservationDTO;
    }

    public void returnCar(CustomerDTO customer, Integer carId, ManagementReservation reservation) throws DocumentException, FileNotFoundException {

        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customer.getName());
        CarDTO carToReturn = carFacade.findCarById(carId);
        generateInvoice(reservation);

    }

    public float calculateRentalFee(LocalDateTime startRent, LocalDateTime endRent, float oneDayCost) {

        return ChronoUnit.DAYS.between(startRent, endRent) * oneDayCost;

    }

    public void processingPayment(CustomerDTO customerFromRepo, float founds) {

        customerFromRepo.setFunds(customerFromRepo.getFunds() - founds);

    }

    private void generateInvoice(ManagementReservation reservation) throws DocumentException, FileNotFoundException {

        managementInvoice.createInvoice(reservation);

    }


}
