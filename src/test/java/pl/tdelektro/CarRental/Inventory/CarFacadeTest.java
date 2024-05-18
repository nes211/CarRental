package pl.tdelektro.CarRental.Inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tdelektro.CarRental.Exception.CarNotFoundException;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

    private Class<?> reservationStatusClassName;

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
        assertEquals(1, availableCars.size());
        verify(carRepository, times(1)).findByIsAvailableTrue();

    }

    @Test
    public void findAvailableCarsFailedTest() {

        when(carRepository.findByIsAvailableTrue()).thenReturn(Set.of());
        Set<CarDTO> availableCars = carFacade.findAvailableCars();
        assertTrue(availableCars.isEmpty());
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
            carFacade.findCarById(carId);
        } catch (Exception e) {
            assertThrows(CarNotFoundException.class, () -> carFacade.findCarById(carId));
        }
    }

    @Test
    public void saveCarStatusTest() throws ClassNotFoundException {

        when(carRepository.save(any())).thenReturn(car);
        when(carRepository.findById(carId)).thenReturn(of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        //I want to use reflection
        reservationStatusClassName = Class.forName("pl.tdelektro.CarRental.Management.ReservationStatus");
        Object[] managementStatus = reservationStatusClassName.getEnumConstants();
        assertEquals(6, managementStatus.length);

        Arrays.stream(managementStatus).forEach(status -> {
            carFacade.saveCarStatus(carId, status.toString());
            if (status.toString().equals("ACTIVE")) {
                assertFalse(car.isAvailable());
            }
            if (!status.toString().equals("ACTIVE")) {
                assertTrue(car.isAvailable());
            }
        });

    }

    @Test
    public void saveCarStatusFailedTest() throws ClassNotFoundException {

        when(carRepository.save(any())).thenReturn(car);
        when(carRepository.findById(carId)).thenReturn(of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //I want to use reflection
        reservationStatusClassName = Class.forName("pl.tdelektro.CarRental.Management.ReservationStatus");
        Object[] managementStatus = reservationStatusClassName.getEnumConstants();
        assertEquals(6, managementStatus.length);

        Arrays.stream(managementStatus).forEach(status -> {
            if (status.toString().equals("ACTIVEE")) {
                carFacade.saveCarStatus(carId, status.toString());
                fail();
            }
        });
        verify(carFacade, times(0)).saveCarStatus(any(), any());

    }

    @Test
    public void findAllCarsTest() {

        when(carRepository.findAll()).thenReturn(Set.of(car));
        Set<CarDTO> allCars = carFacade.findAllCars();
        assertNotNull(allCars);
        assertTrue(allCars.stream().anyMatch(carDTO -> carDTO.getRegistration().equals(car.getRegistration())));
        assertEquals(1, allCars.size());
    }

    @Test
    public void findAllCarsFailedTest() {

        when(carRepository.findAll()).thenReturn(Set.of());
        Set<CarDTO> allCars = carFacade.findAllCars();
        assertTrue(allCars.isEmpty());

    }

    @Test
    public void unwrapCarTest() {

        when(carRepository.existsById(any())).thenReturn(true);
        when(carRepository.findById(any())).thenReturn(of(car));
        Car carToCheck = carFacade.unwrapCar(carId);
        assertNotNull(carToCheck);
        assertEquals(carToCheck.getId(), car.getId());
        assertEquals(carToCheck.getRegistration(), car.getRegistration());

    }

    @Test
    public void unwrapCarFailedTest() {

        when(carRepository.existsById(any())).thenReturn(false);
        when(carRepository.findById(any())).thenReturn(of(car));
        assertThrows(CarNotFoundException.class, () -> carFacade.unwrapCar(carId));

    }

    @Test
    public void unwrapCarToCarDtoTest() {

        CarDTO carDTO = null;
        try {
            carDTO = carFacade.unwrapCarToCarDto(of(car), carId);
        } catch (Exception e) {
            fail();
        }
        assertNotNull(carDTO);

    }

    @Test
    public void unwrapCarToCarDtoFailedTest() {

        CarDTO carDTO = null;
        try {
            carDTO = carFacade.unwrapCarToCarDto(Optional.empty(), carId);
        } catch (Exception e) {
            assertThrows(CarNotFoundException.class, () -> carFacade.unwrapCarToCarDto(Optional.empty(), carId));
        }
        assertNull(carDTO);

    }
}