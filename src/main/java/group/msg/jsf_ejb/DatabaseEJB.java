package group.msg.jsf_ejb;

import group.msg.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
@Startup
public class DatabaseEJB  {
    @PersistenceContext(name="java.training")
    EntityManager entityManager;
    @PostConstruct
    public void onStartUp()
    {
    }
}
