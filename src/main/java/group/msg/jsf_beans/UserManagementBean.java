package group.msg.jsf_beans;

import group.msg.entities.PersonalInfo;
import group.msg.entities.User;
import group.msg.exceptions.UserCreatorException;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
    private String username;
    private String password;
    private String confirmPassword;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;

    private String selectedRole;
    private List<String> rolesTest;
    private List<String> rightsTest;
    private String[] selectedRights;

    private List<String> testUsers;

    @Inject
    DatabaseEJB dataBaseEJB;

public void test() {
    System.out.println("ceva");
}
    @PostConstruct
    public void init() {
        rolesTest = new ArrayList<>();
        rolesTest.add("role1");
        rolesTest.add("role2");
        rolesTest.add("role3");
        rolesTest.add("role4");
        rolesTest.add("role5");
        rightsTest = new ArrayList<>();
        rightsTest.add("right1");
        rightsTest.add("right2");
        rightsTest.add("right3");
        rightsTest.add("right4");
        rightsTest.add("right5");
        rightsTest.add("right6");

        testUsers= new ArrayList<>();
        UserManagementBean user1 = new UserManagementBean();
        UserManagementBean user2 = new UserManagementBean();
        user1.setUsername("user1");
        user1.setFirstName("uuu");
        user1.setLastName("lll");
        user1.setEmail("something@msg.group.com");
        user1.setPassword("admin");
        user1.setConfirmPassword("admin");
        user1.setMobile("07239322");

        user2.setUsername("user2");
        user2.setFirstName("uuu");
        user2.setLastName("lll");
        user2.setEmail("something@msg.group.com");
        user2.setPassword("admin");
        user2.setConfirmPassword("admin");
        user2.setMobile("07239322");
        testUsers.add(user1.username);
        testUsers.add(user2.username);

    }


    public void createUser(){
        int firstNameChars=1;
        int lastNameChars=5;
        StringBuilder generatedUserName=new StringBuilder();

        if(this.password.equals(this.confirmPassword))
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

                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                userInfo.setEmail(email);
                userInfo.setMobile(mobile);
                userInfo.setActive(true);

                newUser.setUsername(generatedUserName.toString().toLowerCase());
                newUser.setPassword(LoginBean.getMd5(this.password));
                newUser.setPersonalInformations(userInfo);

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


}
