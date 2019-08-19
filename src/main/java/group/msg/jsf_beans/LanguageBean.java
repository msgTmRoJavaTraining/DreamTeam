package group.msg.jsf_beans;

import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

@Data
@Named(value = "language")
@SessionScoped
public class LanguageBean implements Serializable {
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private ResourceBundle resourceBundle=ResourceBundle.getBundle("FrontEndInterfaceLanguage", locale);

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void changeLanguage(String language) {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
        resourceBundle=ResourceBundle.getBundle("FrontEndInterfaceLanguage", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    }

    public String getText(String key)
    {
        String value=resourceBundle.getString(key);
        return value;
    }
}

