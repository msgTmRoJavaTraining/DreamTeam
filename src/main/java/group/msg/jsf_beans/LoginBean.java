package group.msg.jsf_beans;


import group.msg.WebHelper;
import group.msg.entities.User;
import group.msg.entities.UserRole;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Data;
import org.primefaces.PrimeFaces;

import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Data
@Named
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    private int remainingTries = 5;
    private String lastTried;
    private User loggedUser;

    private boolean permisionManagement=false;
    private boolean userManagement=false;
    private boolean bugManagement=false;
    private boolean bugClose=false;
    private boolean bugExportPDF=false;
    private boolean userAdresant=false;


    @Inject
    private DatabaseEJB databaseEJB;

    public String validateCredentials() {

        checkEmptyUserPass();

        try {
            if (!databaseEJB.getUserByUserName(username).isActive()) {

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "User: " + username, "Is no longer active");
                PrimeFaces.current().dialog().showMessageDynamic(message);

            } else {
                if (!username.equals(lastTried))
                    remainingTries = 5;

                String hashedPassword = getMd5(password);
                if (databaseEJB.login(username, hashedPassword)) {

                    remainingTries = 5;

                    Map<String,Boolean> rig=databaseEJB.getUserRightsValue(username);
                    for (Map.Entry<String,Boolean> entry : rig.entrySet()){
                        if(entry.getKey().equals("permisionManagement")){
                            permisionManagement=entry.getValue();
                        }
                        else if(entry.getKey().equals("userManagement")){
                            userManagement=entry.getValue();
                        }
                        else if(entry.getKey().equals("bugManagement")){
                            bugManagement=entry.getValue();
                        }
                        else if(entry.getKey().equals("bugClose")){
                            bugClose=entry.getValue();
                        }
                        else if(entry.getKey().equals("bugExportPDF")){
                            bugExportPDF=entry.getValue();
                        }
                        else if(entry.getKey().equals("userAdresant")){
                            userAdresant=entry.getValue();
                        }
                    }

                    loggedUser=databaseEJB.getUserByUserName(username);


                        WebHelper.getSession().setAttribute("loggedIn",true);
                        return NavigationBean.navigateTo("homePage");
                } else {

                    if (remainingTries == 1) {
                        FacesContext.getCurrentInstance().addMessage
                                (null, new FacesMessage(FacesMessage.SEVERITY_WARN, "User " + username + " deactivated", ""));

                        User currentLoggedIn = databaseEJB.getUserByUserName(username);

                        currentLoggedIn.setActive(false);

                        databaseEJB.updateUser(currentLoggedIn);
                    } else {
                        if (!password.isEmpty() && !username.isEmpty() && remainingTries <= 5 && remainingTries > 0) {
                            lastTried = username;
                            remainingTries--;
                            FacesContext.getCurrentInstance().addMessage
                                    (null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username/password",
                                            "Number of tries left: " + remainingTries));
                        }
                    }
                }
            }

        } catch (EJBException e) {

            if (!username.isEmpty())
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username", ""));
            return NavigationBean.navigateTo("login");
        }

        return NavigationBean.navigateTo("login");
    }


    public void checkEmptyUserPass() {
        if (username.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter an username"));
        }
        if (password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Enter an username"));
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
