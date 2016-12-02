/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.education;

import java.util.List;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface EducationManagmenView extends ApplicationView<EducationManagmentViewPresenter> {
    
    public static final String ID = "education-managment";
    
    void showPage(EducationPage page);

    public void removePage(EducationPage page);

    public void addPage(EducationPage page);

    public void setPages(List<EducationPage> findAll);

}
