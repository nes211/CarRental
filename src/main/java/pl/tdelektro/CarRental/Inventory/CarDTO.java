package pl.tdelektro.CarRental.Inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @JsonIgnore
    private Integer id;
    private String make;
    private String model;
    private String type;
    private String registration;
    private int modelYear;
    private float oneDayCost;
    private boolean available;


    public CarDTO (Car car){
        this.id = car.getId();
        this.make = car.getMake();
        this.type = car.getType();
        this.model = car.getModel();
        this.registration = car.getRegistration();
        this.modelYear = car.getModelYear();
        this.oneDayCost = car.getOneDayCost();
        this.available = car.isAvailable();
    }

    public String getRegistration() {
        return registration;
    }

    public Integer getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public int getModelYear() {
        return modelYear;
    }

    public float getOneDayCost() {
        return oneDayCost;
    }

    public boolean isAvailable() {
        return available;
    }


    CarDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    CarDTO setMake(String make) {
        this.make = make;
        return this;
    }

    CarDTO setModel(String model) {
        this.model = model;
        return this;
    }

    CarDTO setType(String type) {
        this.type = type;
        return this;
    }

    CarDTO setModelYear(int modelYear) {
        this.modelYear = modelYear;
        return this;
    }

    CarDTO setOneDayCost(float oneDayCost) {
        this.oneDayCost = oneDayCost;
        return this;
    }

    CarDTO setAvailable(boolean available) {
        this.available = available;
        return this;
    }

    @Override
    public String toString() {
        return
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", oneDayCost=" + oneDayCost
                ;
    }
}
