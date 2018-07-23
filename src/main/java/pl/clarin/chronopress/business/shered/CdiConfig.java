package pl.clarin.chronopress.business.shered;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CdiConfig {

    @Produces
    @Dependent
    @PersistenceContext(unitName = "pu")
    public EntityManager entityManager;

}
