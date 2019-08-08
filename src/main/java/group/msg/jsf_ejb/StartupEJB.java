package group.msg.jsf_ejb;

import group.msg.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class StartupEJB {
    @PersistenceContext(unitName = "java.training")
    private EntityManager entityManager;

    @PostConstruct
    public void init() {

    }
}
