package pl.tdelektro.CarRental.Management;

class NotEnoughFoundsException extends RuntimeException {
    public NotEnoughFoundsException(String message) {
        super(message);
    }
}
