package pl.tdelektro.CarRental.Managment;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

class Management {



    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime localDateTime;


}
