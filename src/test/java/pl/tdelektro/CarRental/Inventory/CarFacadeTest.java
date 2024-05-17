package pl.tdelektro.CarRental.Inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarFacadeTest {


    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarFacade carFacade;
    private final int carId = 1;
    private final String make = "Test";
    private final String model = "Test";
    private final String type = "Test";
    private final String registration = "Test";
    private final int modelYear = 2000;
    private final float oneDayCost = 2;
    private final boolean isAvailable = true;

    Car car = new Car(
            carId,
            make,
            model,
            type,
            registration,
            modelYear,
            oneDayCost,
            isAvailable);

    @Before
    public void warmup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addNewCarTest() {

        assertTrue(carFacade.addNewCar(car));
        verify(carRepository, times(1)).save(car);

    }

    @Test
    public void findAvailableCarsTest() {

        when(carRepository.findByIsAvailableTrue()).thenReturn(Set.of(car));
        Set<CarDTO> availableCars = carFacade.findAvailableCars();
        assertTrue(availableCars.size() == 1);
        verify(carRepository, times(1)).findByIsAvailableTrue();

    }

    @Test
    public void findAvailableCarsFailedTest() {

        when(carRepository.findByIsAvailableTrue()).thenReturn(Set.of());
        Set<CarDTO> availableCars = carFacade.findAvailableCars();
        assertTrue(availableCars.size() == 0);
        verify(carRepository, times(1)).findByIsAvailableTrue();

    }

    @Test
    public void findCarByIdTest() {

        when(carRepository.findById(any(Integer.class))).thenReturn(of(car));
        CarDTO carById = carFacade.findCarById(carId);
        assertTrue(carById.isAvailable());

    }
    @Test
    public void findCarByIdFailedTest() {

        when(carRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        try {
            CarDTO carById = carFacade.findCarById(carId);
        } catch (Exception e) {
            assertThrows(CarNotFoundException.class, ()-> carFacade.findCarById(carId));
        }
    }

    @Test
    public void saveCarStatus(){

        boolean initialStatus = car.isAvailable();
        when(carRepository.save(any())).thenReturn(car);
        when(carRepository.findById(carId)).thenReturn(of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        String status = "ACTIVE";
        carFacade.saveCarStatus(carId, status);
        assertNotEquals(car.isAvailable(), initialStatus);

    }



}












