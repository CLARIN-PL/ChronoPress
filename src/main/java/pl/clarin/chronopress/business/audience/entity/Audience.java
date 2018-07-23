package pl.clarin.chronopress.business.audience.entity;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import pl.clarin.chronopress.business.shered.AbstractEntity;

@Entity
@Table(name = "audience")
@Data
public class Audience extends AbstractEntity {

    @Column(name = "name")
    private String audienceName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "audience_tiles",
            joinColumns = @JoinColumn(name = "audience_id")
    )
    private Set<String> journalTitle;

}

