package pl.tdelektro.CarRental.Management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class ManagementReservationDTO {


    private String reservationId;
    private Integer carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalCost;
    private String status;


    ManagementReservationDTO(String reservationId,
                             Integer carId,
                             LocalDateTime startDate,
                             LocalDateTime endDate,
                             double totalCost,
                             String status) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
    }

    ManagementReservationDTO(ManagementReservation managementReservation) {
        this.reservationId = managementReservation.getReservationId();
        this.carId = managementReservation.getCarId();
        this.startDate = managementReservation.getStartDate();
        this.endDate = managementReservation.getEndDate();
        this.totalCost = managementReservation.getTotalReservationCost();
        this.status = managementReservation.getStatus().toString();
    }
}
