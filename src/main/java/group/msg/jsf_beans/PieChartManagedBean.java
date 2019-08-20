package group.msg.jsf_beans;

import group.msg.jsf_ejb.DatabaseEJB;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@Getter
@Setter
public class PieChartManagedBean implements Serializable {


    @Inject
    private DatabaseEJB databaseEJB;

    @Inject
    private LanguageBean languageBean;

    public static final String[] STATES= {"NEW", "REJECTED", "IN PROGRESS", "FIXED", "INFO NEEDED", "CLOSED"};

    private PieChartModel pieModel2;
    private PieChartModel livePieModel;

    @PostConstruct
    public void init() {
        createPieModels();
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }


    private void createPieModels() {
        createPieModel2();
        createLivePieModel();
    }

    private void createPieModel2() {
        pieModel2 = new PieChartModel();

        pieModel2.set(STATES[0], databaseEJB.getNoOfBugsAfterState(STATES[0]));
        pieModel2.set(STATES[1],  databaseEJB.getNoOfBugsAfterState(STATES[1]));
        pieModel2.set(STATES[2],  databaseEJB.getNoOfBugsAfterState(STATES[2]));
        pieModel2.set(STATES[3],  databaseEJB.getNoOfBugsAfterState(STATES[3]));
        pieModel2.set(STATES[4],  databaseEJB.getNoOfBugsAfterState(STATES[4]));
        pieModel2.set(STATES[5],  databaseEJB.getNoOfBugsAfterState(STATES[5]));

        pieModel2.setTitle(languageBean.getText("bugs"));
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
        pieModel2.setShadow(false);
    }

    private void createLivePieModel() {
        livePieModel = new PieChartModel();

        livePieModel.set("Candidate 1", 540);
        livePieModel.set("Candidate 2", 325);
    }

}
