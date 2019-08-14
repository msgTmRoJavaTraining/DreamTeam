package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.entities.User;
import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    private List<Bug> bugList= new ArrayList<>();

    private String asignedTo;
    private String status;
    private String severity;

    private List<LocalDate>targetDates= new ArrayList<>();
    private List<String>versions= new ArrayList<>();
    private List<String>createdByList= new ArrayList<>();
    private List<String>assignedToList= new ArrayList<>();

    private Bug selectedBug;


    private List<Bug> filteredBugs= new ArrayList<>();

    @PostConstruct
    public void init() {
        bugList = databaseEJB.getAllBugs();
        getAllDates();
        getAllVersions();
        getAllCreatedBy();
        getAllAssignedTo();
//        filteredBugs.add(bugList.get(0));
    }

    public void updateFields(){
        this.severity=selectedBug.getSeverity();
        this.status=selectedBug.getStatus();
    }
    public void updateBug(){
        selectedBug.setSeverity(severity);
        selectedBug.setStatus(status);
        selectedBug.setAssignedId(databaseEJB.getUserByUserName(asignedTo));



        databaseEJB.updateBug(selectedBug);

    }


    private void getAllDates(){
        LocalDateTime targetDate;
        for(Bug bug: bugList){
            targetDate= bug.getTargetDate();
            targetDates.add(targetDate.toLocalDate());
        }
    }
    private void getAllVersions(){
        String crtVersion;
        for(Bug bug: bugList){
            crtVersion= bug.getVersion();
            versions.add(crtVersion);
        }
    }
    private void getAllCreatedBy(){
        String crtUsername;
        for(Bug bug: bugList){
            crtUsername= bug.getCreatedId().getUsername();
            if(!createdByList.contains(crtUsername)){
                createdByList.add(crtUsername);
            }
        }
    }
    private void getAllAssignedTo(){
        String crtUsername;
        for(Bug bug: bugList){
            if(bug.getAssignedId()!=null){
                crtUsername= bug.getAssignedId().getUsername();
                    if(!assignedToList.contains(crtUsername)){
                        assignedToList.add(crtUsername);
                    }

            }
        }
    }
    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }

        if(value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

    @Override
    public Bug getRowData(String rowKey) {
        String name = rowKey;
        return bugList.stream().filter(a -> a.getTitle().equals( name)).collect(Collectors.toList()).get(0);
    }

    @Override
    public Object getRowKey(Bug object) {
        return object.getTitle();
    }

    @Override
    public List<Bug> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        //filter
        for(Bug bug : bugList) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(bug.getClass().getField(filterProperty).get(bug));

                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        }
                        else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }

            if(match) {
                filteredBugs.add(bug);
            }
        }

        //sort
        if(sortField != null) {
            Collections.sort(filteredBugs,new DataTableBean.BugSorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = filteredBugs.size();
        this.setRowCount(dataSize);

        //paginate
        if(dataSize > pageSize) {
            try {
                return filteredBugs.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return filteredBugs.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
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
//    public void rowSelected(SelectEvent event) {
//        outputMessage = selectedMovie.getName();
//    }

}
