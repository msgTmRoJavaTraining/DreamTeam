package group.msg.jsf_beans;

import lombok.Data;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Data
public class AjaxExampleBackingBean implements Serializable {
    private String firstNameAndLastName;
    private String outputMessage;

    public void addUserToDatabase() {
        if (isNameValid(firstNameAndLastName)) {
            outputMessage = "User added!";
        } else {
            outputMessage = "Name invalid.";
        }
    }

    private boolean isNameValid(String name) {
        //validate the name, for the purpose of this example this is enough
        return name.split(" ").length > 1;
    }
}
