package hotelreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "GuestEntitySessionBeanRemote")
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestEntitySessionBeanRemote);
        mainApp.runApp();
    }
}
