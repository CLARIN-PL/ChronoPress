/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.propernames;

import java.util.List;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;


public interface ProperNameView extends ApplicationView<ProperNameViewPresenter> {
    
    public static final String ID = "propernames";

    void setProperNames(List<ProperName> all);
    void showProperNameWindow(ProperName name);
}
