package pl.tdelektro.CarRental.Exception;

import java.util.function.Supplier;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(int carId){
        super("Car with id: " + carId + " does not exists in repository");
    }
    public CarNotFoundException(){
        super("There is no car in repository");
    }

    public CarNotFoundException(String carRegistration){
        super("Car with registration number: " + carRegistration + " not found in repo");
    }
}
