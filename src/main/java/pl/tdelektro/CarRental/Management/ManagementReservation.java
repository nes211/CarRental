package pl.tdelektro.CarRental.Management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ManagementReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.INTEGER)
    @JsonIgnore
    private Integer id;

    private String reservationId;
    private String customerEmail;

    private Integer carId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private float totalReservationCost;
    private ReservationStatus status ;

    ManagementReservation(String reservationId, String customerEmail, Integer carId, LocalDateTime startDate, LocalDateTime endDate, float totalReservationCost, ReservationStatus status) {
        this.reservationId = reservationId;
        this.customerEmail = customerEmail;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalReservationCost = totalReservationCost;
        this.status = status;
    }

    ReservationStatus getStatus() {
        return status;
    }

    ManagementReservation setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    String getCustomerEmail() {
        return customerEmail;
    }

    ManagementReservation setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    Integer getCarId() {
        return carId;
    }

    ManagementReservation setCarId(Integer carId) {
        this.carId = carId;
        return this;
    }

    ManagementReservation setReservationId(String reservationId) {
        this.reservationId = reservationId;
        return this;
    }

    float getTotalReservationCost() {
        return totalReservationCost;
    }

    ManagementReservation setTotalReservationCost(float totalReservationCost) {
        this.totalReservationCost = totalReservationCost;
        return this;
    }

    Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    LocalDateTime getStartDate() {
        return startDate;
    }

    ManagementReservation setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    LocalDateTime getEndDate() {
        return endDate;
    }

    ManagementReservation setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    String getReservationId() {
        return reservationId;
    }

    float getTotalCost() {
        return totalReservationCost;
    }

    ManagementReservation setTotalCost(float totalCost) {
        this.totalReservationCost = totalCost;
        return this;
    }
}
