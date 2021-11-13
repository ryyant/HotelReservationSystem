package hotelreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
                        System.out.print("Number of Rooms: ");
                        int numOfRooms = scanner.nextInt();
                        scanner.nextLine();
                        searchHotelRoom(numOfRooms);
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
            System.out.println("1: Search Hotel Room to Reserve");
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
                        System.out.print("Number of Rooms: ");
                        int numOfRooms = scanner.nextInt();
                        scanner.nextLine();
                        reserveHotelRoom(numOfRooms);
                        break;

                    case 2:
                        System.out.println("Which reservation would you like to view?");
                        System.out.print("Enter reservation Id > ");
                        scanner.nextLine();
                        Long reservationId = scanner.nextLong();

                        viewReservationDetails(currentGuestEntity.getOccupantId(), reservationId);
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
    private HashMap<RoomType, Double> searchHotelRoom(int numOfRoomsInput) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        HashMap<RoomType, Double> priceMapping = new HashMap<>();

        try {
            while (true) {
                System.out.print("Enter check in date (DD/MM/YYYY): ");
                String checkInDateInput = scanner.nextLine();
                checkInDate = formatter.parse(checkInDateInput);
                System.out.print("Enter check out date (DD/MM/YYYY): ");
                String checkOutDateInput = scanner.nextLine();
                checkOutDate = formatter.parse(checkOutDateInput);
                System.out.println();

                // ensure input valid 
                if (checkInDate.before(checkOutDate)) {
                    break;
                } else {
                    System.out.println("Check in date must be before check out date!\n");
                }
            }

            List<RoomType> roomTypes = roomEntitySessionBeanRemote.searchRoom(numOfRoomsInput, checkInDate, checkOutDate);

            // loop room types available, check the total amount for all the days
            for (RoomType rt : roomTypes) {

                if (rt.getEnabled()) {
                    Calendar checkInCal = Calendar.getInstance();
                    checkInCal.setTime(checkInDate);
                    Calendar checkOutCal = Calendar.getInstance();
                    checkOutCal.setTime(checkOutDate);

                    double amount = 0;

                    while (checkInCal.before(checkOutCal)) {
                        Date d = checkInCal.getTime();
                        amount += roomEntitySessionBeanRemote.onlineDayPrevailingRate(d, rt);
                        checkInCal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    priceMapping.put(rt, amount);
                }

            }

            for (Map.Entry<RoomType, Double> entry : priceMapping.entrySet()) {
                System.out.println("Room Id: " + entry.getKey().getRoomTypeId() + ", Room Type: " + entry.getKey().getName() + ", Amount: " + entry.getValue());
            }

            System.out.println();

        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ParseException ex) {
            System.out.println("Wrong Input Format!\n");
        }

        return priceMapping;

    }

    // use case 4 (includes use case 3)
    private void reserveHotelRoom(int numOfRoomsInput) {
        Scanner sc = new Scanner(System.in);

        HashMap<RoomType, Double> priceMapping = searchHotelRoom(numOfRoomsInput);

        if (!priceMapping.isEmpty()) {
            System.out.print("Do you want to reserve a room? (Y/N) > ");
            String input = sc.nextLine().trim();
            System.out.println();
            if (input.equalsIgnoreCase("Y")) {
                System.out.print("Enter Id of the room that you would like to reserve! > ");
                Long roomTypeId = sc.nextLong();
                sc.nextLine();

                Reservation reservation = reservationEntitySessionBeanRemote.reserveRoom(roomTypeId, numOfRoomsInput, currentGuestEntity, priceMapping, checkInDate, checkOutDate);

                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String nowStr = formatter.format(now);
                String checkInDateStr = formatter.format(reservation.getCheckInDate());
                String checkOutDateStr = formatter.format(reservation.getCheckOutDate());

                if (nowStr.equals(checkInDateStr)) {
                    reservationEntitySessionBeanRemote.allocateRoomsForReservation(reservation);
                }

                System.out.println();
                System.out.println(reservation.getRoomType().getName() + " has been reserved from " + checkInDateStr + " until " + checkOutDateStr + "!\n");
                System.out.println("Your reservation ID is " + reservation.getReservationId() + "!\n");

            }
        }
        
    }

    // use case 5
    private void viewReservationDetails(Long occupantId, Long reservationId) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            List<Reservation> reservations = reservationEntitySessionBeanRemote.retrieveReservationsByOccupantId(occupantId);
            Reservation reservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationId);

            if (reservations.contains(reservation)) {
                System.out.println("Here are the details for your reservation with ID: " + reservationId);
                System.out.printf("%15s%20s%30s%15s%20s\n", "Check In Date", "Check Out Date", "Room Type", "Cost/Rm", "Number of Rooms");
                String checkInDate = formatter.format(reservation.getCheckInDate());
                String checkOutDate = formatter.format(reservation.getCheckOutDate());
                RoomType roomType = reservation.getRoomType();

                System.out.printf("%15s%20s%30s%15s%20s\n", checkInDate, checkOutDate, roomType.getName(), reservation.getAmount(), reservation.getQuantity());
                System.out.println();
            } else {
                System.out.println("Invalid Reservation Id! You did not make this reservation! :( \n");
            }

        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    // use case 6
    private void viewAllReservations() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            List<Reservation> reservations = reservationEntitySessionBeanRemote.retrieveReservationsByOccupantId(currentGuestEntity.getOccupantId());
            System.out.println("-------------------------YOUR RESERVATIONS--------------------------");
            System.out.printf("%15s%15s%20s%28s\n", "ReservationId", "Check In Date", "Check Out Date", "Room Type");
            for (Reservation reservation : reservations) {
                String checkInDate = formatter.format(reservation.getCheckInDate());
                String checkOutDate = formatter.format(reservation.getCheckOutDate());

                RoomType roomType = reservation.getRoomType();
                System.out.printf("%15s%15s%20s%28s\n", reservation.getReservationId(), checkInDate, checkOutDate, roomType.getName());

            }

        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
