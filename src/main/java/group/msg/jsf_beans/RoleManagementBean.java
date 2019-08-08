package group.msg.jsf_beans;

import group.msg.entities.UserRole;
import group.msg.jsf_ejb.DatabaseEJB;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RoleManagementBean {

    @Inject
    private DatabaseEJB databaseEJB;

    public void init() {
        UserRole administrator = new UserRole();
        administrator.setRoleName("Administrator");

        UserRole projectManager = new UserRole();
        projectManager.setRoleName("Project manager");

        UserRole testManager = new UserRole();
        testManager.setRoleName("Test Manager");

        UserRole developer = new UserRole();
        developer.setRoleName("Developer");

        UserRole tester = new UserRole();
        tester.setRoleName("Tester");

        databaseEJB.createRole(administrator);
        databaseEJB.createRole(projectManager);
        databaseEJB.createRole(testManager);
        databaseEJB.createRole(developer);
        databaseEJB.createRole(tester);
    }
}
