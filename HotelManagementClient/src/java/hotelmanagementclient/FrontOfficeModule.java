/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementclient;

import ejb.session.stateless.OccupantEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.Employee;
import entity.Occupant;
import entity.Report;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.OccupantNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author ryyant
 */
public class FrontOfficeModule {

    private Employee currentEmployeeEntity;
    private Occupant occupantEntity;

    private OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    private Date checkInDate;
    private Date checkOutDate;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(Employee currentEmployeeEntity, OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote) {
        this.occupantEntitySessionBeanRemote = occupantEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuFrontOffice() throws EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentEmployeeEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** Guest Relation Officer View ***");
            System.out.println("-----------------------");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("-----------------------");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("-----------------------");
            System.out.println("5: Logout\n");
            int response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();
                int numOfRooms = 0;

                switch (response) {
                    case 1:
                        System.out.print("Number of Rooms: ");
                        numOfRooms = scanner.nextInt();
                        scanner.nextLine();
                        searchHotelRoom(numOfRooms);
                        break;
                    case 2:
                        System.out.print("Number of Rooms: ");
                        numOfRooms = scanner.nextInt();
                        scanner.nextLine();
                        reserveHotelRoom(numOfRooms);
                        break;
                    case 3:
                        checkInGuest();
                        break;
                    case 4:
                        checkOutGuest();
                        break;
                    case 5:
                        System.out.println("Logged out!\n");
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    // use case 23
    private HashMap<RoomType, Double> searchHotelRoom(int numOfRoomsInput) {
        HashMap<RoomType, Double> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

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
                    System.out.println("Check in date must be before check out date!");
                }
            }

            map = roomEntitySessionBeanRemote.searchRoom("Walk-in", numOfRoomsInput, checkInDate, checkOutDate);
            for (Map.Entry<RoomType, Double> entry : map.entrySet()) {
                System.out.println("Room Id: " + entry.getKey().getRoomTypeId() + ", Room Type: " + entry.getKey().getName() + ", Amount: " + entry.getValue());
            }

            System.out.println();
        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ParseException ex) {
            System.out.println("Wrong Input Format!\n");
        }

        return map;

    }

    // use case 24 (includes use case 23)
    private void reserveHotelRoom(int numOfRoomsInput) {
        Scanner sc = new Scanner(System.in);

        HashMap<RoomType, Double> priceMapping = searchHotelRoom(numOfRoomsInput);

        if (!priceMapping.isEmpty()) {
            System.out.print("Do you want to reserve a room? (Y/N) > ");
            String input = sc.nextLine().trim();

            System.out.print("Occupant Name: ");
            String name = sc.nextLine().trim();
            System.out.print("Occupant Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Occupant Phone Number: ");
            String phoneNumber = sc.nextLine().trim();
            System.out.println("Occupant Passport Number: ");
            String passportNumber = sc.nextLine().trim();

            try {
                Occupant walkInGuest = new Occupant(name, email, phoneNumber, passportNumber);
                occupantEntity = occupantEntitySessionBeanRemote.occupantRegister(walkInGuest);
            } catch (DuplicateException ex) {
                try {
                    System.out.println(ex.getMessage());
                    System.out.println("Creating Occupant Record...");
                    occupantEntity = occupantEntitySessionBeanRemote.retrieveOccupantByPassport(passportNumber);
                    System.out.println("Occupant Record Created!");
                } catch (OccupantNotFoundException e) {
                    System.out.println(ex.getMessage());
                }

            }

            while (input.equalsIgnoreCase("Y")) {
                System.out.print("Enter Id of the room that you would like to reserve! > ");
                Long roomTypeId = sc.nextLong();
                sc.nextLine();

                Reservation reservation = reservationEntitySessionBeanRemote.reserveRoom(roomTypeId, numOfRoomsInput, occupantEntity, priceMapping, checkInDate, checkOutDate);

                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String checkInDateStr = formatter.format(now);
                String checkOutDateStr = formatter.format(reservation.getCheckInDate());

                if (checkInDateStr.equals(checkOutDateStr)) {
                    reservationEntitySessionBeanRemote.allocateRoomsForReservation(reservation);
                }

                System.out.println(reservation.getRoomType().getName() + " has been reserved from " + checkInDateStr + " until " + checkOutDateStr + "!");
                System.out.print("Would you like to reserve another room? (Y/N) > ");
                input = sc.nextLine().trim();
            }

            System.out.println();
        }
    }

    // use case 25
    public void checkInGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Occupant Passport Number > ");
        String passportNum = sc.nextLine();

        try {
            Occupant occupant = occupantEntitySessionBeanRemote.retrieveOccupantByPassport(passportNum);

            List<Reservation> reservations = occupant.getReservations();

            if (reservations.isEmpty()) {
                System.out.println("NO RESERVATIONS MADE UNDER THIS OCCUPANT!");
                System.out.println("returning to front office view...\n");
                return;
            }
            System.out.println("here");

            for (Reservation reservation : reservations) {
                System.out.println("RESERVATIONS MADE BY " + occupant.getName() + ":");
                System.out.printf("%8s%20s%20s%15s\n", "Reservation ID", "Check In Date", "Check Out Date", "Room Type");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String checkInDate = formatter.format(reservation.getCheckInDate());
                String checkOutDate = formatter.format(reservation.getCheckOutDate());
                System.out.printf("%8s%20s%20s%15s\n", reservation.getReservationId(), checkInDate, checkOutDate, reservation.getRoomType());
            }

            System.out.println("Which reservation would you like to perform check in for? > ");
            Long reservationIdInput = sc.nextLong();
            sc.nextLine();

            Reservation curReservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationIdInput);

            // PRINT EXCEPTIONS MESSAGE IF PRESENT.
            List<Report> reports = curReservation.getReports();
            if (!reports.isEmpty()) {
                for (Report report : reports) {
                    if (report.getType() == 1) {
                        System.out.println("Type 1 Room Allocation Exception! There are no available rooms available for reserved room type,"
                                + " allocated upgrade.");
                    } else {
                        System.out.println("Type 2 Room Allocation Exception! There are no available rooms available for reserved room type,"
                                + " and no upgrade is available.");
                    }
                }
                System.out.println();
            }

            //displaying room info
            List<Room> roomsAllocated = curReservation.getRooms();

            int count = 1;
            for (Room room : roomsAllocated) {
                System.out.println("***Room Allocation " + count + "***");
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Room Size: " + room.getRoomType().getRoomSize());
                System.out.println("Room Status: " + room.getRoomStatus() + "\n");
                count++;
            }

        } catch (OccupantNotFoundException | ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    // use case 26
    public void checkOutGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please key in Room Number > ");
        Integer roomNumber = sc.nextInt();
        sc.nextLine();

        try {
            reservationEntitySessionBeanRemote.checkOut(roomNumber);
            System.out.println("Successfully checked out of room " + roomNumber + "!\n");

        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
