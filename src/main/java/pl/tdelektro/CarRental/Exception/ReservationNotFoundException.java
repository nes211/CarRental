package pl.tdelektro.CarRental.Exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String reservationId){
        super("Reservation: " + reservationId + " does not exists in repository");
    }
    public ReservationNotFoundException(){
        super("There is no reservation in repository");
    }


}
