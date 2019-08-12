package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Personal_information")
public class PersonalInfo {

    @Id
    @GeneratedValue
    private int personalId;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
}
