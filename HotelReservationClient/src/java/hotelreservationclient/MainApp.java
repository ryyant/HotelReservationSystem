package hotelreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;

public class MainApp {

    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    private Guest currentGuestEntity;
    private Date checkInDate;
    private Date checkOutDate;

    public MainApp() {
    }

    public MainApp(GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote) {
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
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
        System.out.print("Enter Name: ");
        String nameInput = sc.nextLine().trim();
        System.out.print("Enter Email: ");
        String emailInput = sc.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phoneNumberInput = sc.nextLine().trim();
        System.out.print("Enter Passport Number: ");
        String passportNumberInput = sc.nextLine().trim();

        currentGuestEntity = guestEntitySessionBeanRemote.guestRegister(usernameInput, passwordInput, nameInput, emailInput, phoneNumberInput, passportNumberInput);
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
            System.out.println("1: Search Hotel Room");
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
                        searchHotelRoom();
                        System.out.println("Do you want to reserve a room? (Y/N) >");
                        scanner.nextLine();
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("Y")) {
                            reserveHotelRoom();
                        }

                        break;

                    case 2:
                        System.out.println("Which reservation would you like to view?");
                        System.out.println("Enter reservation Id >");
                        Long reservationId = scanner.nextLong();

                        viewReservationDetails(reservationId);
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
        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YY");

            System.out.println("Enter check in date (DD-MM-YY): ");
            String checkInDateInput = scanner.nextLine();
            checkInDate = formatter.parse(checkInDateInput);

            System.out.println("Enter check out date (DD-MM-YY): ");
            String checkOutDateInput = scanner.nextLine();
            checkOutDate = formatter.parse(checkOutDateInput);

            try {
                List<Room> rooms = roomEntitySessionBeanRemote.searchRoom(checkInDate, checkOutDate);
                for (Room room : rooms) {
                    System.out.println("Room: " + room);
                    System.out.println("Room Type: " + room.getRoomType().getName());

                }
            } catch (RoomNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (ParseException ex) {
            System.out.println("Wrong Input Format!");
        }

    }

    // use case 4 (includes use case 3)
    private void reserveHotelRoom() {
        Scanner scanner = new Scanner(System.in);

        String input = "Y";

        while (input.equalsIgnoreCase("Y")) {

            System.out.println("Enter room ID of the room that you would like to reserve! >");
            Long roomId = scanner.nextLong();
            
            List<Reservation> list = currentGuestEntity.getReservations();
            System.out.println(list);
            
            Reservation reservation = reservationEntitySessionBeanRemote.reserveRoom(roomId, currentGuestEntity);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);

            System.out.println("Room has been reserved from " + checkInDate + " until " + checkOutDate);
            System.out.println("Would you like to reserve another room? (enter Y to reserve) >");
            input = scanner.nextLine();
        }
    }

    // use case 5
    private void viewReservationDetails(Long reservationId) {
        try {
            List<Reservation> reservations = currentGuestEntity.getReservations();
            Reservation reservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationId);

        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    // use case 6
    private void viewAllReservations() {
        List<Reservation> reservations = currentGuestEntity.getReservations();
        System.out.println("-------THESE ARE YOUR RESERVATIONS--------");
        System.out.printf("%8s%20s%20s%15s\n", "Check In Date", "Check Out Date", "Room Type", "Amount");
        for (Reservation reservation : reservations) {
            RoomType roomType = reservation.getRoom().getRoomType();
            System.out.printf("%8s%20s%20s%15s\n", reservation.getCheckInDate(), reservation.getCheckOutDate(), roomType.toString(), reservation.getAmount());

        }
    }

}
