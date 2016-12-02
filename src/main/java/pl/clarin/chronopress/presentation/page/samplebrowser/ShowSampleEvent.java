/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.samplebrowser;

import pl.clarin.chronopress.business.sample.entity.Sample;

public class ShowSampleEvent {

     private final Sample sample;

    public ShowSampleEvent(final Sample sample) {
        this.sample = sample;
    }

    public Sample getSample() {
        return sample;
    }
        
}
