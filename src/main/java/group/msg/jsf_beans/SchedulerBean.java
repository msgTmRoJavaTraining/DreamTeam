package group.msg.jsf_beans;

import group.msg.jsf_ejb.DatabaseEJB;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.inject.Inject;
import java.time.LocalDateTime;

@Singleton
@Startup
public class SchedulerBean {

    @Inject
    private DatabaseEJB databaseEJB;

    @Schedule(dayOfWeek="*" ,  hour = "11", minute = "58", second = "0", month = "*" , year = "*", info = "Every day timer")
    public void automaticallyScheduled(Timer timer) {
        LocalDateTime today = LocalDateTime.now();
        today= today.minusDays(30);
        databaseEJB.removeNotificationOlder(today);
    }

}
