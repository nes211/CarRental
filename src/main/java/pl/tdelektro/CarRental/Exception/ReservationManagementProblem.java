package pl.tdelektro.CarRental.Exception;

public class ReservationManagementProblem extends RuntimeException {
    public ReservationManagementProblem(String message){
        super(message);
    }
    public ReservationManagementProblem(){
        super("Exception reservation management problem");
    }
}
