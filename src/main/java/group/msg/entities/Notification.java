package entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
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
    private Bug bugId;

}
