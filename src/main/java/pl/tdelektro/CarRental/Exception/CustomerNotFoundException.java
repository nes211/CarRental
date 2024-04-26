package pl.tdelektro.CarRental.Exception;

import pl.tdelektro.CarRental.Customer.Customer;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(){
        super("There is no customer in repository");
    }
    public CustomerNotFoundException(String username){
        super("Customer: " + username + " does not exists in repository, please register Yourself");
    }
    public CustomerNotFoundException(String username, String message){
        super("Customer name " +username + " already exists in service with email address: " + message);
    }


}
