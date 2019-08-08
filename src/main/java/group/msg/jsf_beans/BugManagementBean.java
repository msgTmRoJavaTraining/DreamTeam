package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.util.IOUtils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Named
@SessionScoped
public class BugManagementBean implements Serializable {

    private String title;
    private String description;
    private String version;
    private String fixedInVersion;
    private LocalDateTime targetDate;
    private String severity;
    private String status;
    private byte[] attachment;

    @Inject
    DatabaseEJB databaseEJB;

    @Inject
    FileUploadView fileUploadView;

    public void createBug() throws IOException {
        fileUploadView.upload();
        setBugData();
    }

    public void setBugData() throws IOException {

        Bug bug = new Bug();
        bug.setDescription(description);
        bug.setSeverity(severity);
        bug.setStatus(status);
        bug.setVersion(version);
        bug.setFixedInVersion(fixedInVersion);
        bug.setTitle(title);
        bug.setTargetDate(targetDate);

        InputStream fileInputStream = fileUploadView.getFile().getInputstream();
        attachment = IOUtils.toByteArray(fileInputStream);

        bug.setAttachment(attachment);

        databaseEJB.createBug(bug);
    }
}
