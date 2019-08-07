package group.msg.jsf_ejb;

import group.msg.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class DatabaseEJB  {
    @PersistenceContext(name="java.training")
    EntityManager entityManager;

    @PostConstruct
    public void onStartUp()
    {
        User user=new User();
        user.setPassword("pass");
        entityManager.persist(user);
    }
}
