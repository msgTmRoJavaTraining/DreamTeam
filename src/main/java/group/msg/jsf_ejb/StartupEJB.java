package group.msg.jsf_ejb;

import group.msg.entities.User;
import group.msg.jsf_beans.RoleManagementBean;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class StartupEJB {
    @PersistenceContext(unitName = "java.training")
    private EntityManager entityManager;

    @Inject
    RoleManagementBean roleManagementBean;

    @PostConstruct
    public void init() {
    //roleManagementBean.init();
    }
}
