package pl.edu.pwr.chrono.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "user_id_seq")
    private Long id;

    @Column(name = "username")
    private String userName;
    private String password;
    private Boolean active = false;

    @Email
    private String email;
}
