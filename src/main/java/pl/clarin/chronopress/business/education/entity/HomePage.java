package pl.clarin.chronopress.business.education.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class HomePage extends Page {

    @Column(name = "content", columnDefinition = "text")
    private String content;

}
