package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.ui.Component;

public interface CalculationResult {

    String getType();
    Component showResult();

}
