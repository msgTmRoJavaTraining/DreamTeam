package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.entities.User;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.util.IOUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Named
@SessionScoped
public class BugManagementBean implements Serializable {

    private String title;
    private String description;
    private String version;
    private String fixedInVersion;
    private Date selectedDate = new Date();
    private LocalDateTime targetDate;
    private String severity;
    private String status="NEW";
    private byte[] attachment;
    private String StringUserAssignedToFixIt;

    @Inject
    DatabaseEJB databaseEJB;

    @Inject
    FileUploadView fileUploadView;

    @Inject
    LoginBean loginBean;

    public String getPresent()
    {
        LocalDateTime present=LocalDateTime.now();
        int day=present.getDayOfMonth();
        int month=present.getMonthValue();
        int year=present.getYear()-2000;
        return month+"/"+day+"/"+year;
    }

    public void clearBugFields() {
        fileUploadView.clearFile();
        title = "";
        description = "";
        version = "";
        fixedInVersion = "";
        severity = "";
        StringUserAssignedToFixIt = "";
    }

    public void createBug() throws IOException {

        if (invalidCredentials())
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Bug not added"));

        if (isDescriptionValid(description) && isValidVersion(version)) {
            Bug bug = new Bug();
            bug.setDescription(description);
            bug.setSeverity(severity);
            bug.setStatus(status);
            bug.setVersion(version);
            bug.setFixedInVersion(fixedInVersion);
            bug.setTitle(title);
            bug.setTargetDate(convertToLocalDateTimeViaSqlTimestamp(selectedDate));

            User UserAssignedToFixIt = databaseEJB.getUserByUserName(StringUserAssignedToFixIt);
            bug.setAssignedId(UserAssignedToFixIt);

            User createdByUser = databaseEJB.getUserByUserName(loginBean.getUsername());
            bug.setCreatedId(createdByUser);

            if (fileUploadView.getFile() != null) {
                InputStream fileInputStream = fileUploadView.getFile().getInputstream();
                attachment = IOUtils.toByteArray(fileInputStream);
            }

            bug.setAttachment(attachment);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Bug added successfully"));

            databaseEJB.createBug(bug);
        }
    }

    private LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public static boolean isValidVersion(String version) {
        String regex = "^[a-zA-Z0-9.]*$";
        return version.matches(regex);
    }

    public boolean isDescriptionValid(String description) {
        return description.length() >= 10;
    }

    public boolean invalidCredentials() {
        boolean ok = false;
        if (!isDescriptionValid(description)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Description must have", "at least 10 characters"));
            ok = true;
        }
        if (!isValidVersion(version)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid version format", "Enter only alphanumeric characters separated by . "));
            ok = true;
        }
        return ok;
    }

}
