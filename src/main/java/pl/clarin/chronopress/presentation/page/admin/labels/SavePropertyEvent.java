/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.labels;

import pl.clarin.chronopress.business.property.entity.Property;


public class SavePropertyEvent {
    
    private final Property property;

    public SavePropertyEvent(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
  
}
