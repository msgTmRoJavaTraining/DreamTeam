package group.msg.jsf_beans;

import lombok.Data;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class FileUploadView implements Serializable {

    private UploadedFile file;


    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
    }
}