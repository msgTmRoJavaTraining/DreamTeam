package group.msg;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class WebHelper {
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }
}
