package group.msg.jsf_ejb;

import group.msg.entities.Bug;
import group.msg.entities.Rights;
import group.msg.entities.User;
import group.msg.entities.UserRole;

import javax.ejb.Stateless;
import javax.jws.soap.SOAPBinding;
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

    public List<String> getAllUserNames(){
        List<String> allUserNames;
        Query query=entityManager.createQuery("SELECT users.username From User users");
        allUserNames=query.getResultList();
        return allUserNames;
    }
    public List <User> getAllUsers(){
        List<User> allUsers;
        Query query=entityManager.createQuery("SELECT users From User users");
        allUsers=query.getResultList();
        return allUsers;
    }
    public User getUserById(int seachedId){
        User searchedUser;
        searchedUser=entityManager.find(User.class,seachedId);
        return searchedUser;
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
    public List<String> getRoles(){
        List<String> roleList;
        Query query= entityManager.createQuery("SELECT role.roleName FROM UserRole role ");

        roleList= (List<String>) query.getResultList();
        return roleList;
    }
    public List<String> getRigts(){
        List<String> rightsList;
        Query query= entityManager.createQuery("SELECT right.name FROM Rights right");

        rightsList= (List<String>) query.getResultList();
        return rightsList;
    }
    public int getRoleIdByName(String selectedRole){
        int result;
        Query query= entityManager.createQuery("SELECT role.roleId FROM UserRole role WHERE role.roleName=:selectedRole");
        query.setParameter("selectedRole",selectedRole);
        result= (int)query.getSingleResult();
        return result;
    }
    public int getRightIdByName(String rightName){
        int result;
        Query query= entityManager.createQuery("SELECT right.rightId FROM Rights right WHERE right.name=:rightName");
        query.setParameter("rightName",rightName);
        result= (int)query.getSingleResult();
        return result;
    }
    public void updateRole( UserRole resultRole){

    }

    public List<UserRole> getRolesByName(List<String> userRoleList) {
       List<UserRole>roles=new ArrayList<>();
       Query query=entityManager.createQuery("SELECT role FROM UserRole role WHERE role.roleName=:roleName");
        for (String roleName:userRoleList) {
            query.setParameter("roleName",roleName);
            roles.add((UserRole) query.getSingleResult());
        }
        return roles;
    }
}
