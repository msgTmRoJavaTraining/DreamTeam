package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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
