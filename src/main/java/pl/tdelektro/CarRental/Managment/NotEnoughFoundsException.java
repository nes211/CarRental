package pl.tdelektro.CarRental.Managment;

public class NotEnoughFoundsException extends RuntimeException {
    public NotEnoughFoundsException(String message) {
        super(message);
    }
}
