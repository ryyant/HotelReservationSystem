package ejb.session.stateless;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;



@Stateless

public class EjbTimerSessionBean
{
    
    public EjbTimerSessionBean()
    {
    }
    
    
    @Schedule(hour = "*", minute = "*", second = "*/25", info = "scheduleEvery5Second")
    public void automaticTimer()
    {
        System.out.println("********** EjbTimerSession.automaticTimer(): scheduleEvery5Second");
    }
    
    
    
    @Timeout
    public void handleTimeout(Timer timer) 
    {
        System.out.println("********** EjbTimerSession.handleTimeout(): " + timer.getInfo().toString());
    }
}