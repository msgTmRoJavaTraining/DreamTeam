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
        User user = new User();
        user.setUsername("razvan");
        user.setPassword("c9e0b830ff18645849b8dbab57e477b5");

        User user2 = new User();
        user2.setUsername("admin");
        user2.setPassword("21232f297a57a5a743894a0e4a801fc3");

        entityManager.persist(user);
        entityManager.persist(user2);
    }
}
