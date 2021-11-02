package hotelmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "RoomTypeEntitySessionBeanRemote")
    private static RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    @EJB(name = "PartnerEntitySessionBeanRemote")
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB(name = "EmployeeEntitySessionBeanRemote")
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote);
        mainApp.runApp();
    }
}
