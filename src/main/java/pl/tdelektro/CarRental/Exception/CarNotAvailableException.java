package pl.tdelektro.CarRental.Exception;

import java.time.LocalDateTime;

public class CarNotAvailableException extends RuntimeException {

    public CarNotAvailableException(int carId){
        super("Car is not available");
    }
    public CarNotAvailableException(int carId, LocalDateTime startDate, LocalDateTime endDate){
        super("Car with id : " + carId + " is not available from: " + startDate + " till: " + endDate);
    }
    public CarNotAvailableException(){
        super("Car is not available");
    }

}
