package group.msg.jsf_beans;

import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
@Data
public class HomepageBackingBean {

    @Inject
    private LoginBackingBean loginBackingBean;

    private String loggedInUserName;

    @PostConstruct
    public void init(){
        loggedInUserName = loginBackingBean.getCurrentlyLoggedInUsername();
    }

    public String ajaxExampleButtonClicked() {
        return "ajax_example";
    }

    public String dataTableExampleButtonClicked() {
        return "data_table_example";
    }

    public String navigateTo(String page){
        return page;
    }
}
