package pl.tdelektro.CarRental.Managment;

import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Inventory.CarDTO;

import java.util.Date;

class ManagementReservation {

    private int reservationId;
    private CustomerDTO customer;
    private CarDTO car;
    private Date startDate;
    private Date endDate;
}
