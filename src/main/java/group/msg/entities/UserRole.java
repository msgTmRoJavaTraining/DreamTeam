package group.msg.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRole {


    @Id
    @GeneratedValue
    private int roleId;

    private String roleName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_user",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_rights",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "rightId"))
    private List<Rights> rights;
}
