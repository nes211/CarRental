package pl.tdelektro.CarRental.Management;

import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


public class ManagementControllerTest2 {
    @Mock
    private ManagementFacade managementFacade;
    @InjectMocks
    ManagementController managementController;

 @Before
    public void warmup(){
     MockitoAnnotations.initMocks(this);
 }

}
