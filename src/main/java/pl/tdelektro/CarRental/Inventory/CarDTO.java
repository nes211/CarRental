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
    private float oneDayCost;
    private boolean available;

    CarDTO() {
    }


    public CarDTO (Car car){
        this.make = car.getMake();
        this.type = car.getType();
        this.model = car.getModel();
        this.modelYear = car.getModelYear();
        this.oneDayCost = car.getOneDayCost();
        this.available = true;
    }

    Integer getId() {
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
}
