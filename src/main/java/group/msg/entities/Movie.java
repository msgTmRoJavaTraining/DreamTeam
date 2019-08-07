package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Movie {
   private String name;
   private String categ;
   private double rating;
   private Date data;
   private String suitable;

}
