package group.msg.jsf_beans;

import lombok.Data;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.InputStream;
import java.io.Serializable;

@Data
@Named
@ViewScoped
public class FileDownloadBean implements Serializable {

    private StreamedContent file;

    public FileDownloadBean() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("resources/images/romania.png");
        file = new DefaultStreamedContent(stream, "image/png", "downloaded_boromir.jpg");
    }

    public StreamedContent getFile() {
        return file;
    }
}