package group.msg.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Bug {

    @Id
    @GeneratedValue
    private int bugId;

    private String title;
    private String description;
    private String version;
    private String fixedInVersion;
    private LocalDateTime targetDate;
    private String severity;
    private String status;
    private byte[] attachment;

    @ManyToOne
    private User createdId;

    @ManyToOne
    private User assignedId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bugId")
    private List<Notification> notification;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bugId")
    private List<ChangeHistoryBugs> historyBugs;
}
