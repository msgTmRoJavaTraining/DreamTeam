package group.msg.jsf_beans;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@Getter
@Setter
public class NavigationBean implements Serializable {
    public static String navigateTo(String page) {
        return page;
    }
}
