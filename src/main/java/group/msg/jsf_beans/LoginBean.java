package group.msg.jsf_beans;


import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Data;


import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
@Named
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;

    @Inject
    private DatabaseEJB databaseEJB;

    public String validateCredentials() {

        checkEmptyUserPass();

        String hashedPassword = getMd5(password);
        if (databaseEJB.login(username, hashedPassword)) {
            return NavigationBean.navigateTo("homePage");
        } else {
            if(!password.isEmpty() && !username.isEmpty())
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid", "Invalid username/password"));
            return NavigationBean.navigateTo("login");
        }
    }

    public void checkEmptyUserPass()
    {
        if (username.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Enter an username"));
        }
        if (password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter a password"));
        }
    }

    public String getCurrentlyLoggedInUsername() {
        return username;
    }

    static String getMd5(String input) {
        String hashtext = "";
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }
}
