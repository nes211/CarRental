package pl.tdelektro.CarRental.Management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.tdelektro.CarRental.Customer.CustomerDTO;
import pl.tdelektro.CarRental.Inventory.CarDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "management_dto")
@NoArgsConstructor
@Getter
@Setter
public class ManagementReservationDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer id;
    private String reservationId;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarDTO car;
    @ManyToOne
    @JoinColumn(name = "customer_dto_id")
    private CustomerDTO customerDTO;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private float totalCost;


    ManagementReservationDTO(String reservationId,
                             CarDTO car,
                             CustomerDTO customerDTO,
                             LocalDateTime startDate,
                             LocalDateTime endDate,
                             float totalCost) {
        this.reservationId = reservationId;
        this.car = car;
        this.customerDTO = customerDTO;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }


}
