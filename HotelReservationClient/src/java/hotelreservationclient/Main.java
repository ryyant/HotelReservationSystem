package hotelreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "RoomEntitySessionBeanRemote")
    private static RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    @EJB(name = "ReservationEntitySessionBeanRemote")
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    @EJB(name = "GuestEntitySessionBeanRemote")
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestEntitySessionBeanRemote, reservationEntitySessionBeanRemote, roomEntitySessionBeanRemote);
        mainApp.runApp();
    }
}
