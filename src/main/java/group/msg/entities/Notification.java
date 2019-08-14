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

public class Notification {

    @Id
    @GeneratedValue
    private int notificationId;

    private String name;
    private String message;
    private LocalDateTime date;

    @ManyToOne
    private User userId;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Bug bugId;

}
