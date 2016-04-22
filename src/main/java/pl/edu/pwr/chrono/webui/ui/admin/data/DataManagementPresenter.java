package pl.edu.pwr.chrono.webui.ui.admin.data;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.UCProcessGeolocations;
import pl.edu.pwr.chrono.readmodel.UCUploadingProperNames;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

@SpringComponent
@UIScope
@Slf4j
public class  DataManagementPresenter extends Presenter<DataManagementView> {

    @Autowired
    private UCUploadingProperNames ucUploadingProperNames;

    @Autowired
    private UCProcessGeolocations processGeolocations;

    public void processUploadingProperNames(String filename){
        ucUploadingProperNames.execute(filename);
    }

    public void processGeolocations(){
        processGeolocations.process();
    }
}
