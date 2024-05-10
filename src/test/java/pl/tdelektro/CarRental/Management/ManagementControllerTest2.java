package pl.tdelektro.CarRental.Management;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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
