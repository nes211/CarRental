package pl.tdelektro.CarRental.Inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Builder
@Entity
@Table(name = "car")
class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String make;
    private String model;
    private String type;
    private int modelYear;
    private float oneDayCost;

    Car() {
    }
    Car(Integer id, String make, String model, String type, int modelYear,float oneDayCost) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.type = type;
        this.modelYear = modelYear;
        this.oneDayCost = oneDayCost;
    }

    Integer getId() {
        return id;
    }

    String getMake() {
        return make;
    }

    String getModel() {
        return model;
    }

    String getType() {
        return type;
    }

    int getModelYear() {
        return modelYear;
    }

    float getOneDayCost() {
        return oneDayCost;
    }
}
