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
public class Rights {

    @Id
    @GeneratedValue
    private int rightId;

    private String code;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_rights",
            joinColumns = @JoinColumn(name = "rightId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<UserRole> roles;

}
