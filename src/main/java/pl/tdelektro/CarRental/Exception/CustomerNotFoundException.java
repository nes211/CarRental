package pl.tdelektro.CarRental.Exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("There is no customer in repository");
    }

    public CustomerNotFoundException(String username) {
        super("Customer: " + username + " does not exists in repository, please register Yourself");
    }

    public CustomerNotFoundException(String username, String email) {
        super("Customer name " + username + " already exists in service with email address: " + email);
    }


}
