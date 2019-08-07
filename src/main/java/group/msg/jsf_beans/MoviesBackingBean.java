package group.msg.jsf_beans;

import group.msg.entities.Movie;
import lombok.Data;
import org.primefaces.event.SelectEvent;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@Named
@Data
public class MoviesBackingBean implements Serializable {
   private String movieName;
   private String movieCate;
   private double movieRati;
   private Date data;
   private String outputMessage="llslsl";
   private Movie selectedMovie;
   private String[] suitable;
    List<Movie> movies=new ArrayList<>();

    public void addToList(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(movieRati>5){
            context.addMessage(null, new FacesMessage("Failed",  "Ratting [0-5]:" + movieRati) );
            return;
        }
        String suitB="";
        for(int i=0;i<suitable.length;i++)suitB=suitB+" "+suitable[i];
        movies.add(new Movie(movieName,movieCate,movieRati,data,suitB));
        context.addMessage(null, new FacesMessage("Succes") );
    }
    public void rowSelected(SelectEvent event) {
        selectedMovie=(Movie)event.getObject();
        outputMessage = selectedMovie.getName()+" "+selectedMovie.getCateg()+" "+selectedMovie.getRating();
    }
}
