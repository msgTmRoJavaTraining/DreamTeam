package group.msg.jsf_beans;

import group.msg.entities.Rights;
import group.msg.entities.UserRole;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
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
public class RoleManagementBean implements Serializable {

    private List<String> roleList;
    private List<String> rightsList;
    private String selectedRole;
    private List<String> selectedRights;

    @Inject
    private DatabaseEJB databaseEJB;

    @PostConstruct
    public void init() {
        roleList = databaseEJB.getRoles();
        rightsList = databaseEJB.getRights();
    }

    public void updateRoles() {
        List<Rights> resultRights = new ArrayList<>();
        UserRole resultRole;
        resultRole = databaseEJB.getRoleByName(selectedRole);
        for (String rightName : selectedRights) {
            Rights crtRight;
            crtRight = databaseEJB.getRightByName(rightName);
            resultRights.add(crtRight);
        }
        resultRole.setRights(resultRights);
        databaseEJB.updateRole(resultRole);
    }
}
