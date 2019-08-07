package group.msg.jsf_ejb;

import group.msg.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

@Stateless
public class DatabaseEJB implements Serializable {

    @PersistenceContext(name = "java.training")
    EntityManager entityManager;

    public boolean login(String username, String password) {
        String select = "SELECT u FROM User u WHERE u.username=:username and u.password=:password";

        Query query = entityManager.createQuery(select);
        query.setParameter("username", username);
        query.setParameter("password", password);

        try{
            User ua = (User) query.getSingleResult();
        }catch (NoResultException e)
        {
            return false;
        }
       return true;
    }
}
