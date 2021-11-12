package hotelmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.OccupantEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
import util.exception.EmployeeNotFoundException;

public class MainApp {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    private SystemAdministrationModule systemAdministrationModule;
    private HotelOperationModule hotelOperationModule;
    private FrontOfficeModule frontOfficeModule;

    private Employee currentEmployeeEntity;

    public MainApp() {
    }

    public MainApp(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote
            , RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateEntitySessionBeanRemote;
        this.occupantEntitySessionBeanRemote = occupantEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
    }

    public void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HoRS Management Client ***");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                System.out.println();

                switch (response) {
                    case 1:
                        try {
                        employeeLogin();
                        System.out.println("Login successful!\n");

                        if (currentEmployeeEntity.getUserRole() == UserRoleEnum.SYSTEM_ADMIN) {
                            systemAdministrationModule = new SystemAdministrationModule(currentEmployeeEntity, employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, reservationEntitySessionBeanRemote);
                            systemAdministrationModule.menuSystemAdministration();
                        }

                        if (currentEmployeeEntity.getUserRole() == UserRoleEnum.OPERATION_MANAGER || currentEmployeeEntity.getUserRole() == UserRoleEnum.SALES_MANAGER) {
                            hotelOperationModule = new HotelOperationModule(currentEmployeeEntity, roomTypeEntitySessionBeanRemote, roomRateEntitySessionBeanRemote, roomEntitySessionBeanRemote, reservationEntitySessionBeanRemote);
                            hotelOperationModule.menuHotelOperation();
                        }

                        if (currentEmployeeEntity.getUserRole() == UserRoleEnum.RELATION_OFFICER) {
                            frontOfficeModule = new FrontOfficeModule(currentEmployeeEntity, occupantEntitySessionBeanRemote, reservationEntitySessionBeanRemote, roomEntitySessionBeanRemote);
                            frontOfficeModule.menuFrontOffice();
                        }

                    } catch (EmployeeNotFoundException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                    break;
                    case 2:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void employeeLogin() throws EmployeeNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Employee Login ***");
        System.out.print("Enter Username: ");
        String usernameInput = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String passwordInput = sc.nextLine().trim();
        currentEmployeeEntity = employeeEntitySessionBeanRemote.employeeLogin(usernameInput, passwordInput);
        System.out.println();
    }

}
