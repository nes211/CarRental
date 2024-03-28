package pl.tdelektro.CarRental.Exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String username){
        super("Customer: " + username + " does not exists in repository, please register Yourself");
    }
    public CustomerNotFoundException(){
        super("There is no customer in repository");
    }


}
