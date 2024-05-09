package pl.tdelektro.CarRental.Inventory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarFacadeIntegrationTest {

    @Autowired
    private CarFacade carFacade;
    @Autowired
    private CarRepository carRepository;

    private final Car testCar1 = new Car.CarBuilder()
            .make("Test")
            .model("Test")
            .type("test")
            .modelYear(1900)
            .registration("test1")
            .oneDayCost(1f)
            .isAvailable(true)
            .build();
    private final Car testCar2 = new Car.CarBuilder()
            .make("Test2")
            .model("Test2")
            .type("test")
            .modelYear(1800)
            .registration("test2")
            .oneDayCost(1f)
            .isAvailable(false)
            .build();

    @Test
    @Order(0)
    public void addNewCarTest() {
        Set<Car> carSet = carRepository.findAll();
        carFacade.addNewCar(testCar1);
        assertEquals(carSet.size() + 1, carRepository.findAll().size());
        assertTrue(carRepository.findAll().stream()
                .anyMatch(car -> car.getRegistration().matches(testCar1.getRegistration())));

        carFacade.addNewCar(testCar2);
        assertEquals(carSet.size() + 2, carRepository.findAll().size());
        assertTrue(carRepository.existsByRegistration(testCar2.getRegistration()));
    }

    @Test
    @Order(1)
    public void findAvailableCarsTest() {
        Set<CarDTO> carSet = carFacade.findAvailableCars();
        assertFalse(carSet.isEmpty());
        assertTrue(carSet.stream().allMatch(CarDTO::isAvailable));
        assertTrue(carRepository.existsByRegistration(testCar1.getRegistration()));
        assertTrue(carRepository.existsByRegistration(testCar2.getRegistration()));
    }

    @Test
    @Order(2)
    public void findCarByIdTest() {
        Set<Car> carSet = carRepository.findAll();

        carSet.stream().forEach(car -> assertEquals(carFacade.findCarById(car.getId()).getId(), car.getId()));
        assertThrows(RuntimeException.class, () -> carFacade.findCarById(-1));
    }

    @Test
    @Order(3)
    public void saveCarStatusTest() {
        Set<Car> carSet = carRepository.findAll();
        carSet.stream().anyMatch(car -> {
            if (car.getRegistration().equals(testCar1.getRegistration())) {

                //managementStatus declaration to set isAvailable status to opposite
                String managementStatus = car.isAvailable() ? "ACTIVE" : "INACTIVE";
                carFacade.saveCarStatus(car.getId(), managementStatus);
                assertNotEquals(car.isAvailable(), carRepository.findByRegistration(car.getRegistration()).isAvailable());
            } else if (car.getRegistration().equals(testCar2.getRegistration())) {

                //FOR TESTS - Opposite status declaration
                String status = car.isAvailable() ? "ACTIVE" : "INACTIVE";
                carFacade.saveCarStatus(car.getId(), status);
                assertNotEquals(
                        car.isAvailable(),
                        carRepository.findByRegistration(car.getRegistration()).isAvailable()
                );
            }
            return false;
        });

        assertNotEquals(
                carRepository.findByRegistration(testCar1.getRegistration()).isAvailable(),
                carRepository.findByRegistration(testCar2.getRegistration()).isAvailable()
        );
    }

    @Test
    @Order(4)
    public void findAllCarsTest() {
        Set<CarDTO> carDtoSet = carFacade.findAllCars();
        Set<Car> carSet = carRepository.findAll();

        assertEquals(carSet.size(), carDtoSet.size());

        assertTrue(
                carDtoSet.stream().allMatch(carDtoSetItem -> carSet.stream().anyMatch(carSetItem -> {
                    carSetItem.getRegistration().equals(carDtoSetItem.getRegistration());
                    return true;
                }))
        );
    }

    @Test
    @Order(5)
    public void removeCarTest() {

        assertTrue(carRepository.findAll().stream()
                .anyMatch(car -> {
                            if (car.getRegistration().equals(testCar1.getRegistration())) {
                                carFacade.removeCar(car.getId());
                                return true;
                            } else {
                                return false;
                            }
                        }
                ));
        assertTrue(carRepository.findAll().stream()
                .anyMatch(car -> {
                            if (car.getRegistration().equals(testCar2.getRegistration())) {
                                carFacade.removeCar(car.getId());
                                return true;
                            } else {
                                return false;
                            }
                        }
                ));

    }
}