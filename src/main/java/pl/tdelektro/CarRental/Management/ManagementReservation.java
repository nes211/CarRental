package pl.tdelektro.CarRental.Management;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Inventory.CarDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ManagementReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer id;

    private String reservationId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerDTO customer;

    @ManyToOne
    @JoinColumn(name = "car_dto")
    private CarDTO car;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private float totalReservationCost;

    //for management controller @ResponseBody
    private String customerEmail;
    private Integer carId;

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

    CarDTO getCar() {
        return car;
    }

    void setCar(CarDTO car) {
        this.car = car;
    }

    CustomerDTO getCustomer() {
        return customer;
    }

    void setCustomer(CustomerDTO customer) {
        this.customer = customer;
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
