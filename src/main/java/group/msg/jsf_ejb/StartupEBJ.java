package group.msg.jsf_ejb;

import group.msg.entities.Animal;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Singleton
@Startup
public class StartupEBJ implements Serializable {
    @PersistenceContext(unitName = "java.training")
    private EntityManager entityManager;

    @PostConstruct
    public void go () {
        System.out.println("ASD!!!");
        Animal animal = new Animal();
        entityManager.persist(animal);
    }
}
