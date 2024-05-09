package pl.tdelektro.CarRental.Inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "car")
@Builder
@NoArgsConstructor
class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String make;
    private String model;
    private String type;
    @Column(unique = true, nullable = false)
    @NaturalId
    private String registration;
    private int modelYear;
    private float oneDayCost;
    private boolean isAvailable;

    Car(Integer cadId, String make, String model, String type, String registration, int modelYear, float oneDayCost, boolean isAvailable) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.registration = registration;
        this.modelYear = modelYear;
        this.oneDayCost = oneDayCost;
        this.isAvailable = isAvailable;
    }

    String getRegistration() {
        return registration;
    }

    Car setRegistration(String registration) {
        this.registration = registration;
        return this;
    }

    Car setMake(String make) {
        this.make = make;
        return this;
    }

    Car setModel(String model) {
        this.model = model;
        return this;
    }

    Car setType(String type) {
        this.type = type;
        return this;
    }

    Car setModelYear(int modelYear) {
        this.modelYear = modelYear;
        return this;
    }

    Car setOneDayCost(float oneDayCost) {
        this.oneDayCost = oneDayCost;
        return this;
    }

    Car setAvailable(boolean available) {
        isAvailable = available;
        return this;
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

    boolean isAvailable() {
        return isAvailable;
    }
}


