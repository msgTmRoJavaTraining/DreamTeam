package group.msg.jsf_beans;

import lombok.Data;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@RequestScoped
@Named
@Data
public class RequestScopeTestBackingBean {
    private String savedData;
    private String outputMessage = "Empty";

    public void respondToAjaxRequest() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hello", "This is just to test AJAX functionality..."));
    }

    public void saveInput() {
        outputMessage = savedData;
    }


}
