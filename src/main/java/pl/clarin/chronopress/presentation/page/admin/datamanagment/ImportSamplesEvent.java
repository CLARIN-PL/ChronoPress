/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.datamanagment;

class ImportSamplesEvent {
    
    private String path;

    public ImportSamplesEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
