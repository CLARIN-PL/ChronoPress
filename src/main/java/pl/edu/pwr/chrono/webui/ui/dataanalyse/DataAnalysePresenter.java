package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.google.common.collect.Lists;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.UCDataSelection;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;

/**
 * Created by tnaskret on 10.01.16.
 */
@SpringComponent
@UIScope
public class DataAnalysePresenter extends Presenter<DataAnalyseView> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataAnalysePresenter.class);

    @Autowired
    private UCDataSelection ucDataSelection;

    public void acceptDataSelection(){
        DataSelectionDTO dto = new DataSelectionDTO();
        List<String> authors= Lists.newArrayList();
        authors.add("%Adam Ochocki%");
        dto.setAuthors(authors);

        DataSelectionResult result =  ucDataSelection.search(dto);
        System.out.println(result);
    }
}
