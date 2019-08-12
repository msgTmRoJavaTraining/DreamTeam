package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private int userId;

    private String username;
    private String password;

    @ManyToMany
    @JoinTable(name = "role_user",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<UserRole> roles;

    @OneToOne(cascade = CascadeType.ALL)
    private PersonalInfo personalInformations;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "userId")
    private List<Notification> notifications;
    
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "assignedId")
    private List<Bug> assignedBugs;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "createdId")
    private List<Bug> createdBugs;



}
