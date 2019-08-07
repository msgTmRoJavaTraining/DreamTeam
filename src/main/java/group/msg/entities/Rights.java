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
