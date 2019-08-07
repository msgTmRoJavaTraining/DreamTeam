package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class ChangeHistoryBugs {


    @Id
    @GeneratedValue
    private int changeId;

    @ManyToOne
    private Bug bugId;

    private String attribute;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime modifiedDate;
    private User modifiedBy;
}
