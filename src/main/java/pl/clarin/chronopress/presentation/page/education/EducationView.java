/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.education;

import java.util.List;
import pl.clarin.chronopress.business.education.entity.TitleDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface EducationView extends ApplicationView<EducationViewPresenter> {
    
    public static final String ID = "education";
    
    public void addTab(String catgeory, List<TitleDTO> list);
}
