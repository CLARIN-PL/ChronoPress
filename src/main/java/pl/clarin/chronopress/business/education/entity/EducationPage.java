package pl.clarin.chronopress.business.education.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class EducationPage extends Page{

    private String pageTitle;

    private boolean published;

    @Column(name = "discursive", columnDefinition = "text")
    private String discursive;
    
    @Column(name = "tabular", columnDefinition = "text")
    private String tabular;

    @Column(name = "citation", columnDefinition = "text")
    private String citation;

    @Column(name = "filename")
    private String filename;
    
}
