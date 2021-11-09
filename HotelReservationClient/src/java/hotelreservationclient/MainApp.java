package hotelreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import entity.Guest;
import java.util.Scanner;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;

public class MainApp {

    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;

    private Guest currentGuestEntity;

    public MainApp() {
    }

    public MainApp(GuestEntitySessionBeanRemote guestEntitySessionBeanRemote) {
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
    }

    public void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HoRS Reservation Client ***");
            System.out.println("1: Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                System.out.println();

                switch (response) {
                    case 1:
                        
                        try {
                        guestLogin();
                        System.out.println("Login successful!\n");
                        guestLoginPage();
                    } catch (GuestNotFoundException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }

                    break;

                    case 2:
                         
                        try {
                        guestRegister();
                        System.out.println("Guest registration successful!\n");
                    } catch (DuplicateException ex) {
                        System.out.println("Registration failed: " + ex.getMessage() + "\n");
                    }

                    break;

                    case 3:
                        searchHotelRoom();
                        break;

                    case 4:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    // use case 1
    private void guestLogin() throws GuestNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Guest Login ***");
        System.out.print("Enter Username: ");
        String usernameInput = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String passwordInput = sc.nextLine().trim();
        currentGuestEntity = guestEntitySessionBeanRemote.guestLogin(usernameInput, passwordInput);
        System.out.println();
    }

    // use case 2
    private void guestRegister() throws DuplicateException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Guest Register ***");
        System.out.print("Enter Username: ");
        String usernameInput = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String passwordInput = sc.nextLine().trim();
        // currentGuestEntity = guestEntitySessionBeanRemote.guestRegister(usernameInput, passwordInput);
        System.out.println();
    }

    // guest login screen
    public void guestLoginPage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentGuestEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** Guest View ***");
            System.out.println("-----------------------");
            System.out.println("1: Reserve Hotel Room");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View All Reservations");
            System.out.println("-----------------------");
            System.out.println("4: Logout\n");
            int response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                System.out.println();
                switch (response) {
                    case 1:
                        reserveHotelRoom();
                        break;
                    case 2:
                        viewReservationDetails();
                        break;
                    case 3:
                        viewAllReservations();
                        break;
                    case 4:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    // use case 3
    private void searchHotelRoom() {
    }

    // use case 4 (includes use case 3)
    private void reserveHotelRoom() {
    }

    // use case 5
    private void viewReservationDetails() {
    }

    // use case 6
    private void viewAllReservations() {
    }

}
