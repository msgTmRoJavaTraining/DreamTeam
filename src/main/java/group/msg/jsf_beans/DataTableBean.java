package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.entities.User;
import group.msg.entities.Notification;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class DataTableBean extends LazyDataModel<Bug> implements Serializable {

    @Inject
    DatabaseEJB databaseEJB;

    @Inject
    UserManagementBean userManagementBean;

    @Inject
    LoginBean loginBean;

    @Inject
    private LanguageBean languageBean;

    private List<Bug> bugList = new ArrayList<>();
    private static final String NEW_LINE= "\n";//System.getProperty("line.separator");

    @Inject
    BugManagementBean bugManagementBean;

    @Inject
    FileUploadView fileUploadView;

    private String assignedTo;
    private String status;
    private String severity;
    private String version;
    private String description;
    private byte[] attachment;
    private boolean deleteAttachment =false;

    private List<LocalDate> targetDates = new ArrayList<>();
    private List<String> versions = new ArrayList<>();
    private List<String> createdByList = new ArrayList<>();
    private List<String> assignedToList = new ArrayList<>();

    private Bug selectedBug;

    private List<Bug> filteredBugs = new ArrayList<>();
    private String oldStatus;
    private boolean editAttachment=false;

    @PostConstruct
    public void init() {
        bugList = databaseEJB.getAllBugs();
        getAllDates();
        getAllVersions();
        getAllCreatedBy();
        getAllAssignedTo();
    }

    public void deleteAttachmentOperation()
    {
        deleteAttachment =true;
    }

    public void editAttachmentOperation()
    {
        editAttachment =true;
    }

    public List<String> possibleStates() {
        //ConstantsBean.STATE  {"NEW", "REJECTED", "IN PROGRESS", "FIXED", "INFO NEEDED", "CLOSED"};
        List<String> possState = new ArrayList<>();
        switch (status) {
            case "NEW":
                possState.add(ConstantsBean.STATE[1]);
                possState.add(ConstantsBean.STATE[2]);
                break;
            case "IN PROGRESS":
                possState.add(ConstantsBean.STATE[3]);
                possState.add(ConstantsBean.STATE[4]);
                possState.add(ConstantsBean.STATE[1]);
                break;
            case "FIXED":
                possState.add(ConstantsBean.STATE[5]);
                break;
            case "INFO NEEDED":
                possState.add(ConstantsBean.STATE[2]);
                break;
            case "REJECTED":
                possState.add(ConstantsBean.STATE[5]);
                break;
        }
        if (!loginBean.isBugClose())
            possState.remove(ConstantsBean.STATE[5]);

        possState.add(status);
        return possState;
    }

    public void updateFields() {
        this.severity = selectedBug.getSeverity();
        this.status = selectedBug.getStatus();
        this.version = selectedBug.getVersion();
        this.description=selectedBug.getDescription();
        oldStatus=selectedBug.getStatus();
        if(!(selectedBug.getAssignedId()==null)){
            this.assignedTo=selectedBug.getAssignedId().getUsername();
        }else{
            this.assignedTo="UNASSIGNED";
        }
    }

    public void updateBug() throws IOException {
        if (bugManagementBean.invalidCredentials(description, version))
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", languageBean.getText("bugNotUpdated")));

        if (bugManagementBean.isDescriptionValid(description) && bugManagementBean.isValidVersion(version)) {
            selectedBug.setSeverity(severity);
            selectedBug.setStatus(status);
            selectedBug.setDescription(description);
            selectedBug.setVersion(version);

            if (deleteAttachment) {
                selectedBug.setAttachment(null);
                deleteAttachment=false;
                selectedBug.setMimeType(null);
            } else if(editAttachment){
                selectedBug.setMimeType(bugManagementBean.getMimeType(fileUploadView.getFile()));
                InputStream fileInputStream = fileUploadView.getFile().getInputstream();
                attachment = IOUtils.toByteArray(fileInputStream);
                selectedBug.setAttachment(attachment);
                editAttachment=false;
            }

            if ((assignedTo.equals("UNASSIGNED"))) {
                selectedBug.setAssignedId(null);
            } else {
                selectedBug.setAssignedId(databaseEJB.getUserByUserName(assignedTo));
            }

            databaseEJB.updateBug(selectedBug);

            String assignedName="UNASSIGNED";
            if(!(selectedBug.getAssignedId()==null))
                assignedName=selectedBug.getAssignedId().getUsername();
            if(status.equals("CLOSED")) {
                StringBuilder sb=new StringBuilder();


                sb.append(languageBean.getText("title")+selectedBug.getTitle()).append(NEW_LINE)
                        .append(languageBean.getText("description")+selectedBug.getDescription()).append(NEW_LINE)
                        .append(languageBean.getText("version")+selectedBug.getVersion()).append(NEW_LINE)
                        .append(languageBean.getText("targetDate")+selectedBug.getTargetDate()).append(NEW_LINE)
                        .append(languageBean.getText("bugCreatedBy")+selectedBug.getCreatedId().getUsername()).append(NEW_LINE)
                        .append(languageBean.getText("assignedTo")+assignedName).append(NEW_LINE)
                        .append(languageBean.getText("severity")+selectedBug.getSeverity()).append(NEW_LINE)
                        .append(languageBean.getText("status")+selectedBug.getStatus());

                sendNotification(sb.toString(),"BUG_CLOSE",selectedBug);
            }

            if(!oldStatus.equals(status))
            {
                StringBuilder sb=new StringBuilder();
                sb.append(languageBean.getText("title")+selectedBug.getTitle()).append(NEW_LINE)
                        .append(languageBean.getText("description")+selectedBug.getDescription()).append(NEW_LINE)
                        .append(languageBean.getText("version")+selectedBug.getVersion()).append(NEW_LINE)
                        .append(languageBean.getText("targetDate")+selectedBug.getTargetDate()).append(NEW_LINE)
                        .append(languageBean.getText("bugCreatedBy")+selectedBug.getCreatedId().getUsername()).append(NEW_LINE)
                        .append(languageBean.getText("assignedTo")+assignedName).append(NEW_LINE)
                        .append(languageBean.getText("severity")+selectedBug.getSeverity()).append(NEW_LINE)
                        .append(languageBean.getText("oldStatus")+oldStatus+languageBean.getText("newStatus")+": "+selectedBug.getStatus());

                sendNotification(sb.toString(),"BUG_STATUS_UPDATED",selectedBug);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", languageBean.getText("bugSuccessfullUpdate")));
        }
    }

    private void getAllDates() {
        LocalDateTime targetDate;
        for (Bug bug : bugList) {
            targetDate = bug.getTargetDate();
            targetDates.add(targetDate.toLocalDate());
        }
    }

    private void getAllVersions() {
        String crtVersion;
        for (Bug bug : bugList) {
            crtVersion = bug.getVersion();
            versions.add(crtVersion);
        }
    }

    private void getAllCreatedBy() {
        String crtUsername;
        for (Bug bug : bugList) {
            crtUsername = bug.getCreatedId().getUsername();
            if (!createdByList.contains(crtUsername)) {
                createdByList.add(crtUsername);
            }
        }
    }

    private void getAllAssignedTo() {
        String crtUsername;
        for (Bug bug : bugList) {
            if (bug.getAssignedId() != null) {
                crtUsername = bug.getAssignedId().getUsername();
                if (!assignedToList.contains(crtUsername)) {
                    assignedToList.add(crtUsername);
                }
            }
        }
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

    public DefaultStreamedContent downloadAttachment() {
        InputStream stream = new ByteArrayInputStream(selectedBug.getAttachment());
        String mimeType = selectedBug.getMimeType();
        String extension;
        if (mimeType.contains("png")) {
            extension = "png";
        } else if (mimeType.contains("jpeg")) {
            extension = "jpeg";
        } else if (mimeType.contains("pdf")) {
            extension = "pdf";
        } else if (mimeType.contains("msword")) {
            extension = "doc";
        } else if (mimeType.contains("opendocument")) {
            extension = "odf";
        } else if (mimeType.contains("excel")) {
            extension = "xls";
        } else return null;
        return new DefaultStreamedContent(stream, mimeType, languageBean.getText("attachment") +"."+ extension);
    }

    @Override
    public Bug getRowData(String rowKey) {
        String name = rowKey;
        return bugList.stream().filter(a -> a.getTitle().equals(name)).collect(Collectors.toList()).get(0);
    }

    @Override
    public Object getRowKey(Bug object) {
        return object.getTitle();
    }

    @Override
    public List<Bug> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        for (Bug bug : bugList) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(bug.getClass().getField(filterProperty).get(bug));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }

            if (match) {
                filteredBugs.add(bug);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(filteredBugs, new DataTableBean.BugSorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = filteredBugs.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return filteredBugs.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return filteredBugs.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return filteredBugs;
        }
    }

    public static class BugSorter implements Comparator<Bug> {
        private String sortField;
        private SortOrder sortOrder;

        public BugSorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Bug bug, Bug t1) {
            try {
                Object val1 = Bug.class.getField(sortField).get(bug);
                Object val2 = Bug.class.getField(sortField).get(t1);

                int comparationResult = ((Comparable) val1).compareTo(val2);

                return SortOrder.ASCENDING.equals(sortOrder) ? comparationResult : (-1) * comparationResult;
            } catch (Exception e) {
                return 1;
            }
        }
    }

    public List<String> activeUsers() {
        List<String> activeUsr = new ArrayList<>();
        activeUsr.add("UNASSIGNED");
        activeUsr.addAll(userManagementBean.activeUserNamesList());

        return activeUsr;
    }

    public void sendNotification(String message,String bugName, Bug selectedBug)
    {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedBy(selectedBug.getCreatedId());
        notification.setName(bugName);
        notification.setDate(LocalDateTime.now());
        notification.setBugId(selectedBug);

        if(selectedBug.getAssignedId()!=null)
        notification.setUserId(selectedBug.getAssignedId());

        databaseEJB.createNotification(notification);
    }


}
