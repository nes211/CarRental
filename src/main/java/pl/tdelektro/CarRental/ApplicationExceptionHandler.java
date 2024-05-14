package pl.tdelektro.CarRental;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.tdelektro.CarRental.Exception.CarNotAvailableException;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;
import pl.tdelektro.CarRental.Exception.CustomerNotFoundException;
import pl.tdelektro.CarRental.Exception.ErrorResponse;
import pl.tdelektro.CarRental.Exception.NotEnoughFoundsException;
import pl.tdelektro.CarRental.Exception.ReservationManagementProblem;
import pl.tdelektro.CarRental.Exception.ReservationNotFoundException;

import java.util.Arrays;

@ControllerAdvice
class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            CarNotFoundException.class,
            CarNotAvailableException.class,
            CustomerNotFoundException.class,
            NotEnoughFoundsException.class,
            ReservationNotFoundException.class,
            ReservationManagementProblem.class}
    )
    public ResponseEntity<ErrorResponse> exceptionHandler(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
