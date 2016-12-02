/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.labels;

import java.util.List;
import pl.clarin.chronopress.business.property.entity.Property;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface LabelsView extends ApplicationView<LabelsViewPresenter> {
    
    public static final String ID = "labels";

    void setProperties(List<Property> all);
}
