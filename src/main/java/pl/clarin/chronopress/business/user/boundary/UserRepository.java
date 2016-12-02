package pl.clarin.chronopress.business.user.boundary;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import pl.clarin.chronopress.business.user.entity.User;

@Repository
public interface UserRepository extends EntityRepository<User, Long> {

    User findByUsernameEqual(String username);

}
