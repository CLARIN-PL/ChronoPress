package pl.clarin.chronopress.business.shered;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable, Cloneable, Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    int version;

    public boolean isPersistent() {
        return id != null;
    }
}
