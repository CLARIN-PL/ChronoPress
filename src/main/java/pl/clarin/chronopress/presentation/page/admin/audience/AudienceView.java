/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.audience;

import java.util.List;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface AudienceView extends ApplicationView<AudienceViewPresenter> {
    
    public static final String ID = "audience";

   void setAudience(List<Audience> list);
   void swapAudience(Audience old, Audience actual);

}
