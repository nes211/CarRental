package pl.tdelektro.CarRental.Managment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Customer.CustomerFacade;
import pl.tdelektro.CarRental.Inventory.CarDTO;
import pl.tdelektro.CarRental.Inventory.CarFacade;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ManagementFacade {


    private List<ManagementReservation> reservations;
    private CarFacade carFacade;
    private CustomerFacade customerFacade;


    public void addReservation(ManagementReservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(ManagementReservation reservation) {
        reservations.remove(reservation);
    }


    public void rentCar(String customerEmail, LocalDateTime startRent, LocalDateTime endRent, Integer carId) {
        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customerEmail);
        CarDTO carToRent = carFacade.findCarById(carId);
        calculateRentalFee();
        processingPayment();




    }

    public void returnCar(String customerEmail, Integer carId) {

        CustomerDTO customerFromRepo = customerFacade.findCustomerByName(customerEmail);
        CarDTO carToReturn = carFacade.findCarById(carId);

        generateInvoice();


    }

    public void calculateRentalFee() {
        // TODO: 19.03.2024  
    }

    public void processingPayment() {
        // TODO: 19.03.2024  
    }

    private void generateInvoice() {
        // TODO: 19.03.2024  
    }
}
