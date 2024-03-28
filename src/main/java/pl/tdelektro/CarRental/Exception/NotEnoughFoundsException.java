package pl.tdelektro.CarRental.Exception;

public class NotEnoughFoundsException extends RuntimeException {
    public NotEnoughFoundsException(String message) {
        super(message);
    }
}
