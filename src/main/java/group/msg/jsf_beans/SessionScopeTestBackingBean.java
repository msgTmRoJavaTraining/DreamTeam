package group.msg.jsf_beans;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
@Data
public class SessionScopeTestBackingBean implements Serializable {
    private String savedData ;
    private String outputMessage = "Empty";

    public void respondToAjaxRequest(){
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Hello","This is just to test AJAX functionality..."));
    }

    public void saveInput(){
        outputMessage = savedData;
    }
}
