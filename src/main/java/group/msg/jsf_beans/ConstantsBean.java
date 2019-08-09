package group.msg.jsf_beans;

import lombok.Data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
@Data
public class ConstantsBean implements Serializable {
    public static final String[] SEVERITY = {"CRITICAL", "HIGH", "MEDIUM", "LOW"};
    public static final String[] STATE = {"NEW", "REJECTED", "IN PROGRESS", "FIXED", "INFO NEEDED", "CLOSED"};

    String[] severities = SEVERITY;
    String[] states = STATE;
}
