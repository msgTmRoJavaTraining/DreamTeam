package group.msg.jsf_ejb;

import group.msg.entities.Bug;
import group.msg.entities.User;
import group.msg.entities.UserRole;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public boolean existsUserWithId(String user){
        String select="SELECT u FROM User u WHERE u.username=:username";
        Query query =entityManager.createQuery(select);
        query.setParameter("username",user);
        try{
            User searchedUser= (User) query.getSingleResult();
        }catch (NoResultException e)
        {
            return false;
        }
        return true;
}
    public void createUser(User newUser){
        entityManager.persist(newUser);
    }

    public void createRole(UserRole newRole){
        entityManager.persist(newRole);
    }
    public List<String> getAllUsers(){
        List<String> allUsers=new ArrayList<>();
        Query query=entityManager.createQuery("SELECT users.username From User users");
        allUsers=query.getResultList();
        return allUsers;
    }
    public User getUserByUserName(String username){
        User foundUser;
        Query query=entityManager.createQuery("SELECT user FROM User user WHERE user.username=:username");
        query.setParameter("username",username);

            foundUser= (User) query.getSingleResult();
        return foundUser;
    }

    public void updateUser(User toUpdate){
        entityManager.merge(toUpdate);
    }

    public void createBug(Bug newBug) {

        entityManager.persist(newBug);
    }
}
