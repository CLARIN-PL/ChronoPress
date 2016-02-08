package pl.edu.pwr.chrono.webui.infrastructure.components;

import lombok.Data;

/**
 * Created by tnaskret on 08.02.16.
 */
@Data
public class StringBeanItem {

    private String value;

    public StringBeanItem(String value){
        this.value = value;
    }
}
