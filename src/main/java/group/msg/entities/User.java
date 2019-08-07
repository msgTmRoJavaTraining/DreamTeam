package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private int userId;

    private String username;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_user",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<UserRole> roles;

    @OneToOne
    private PersonalInfo personalInformations;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "userId")
    private List<Notification> notifications;



    @OneToMany(cascade = CascadeType.ALL,mappedBy = "assignedId")
    private List<Bug> assignedBugs;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "createdId")
    private List<Bug> createdBugs;



}
