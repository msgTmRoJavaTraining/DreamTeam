package group.msg.jsf_beans;

import group.msg.entities.PersonalInfo;
import group.msg.entities.User;
import group.msg.exceptions.UserCreatorException;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class UserManagementBean implements Serializable {
    private String username;
    private String password;
    private String confirmPassword;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;

    @Inject
    DatabaseEJB dataBaseEJB;

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

                newUser.setUsername(generatedUserName.toString());
                newUser.setPassword(LoginBean.getMd5(this.password));
                newUser.setPersonalInformations(userInfo);

               dataBaseEJB.createUser(newUser);
            }

        }
    }


}
