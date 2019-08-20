package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.entities.Notification;
import group.msg.entities.User;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.UploadedFile;

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

    private static final String NEW_LINE = "\n";
    private String title;
    private String description;
    private String version;
    private String fixedInVersion;
    private Date selectedDate = new Date();
    private LocalDateTime targetDate;
    private String severity;
    private String status = "NEW";
    private byte[] attachment;
    private String stringUserAssignedToFixIt;
    private String mimeType;


    @Inject
    DatabaseEJB databaseEJB;

    @Inject
    FileUploadBean fileUploadBean;

    @Inject
    LoginBean loginBean;

    @Inject
    LanguageBean languageBean;

    public String getPresent() {
        LocalDateTime present = LocalDateTime.now();
        int day = present.getDayOfMonth();
        int month = present.getMonthValue();
        int year = present.getYear() - 2000;
        return month + "/" + day + "/" + year;
    }

    public void clearBugFields() {
        fileUploadBean.clearFile();
        title = "";
        description = "";
        version = "";
        fixedInVersion = "";
        severity = "";
        stringUserAssignedToFixIt = "";
        attachment=null;
    }


    public String createBug() throws IOException {

        if (invalidCredentials(description, version))
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", languageBean.getText("bugNotAdded")));

        if (isDescriptionValid(description) && isValidVersion(version)) {
            Bug bug = new Bug();
            bug.setDescription(description);
            bug.setSeverity(severity);
            bug.setStatus(status);
            bug.setVersion(version);
            bug.setTitle(title);
            bug.setTargetDate(convertToLocalDateTimeViaSqlTimestamp(selectedDate));

            if (!(stringUserAssignedToFixIt == null)) {
                User UserAssignedToFixIt = databaseEJB.getUserByUserName(stringUserAssignedToFixIt);
                bug.setAssignedId(UserAssignedToFixIt);
            }

            User createdByUser = databaseEJB.getUserByUserName(loginBean.getUsername());
            bug.setCreatedId(createdByUser);

            if(fileUploadBean.getFile()!=null){
                bug.setMimeType(getMimeType(fileUploadBean.getFile()));
                InputStream fileInputStream = fileUploadBean.getFile().getInputstream();
                attachment = IOUtils.toByteArray(fileInputStream);
            }

            bug.setAttachment(attachment);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", languageBean.getText("bugSuccessfullAdd")));

            databaseEJB.createBug(bug);

//            String assignedName="UNASSIGNED";
            String assignedName=languageBean.getText("unassigned");

            if(!(bug.getAssignedId()==null))
                assignedName=bug.getAssignedId().getUsername();

            StringBuilder sb=new StringBuilder();

            sb.append(languageBean.getText("title")+": "+bug.getTitle()).append(NEW_LINE)
                    .append(languageBean.getText("description")+": "+bug.getDescription()).append(NEW_LINE)
                    .append(languageBean.getText("version")+": "+bug.getVersion()).append(NEW_LINE)
                    .append(languageBean.getText("targetDate")+": "+bug.getTargetDate()).append(NEW_LINE)
                    .append(languageBean.getText("bugCreatedBy")+": "+bug.getCreatedId().getUsername()).append(NEW_LINE)
                    .append(languageBean.getText("assignedTo")+": "+assignedName).append(NEW_LINE)
                    .append(languageBean.getText("severity")+": "+bug.getSeverity()).append(NEW_LINE)
                    .append(languageBean.getText("status")+": "+bug.getStatus()).append(NEW_LINE)
                    .append(languageBean.getText("newBug"));

            Notification notification = new Notification();
            notification.setMessage(sb.toString());
            notification.setCreatedBy(bug.getCreatedId());
            notification.setName("BUG_UPDATED");
            notification.setDate(LocalDateTime.now());
            notification.setBugId(bug);

            if(bug.getAssignedId()==null)
                notification.setUserId(bug.getAssignedId());
            databaseEJB.createNotification(notification);
        }
        return "homePage";
    }

    public String getMimeType(UploadedFile file)
    {
        String mimeType=null;
        if (file != null) {
            if (file.getFileName().endsWith("png")) {
                mimeType = "image/png";
            } else if (file.getFileName().endsWith("jpg") || file.getFileName().endsWith("jpeg")) {
                mimeType = "image/jpeg";
            } else if (file.getFileName().endsWith("pdf")) {
                mimeType = "application/pdf";
            } else if (file.getFileName().endsWith("doc")) {
                mimeType = "application/msword";
            } else if (file.getFileName().endsWith("odf")) {
                mimeType = "application/vnd.oasis.opendocument.formula";
            } else if (file.getFileName().endsWith("xls") || file.getFileName().endsWith("xlsx")) {
                mimeType = "application/excel";
            }
        }
        return mimeType;
    }

    private LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public boolean isValidVersion(String version) {
        String regex = "^[a-zA-Z0-9.]*$";
        return version.matches(regex);
    }

    public boolean isDescriptionValid(String description) {
        return description.length() >= 10;
    }

    public boolean invalidCredentials(String description, String version) {
        boolean ok = false;
        if (!isDescriptionValid(description)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getText("mustDescription"), languageBean.getText("min10Chars")));
            ok = true;
        }
        if (!isValidVersion(version)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getText("invalidVersion"), languageBean.getText("onlyAlphanumericsSeparatedByDot")));
            ok = true;
        }
        return ok;
    }

}
