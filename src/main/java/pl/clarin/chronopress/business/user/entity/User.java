package pl.clarin.chronopress.business.user.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.clarin.chronopress.business.shered.AbstractEntity;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "pw", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    @NotNull
    private Role role;

    @Column(name = "email")
    private String email;

    public enum Role {
        MODERATOR, EDYTOR
    }

}
