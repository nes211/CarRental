package pl.tdelektro.CarRental.Managment;

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
    private Long id;
    private String reservationId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerDTO customer;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarDTO car;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private float totalCost;

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

    Long getId() {
        return id;
    }

    void setId(Long id) {
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
        return totalCost;
    }

    ManagementReservation setTotalCost(float totalCost) {
        this.totalCost = totalCost;
        return this;
    }
}
