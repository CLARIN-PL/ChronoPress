package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.ui.Component;

public interface CalculationResult {

    String getType();
    Component showResult();
}
