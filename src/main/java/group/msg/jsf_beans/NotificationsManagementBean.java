package group.msg.jsf_beans;

import group.msg.entities.Notification;
import group.msg.entities.User;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Getter
@Setter
public class NotificationsManagementBean implements Serializable {
    @Inject
    private LoginBean loginBean;
    @Inject
    private DatabaseEJB databaseEJB;
    private String crtUsername;


    public String getIconForNotification(Notification notif){
        String iconName="/resources/images/";
        String crtNotifType= notif.getName();
        String icon=crtNotifType+".png";
        iconName= iconName+icon;
        return iconName;
    }

    public List<Notification> allUserNotifications(){
        List<Notification> notifs;
        User usr=databaseEJB.getUserByUserName(loginBean.getUsername());
        notifs=usr.getGeneratedNotifications();
        notifs.addAll(usr.getNotifications());
        return notifs;
    }

}
