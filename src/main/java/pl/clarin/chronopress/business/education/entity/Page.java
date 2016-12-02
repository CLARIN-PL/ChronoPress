package pl.clarin.chronopress.business.education.entity;

import javax.persistence.*;
import lombok.Data;
import pl.clarin.chronopress.business.shered.AbstractEntity;

@Entity
@Table(name = "Pages")
@DiscriminatorColumn(
		name="dtype",
		discriminatorType = DiscriminatorType.STRING)
@Data
public class Page extends AbstractEntity{

    private String category;

}
