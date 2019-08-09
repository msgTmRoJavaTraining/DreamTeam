package group.msg.jsf_beans;

import group.msg.entities.PersonalInfo;
import group.msg.entities.User;
import group.msg.entities.UserRole;
import group.msg.exceptions.UserCreatorException;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
public class UserManagementBean implements Serializable {
    User userToUpdate;
    PersonalInfo newPersonalInfo;

    private String username;
    private String password;
    private String confirmPassword;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private boolean active;
    private List<String> userRoleList;


    private List<String> testUsers;

    @Inject
    DatabaseEJB dataBaseEJB;


    @PostConstruct
    public void init() {

    }
    public void createUser(){
        int firstNameChars=1;
        int lastNameChars=5;
        StringBuilder generatedUserName=new StringBuilder();

        if(invalidCredentials())
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "User not added"));

        if(this.password.equals(this.confirmPassword)&& isEmailValid(email)&& isValidPhoneNumber(mobile))
        {
            if(lastName.length()<5){
                lastNameChars=lastName.length();
                firstNameChars=6-lastNameChars;
            }
            while(lastNameChars!=0) {
                generatedUserName.append(lastName.substring(0, lastNameChars));
                generatedUserName.append(firstName.substring(0, firstNameChars));
                if (dataBaseEJB.existsUserWithId(generatedUserName.toString())) {
                    lastNameChars--;
                    firstNameChars++;
                    generatedUserName.setLength(0);
                } else break;
            }
            if(generatedUserName.length()==0){
                throw new UserCreatorException(lastName,firstName);
            }else{
                User newUser=new User();
                PersonalInfo userInfo=new PersonalInfo();
                ///
                List<UserRole> userRoles;
                userRoles=dataBaseEJB.getRolesByName(userRoleList);
                ///
                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                userInfo.setEmail(email);
                userInfo.setMobile(mobile);
                userInfo.setActive(true);

                newUser.setUsername(generatedUserName.toString().toLowerCase());
                newUser.setPassword(LoginBean.getMd5(this.password));
               ////
                newUser.setRoles(userRoles);
               ///
                newUser.setPersonalInformations(userInfo);
               

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "User added successfully"));

                dataBaseEJB.createUser(newUser);

            }
        }
        username="";
        password="";
        confirmPassword="";
        firstName="";
        lastName="";
        mobile="";
        email="";
    }

    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@msggroup.com";
        return email.matches(regex);
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regex = "\\+407\\d{8}";
        String regex1 = "07\\d{8}";
        String regex7 = "00407\\d{8}";
        String regex2 = "\\+4915\\d{9}";
        String regex3 = "\\+4916\\d{8}\\(\\d\\)";
        String regex4 = "\\+4917\\d{8}\\(\\d\\)";
        String regex5 = "004915\\d{9}";
        String regex6 = "\\+4915\\d{7}";

        return mobile.matches(regex) || mobile.matches(regex1) || mobile.matches(regex2) || mobile.matches(regex6)
                || mobile.matches(regex3) || mobile.matches(regex4) || mobile.matches(regex5) || mobile.matches(regex7);
    }

    public List<User> usersList(){
        return dataBaseEJB.getAllUsers();
    }
    public List<String> userNamesList(){
        return dataBaseEJB.getAllUserNames();
    }

    public void prepareForUserUpdate()
    {
        userToUpdate=dataBaseEJB.getUserByUserName(this.username);
        newPersonalInfo=userToUpdate.getPersonalInformations();
        this.firstName=newPersonalInfo.getFirstName();
        this.lastName=newPersonalInfo.getLastName();
        this.mobile=newPersonalInfo.getMobile();
        this.email=newPersonalInfo.getEmail();
        this.active=newPersonalInfo.isActive();
    }

    public void userUpdate(){
        if(this.password.equals(this.confirmPassword) && isEmailValid(email)&& isValidPhoneNumber(mobile)){
            newPersonalInfo.setFirstName(this.firstName);
            newPersonalInfo.setLastName(this.lastName);
            newPersonalInfo.setMobile(this.mobile);
            newPersonalInfo.setEmail(this.email);
            newPersonalInfo.setActive(this.active);
            userToUpdate.setPersonalInformations(newPersonalInfo);
            userToUpdate.setPassword(LoginBean.getMd5(this.password));
            dataBaseEJB.updateUser(userToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "User updated successfully"));
        }else{
            if(invalidCredentials())
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "User not updated"));
        }
    }

    public boolean invalidCredentials()
    {
        boolean ok=false;

        if(!isEmailValid(email)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Invalid email"));
            ok=true;
        }
        if(!isValidPhoneNumber(mobile)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Invalid phone number"));
            ok=true;
        }
        return ok;
    }
}
