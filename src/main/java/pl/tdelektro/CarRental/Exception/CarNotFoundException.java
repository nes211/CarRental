package pl.tdelektro.CarRental.Exception;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(int carId){
        super("Car with id: " + carId + " does not exists in repository");
    }

}
