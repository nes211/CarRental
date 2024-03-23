package pl.tdelektro.CarRental.Managment;

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

    private List<ManagementReservation> reservations;
    private ManagementReservationRepository managementReservationRepository;
    private CarFacade carFacade;
    private CustomerFacade customerFacade;
    private ManagementInvoice managementInvoice;

    public void addReservation(ManagementReservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(ManagementReservation reservation) {
        reservations.remove(reservation);
    }

    public void rentCar(CustomerDTO customerDTO, LocalDateTime startRent, LocalDateTime endRent, Integer carId) {

        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customerDTO.getName());
        CarDTO carToRent = carFacade.findCarById(carId);

        float customerFounds = calculateRentalFee(startRent, endRent, carToRent.getOneDayCost());
        float foundsTotal = customerFromRepo.getFunds() - customerFounds;

        if (foundsTotal < 0) {
            throw new NotEnoughFoundsException("Your account balance is insufficient by"
                    + foundsTotal + ". Please recharge your account.");
        }
        processingPayment(customerFromRepo, foundsTotal);
        managementReservationRepository.save(new ManagementReservation.ManagementReservationBuilder()
                .reservationId(new String(String.valueOf(LocalDate.now() + " " + customerDTO.getName())))
                .customer(customerFromRepo)
                .car(carToRent)
                .startDate(startRent)
                .endDate(endRent)
                .totalCost(foundsTotal)
                .build()
        );
    }

    public void returnCar(CustomerDTO customer, Integer carId, ManagementReservation reservationId) throws DocumentException, FileNotFoundException {

        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customer.getName());
        CarDTO carToReturn = carFacade.findCarById(carId);
        generateInvoice(reservationId);

    }

    public float calculateRentalFee(LocalDateTime startRent, LocalDateTime endRent, float oneDayCost) {

        return ChronoUnit.DAYS.between(startRent, endRent) * oneDayCost;

    }

    public void processingPayment(CustomerDTO customerFromRepo, float founds) {

        customerFromRepo.setFunds(customerFromRepo.getFunds() - founds);

    }

    private void generateInvoice(ManagementReservation managementReservation) throws DocumentException, FileNotFoundException {

        managementInvoice.createInvoice(managementReservation);

    }
}
