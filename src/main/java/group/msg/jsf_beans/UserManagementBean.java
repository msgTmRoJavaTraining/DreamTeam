package group.msg.jsf_beans;

import group.msg.entities.User;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
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

    public void createUser(){
        StringBuilder generatedUser=new StringBuilder();

        if(this.password.equals(this.confirmPassword))
        {



        }

        LoginBean.getMd5(this.password);
        generatedUser.append(lastName.substring(4));
        generatedUser.append(firstName.charAt(0));



        User newUser=new User();



    }


}
