package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Personal_information")
public class PersonalInfo {

    @Id
    @GeneratedValue
    private int personalId;

    @OneToOne
    private int userId;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private boolean isActive;

}
