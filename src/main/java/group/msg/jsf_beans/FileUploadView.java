package group.msg.jsf_beans;

import lombok.Data;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@ViewScoped
public class FileUploadView implements Serializable {

    private UploadedFile file;

    public void clearFile() {
        file = null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
    }
}