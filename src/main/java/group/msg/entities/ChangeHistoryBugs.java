package group.msg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
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
    
    private int modifiedBy;
}
