package pl.tdelektro.CarRental.Managment;

import java.util.ArrayList;
import java.util.List;

public class ManagementFacade {


    private List<ManagementReservation> reservations;

    public ManagementFacade() {
        this.reservations = new ArrayList<>();
    }

    public void addReservation(ManagementReservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(ManagementReservation reservation) {
        reservations.remove(reservation);
    }


    public void rentCar() {
        // TODO: 19.03.2024  
    }

    public void returnCar() {
        // TODO: 19.03.2024  
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
