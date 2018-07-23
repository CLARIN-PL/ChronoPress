/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.datamanagment;

import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface DataManagementView extends ApplicationView<DataManagementViewPresenter> {

    static final String ID = "data-management";

    
    void uploadingSamplesFinished();
        
    void proccesingSamplesFinished();

    void setImportInProgress();

    void setProcessingInProgress();
    
    void setProcessingStatusMessage(String msg);

    void uploadingFOnemsFinished();
}
