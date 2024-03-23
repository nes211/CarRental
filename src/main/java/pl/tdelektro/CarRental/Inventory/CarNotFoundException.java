package pl.tdelektro.CarRental.Inventory;

class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String message){
        super(message);
    }

}
