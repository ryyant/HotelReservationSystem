/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.OccupantEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import entity.Employee;
import entity.Occupant;
import entity.Report;
import entity.Reservation;
import entity.Room;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.UserRoleEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author ryyant
 */
public class FrontOfficeModule {

    private Employee currentEmployeeEntity;
    private OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(Employee currentEmployeeEntity, OccupantEntitySessionBeanRemote occupantEntitySessionBeanRemote, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote) {
        this.occupantEntitySessionBeanRemote = occupantEntitySessionBeanRemote;
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
                switch (response) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        checkInGuest();
                        break;
                    case 4:
                        checkOutGuest();
                        break;
                    case 5:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    public void checkInGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please key in Occupant Name >");
        String occupantName = sc.nextLine();
        System.out.println("Please key in Occupant Passport Number >");
        String passportNum = sc.nextLine();

        try {
            Occupant occupant = occupantEntitySessionBeanRemote.retrieveOccupantByNameAndPassport(occupantName, passportNum);

            List<Reservation> reservations = occupant.getReservations();

            if (reservations.size() == 0) {
                System.out.println("NO RESERVATIONS MADE UNDER THIS OCCUPANT!");
                System.out.println("-----returning to front office menu--------");
                return;
            }

            for (Reservation reservation : reservations) {
                System.out.println("DISPLAYING EXISINTG RESERVATIONS MADE BY " + occupant.getName() + " :");
                System.out.printf("%8s%20s%20s%15s\n", "Reservation ID", "Check In Date", "Check Out Date", "Room Type");
                System.out.printf("%8s%20s%20s%15s\n", reservation.getReservationId(), reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoomType());
            }

            System.out.println("Which reservation would you like to perform check in for? >");
            Long reservationIdInput = sc.nextLong();

            Reservation currentReservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationIdInput);

            Report exceptionReport = currentReservation.getReport();
            if (exceptionReport == null) {

                // Exception was found, handle the exception accordingly    
            } else {

            }

            //displaying room info
            List<Room> roomsAllocated = currentReservation.getRooms();

            int count = 1;
            for (Room room : roomsAllocated) {

                System.out.println("Here are the details for reservation " + count + " :");
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Room Size: " + room.getRoomType().getRoomSize());
                System.out.println("Room Status: " + room.getRoomStatus());
                count++;

            }

        } catch (OccupantNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void checkOutGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please key in Room Number >");
        Integer occupantName = sc.nextInt();
        
        occupantEntitySessionBeanRemote.
        
        
        
        
    }

}
