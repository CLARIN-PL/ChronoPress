/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.datamanagment;

public class ProcessSamplesStatusEvent {

    private String message;

    public ProcessSamplesStatusEvent(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
