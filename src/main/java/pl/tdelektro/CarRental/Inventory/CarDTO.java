package pl.tdelektro.CarRental.Inventory;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "car_dto")
@Builder
@AllArgsConstructor
public class CarDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String make;
    private String model;
    private String type;
    private int modelYear;

    private boolean available;

    CarDTO() {
    }


    CarDTO (Car car){
        this.make = car.getMake();
        this.type = car.getType();
        this.model = car.getModel();
        this.modelYear = car.getModelYear();
    }
}
