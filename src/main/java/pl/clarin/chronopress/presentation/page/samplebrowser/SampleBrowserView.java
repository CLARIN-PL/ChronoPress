/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.samplebrowser;

import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface SampleBrowserView extends ApplicationView<SampleBrowserViewPresenter> {
    
    public static final String ID = "sample-browser";
    
     public void showSampleWindow(Sample s);

}
