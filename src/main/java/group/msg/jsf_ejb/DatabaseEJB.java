package group.msg.jsf_ejb;

import group.msg.entities.Bug;
import group.msg.entities.Rights;
import group.msg.entities.User;
import group.msg.entities.UserRole;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class DatabaseEJB implements Serializable {

    @PersistenceContext(name = "java.training")
    EntityManager entityManager;

    public boolean login(String username, String password) {
        String select = "SELECT u FROM User u WHERE u.username=:username and u.password=:password";
        Query query = entityManager.createQuery(select);
        query.setParameter("username", username);
        query.setParameter("password", password);

        try {
            User ua = (User) query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public boolean existsUserWithId(String user) {
        String select = "SELECT u FROM User u WHERE u.username=:username";
        Query query = entityManager.createQuery(select);
        query.setParameter("username", user);
        try {
            User searchedUser = (User) query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;


    }

    public void createUser(User newUser) {
        entityManager.persist(newUser);
    }

    public void createRole(UserRole newRole) {

        entityManager.persist(newRole);
    }

    public List<String> getAllUserNames() {
        List<String> allUserNames;
        Query query = entityManager.createQuery("SELECT users.username From User users");
        allUserNames = query.getResultList();
        return allUserNames;
    }

    public List<User> getAllUsers() {
        List<User> allUsers;
        Query query = entityManager.createQuery("SELECT users From User users");
        allUsers = query.getResultList();
        return allUsers;
    }

    public List<String> getAllActiveUserNames() {
        List<String> allUserNames;
        Query query = entityManager.createQuery("SELECT users.username From User users where users.isActive=true");
        allUserNames = query.getResultList();
        return allUserNames;
    }

    public User getUserById(int seachedId) {
        User searchedUser;
        searchedUser = entityManager.find(User.class, seachedId);
        return searchedUser;
    }

    public User getUserByUserName(String username) {
        User foundUser;
        Query query = entityManager.createQuery("SELECT user FROM User user WHERE user.username=:username");
        query.setParameter("username", username);

        foundUser = (User) query.getSingleResult();
        return foundUser;
    }

    public void updateUser(User toUpdate) {
        entityManager.merge(toUpdate);
    }

    public void createBug(Bug newBug) {
        entityManager.persist(newBug);
    }

    public List<String> getRoles() {
        List<String> roleList;
        Query query = entityManager.createQuery("SELECT role.roleName FROM UserRole role ");

        roleList = (List<String>) query.getResultList();
        return roleList;
    }

    public List<String> getRights() {
        List<String> rightsList;
        Query query = entityManager.createQuery("SELECT right.name FROM Rights right");

        rightsList = (List<String>) query.getResultList();
        return rightsList;
    }

    public UserRole getRoleByName(String selectedRole) {
        UserRole result;
        Query query = entityManager.createQuery("SELECT role FROM UserRole role WHERE role.roleName=:selectedRole");
        query.setParameter("selectedRole", selectedRole);
        result = (UserRole) query.getSingleResult();
        return result;
    }

    public void updateRole(UserRole resultRole) {
        entityManager.merge(resultRole);
    }

    public List<UserRole> getRolesByName(List<String> userRoleList) {
        List<UserRole> roles = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT role FROM UserRole role WHERE role.roleName=:roleName");
        for (String roleName : userRoleList) {
            query.setParameter("roleName", roleName);
            roles.add((UserRole) query.getSingleResult());
        }
        return roles;
    }

    public Rights getRightByName(String rightName) {
        Rights result;
        Query query = entityManager.createQuery("SELECT right FROM Rights right WHERE right.name=:rightName");
        query.setParameter("rightName", rightName);
        result = (Rights) query.getSingleResult();
        return result;
    }
    public Map<String,Boolean> getUserRightsValue(String username){
        Map<String,Boolean> rights=new HashMap<String,Boolean>();
                            rights.put("permisionManagement",false);
                            rights.put("userManagement",false);
                            rights.put("bugManagement",false);
                            rights.put("bugClose",false);
                            rights.put("bugExportPDF",false);
                            rights.put("userAdresant",false);
        User loggedUser=getUserByUserName(username);
            List<UserRole> roles=loggedUser.getRoles();

        for (UserRole cRole:roles) {
            List<Rights>roleRights=cRole.getRights();

                for(Rights cRight:roleRights){
                    if(cRight.getName().equals("PERMISSION-MANAGEMENT")){
                        rights.put("permisionManagement",true);
                    }
                   else if(cRight.getName().equals("USER-MANAGEMENT")){
                        rights.put("userManagement",true);
                    }
                   else if(cRight.getName().equals("BUG-MANAGEMENT")){
                        rights.put("bugManagement",true);
                    }
                   else if(cRight.getName().equals("BUG-CLOSE")){
                        rights.put("bugClose",true);
                    }
                   else if(cRight.getName().equals("BUG-EXPORT-PDF")){
                        rights.put("bugExportPDF",true);
                    }
                   else if(cRight.getName().equals("USER-ADRESANT")){
                        rights.put("userAdresant",true);
                    }
                }
            }
        return rights;
    }
    public List<Bug> getAllBugs() {
        List<Bug> result;
        Query query = entityManager.createQuery("SELECT bug FROM Bug bug ");
        result = (List<Bug>) query.getResultList();
        return result;
    }
}
