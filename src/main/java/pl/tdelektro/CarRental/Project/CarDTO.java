package pl.tdelektro.CarRental.Project;

public class CarDTO {

    private Integer id;
    private String make;
    private String model;
    private String type;
    private int modelYear;
    private boolean available;
    public CarDTO() {
    }

    CarDTO(Integer id, String make, String model, String type, int modelYear, boolean available) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.type = type;
        this.modelYear = modelYear;
        this.available = available;
    }
}
