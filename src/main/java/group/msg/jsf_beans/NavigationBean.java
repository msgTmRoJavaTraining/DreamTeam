package group.msg.jsf_beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class NavigationBean implements Serializable {
    public static String navigateTo(String page)
    {
        return page;
    }
}
