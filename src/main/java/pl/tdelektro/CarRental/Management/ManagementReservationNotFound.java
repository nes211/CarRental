package pl.tdelektro.CarRental.Management;

class ManagementReservationNotFound extends RuntimeException{
   public ManagementReservationNotFound(String reservationIn) {
       super("Reservation with ID: " + reservationIn + "not found in repository");
   }
}
