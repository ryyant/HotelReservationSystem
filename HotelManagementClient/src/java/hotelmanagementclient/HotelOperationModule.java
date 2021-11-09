package hotelmanagementclient;

import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class HotelOperationModule {

    private Employee currentEmployeeEntity;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    public HotelOperationModule() {
    }

    public HotelOperationModule(Employee currentEmployeeEntity, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
    }

    public void menuHotelOperation() {

        if (currentEmployeeEntity.getUserRole() == UserRoleEnum.OPERATION_MANAGER) {
            operationManagerView();
        }

        if (currentEmployeeEntity.getUserRole() == UserRoleEnum.SALES_MANAGER) {
            operationManagerView();
        }

    }

    public void operationManagerView() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentEmployeeEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** Operation Manager View ***");
            System.out.println("-----------------------");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: Update Room Type");
            System.out.println("4: Delete Room Type");
            System.out.println("5: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("6: Create New Room");
            System.out.println("7: Update Room");
            System.out.println("8: Delete Room");
            System.out.println("9: View All Rooms");
            System.out.println("-----------------------");
            System.out.println("10: View Room Allocation Exception Report");
            System.out.println("-----------------------");
            System.out.println("11: Logout\n");
            int response = 0;

            while (response < 1 || response > 11) {
                System.out.print("> ");
                response = scanner.nextInt();
                System.out.println();
                switch (response) {
                    case 1:
                        createNewRoomType();
                        break;
                    case 2:
                        viewRoomTypeDetails();
                        break;
                    case 3:
                        deleteRoomType();
                        break;
                    case 4:
                        updateRoomType();
                        break;
                    case 5:
                        viewAllRoomTypes();
                        break;
                    case 6:
                        createNewRoom();
                        break;
                    case 7:
                        updateRoom();
                        break;
                    case 8:
                        deleteRoom();
                        break;
                    case 9:
                        viewAllRooms();
                        break;
                    case 10:
                        //viewRoomAllocationExceptionReport();
                        break;
                    case 11:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    public void salesManagerView() throws EmployeeNotFoundException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentEmployeeEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** Sales Manager View ***");
            System.out.println("-----------------------");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("6: Logout\n");
            int response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                System.out.println();

                switch (response) {
                    case 1:
                        createNewRoomRate();
                        break;
                    case 2:
                        viewRoomRateDetails();
                        break;
                    case 3:
                        updateRoomRate();
                        break;
                    case 4:
                        deleteRoomRate();
                        break;
                    case 5:
                        viewAllRoomRates();
                        break;
                    case 6:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }

        }
    }

    // use case 7
    private void createNewRoomType() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Create New Room Type *****");
        System.out.print("Enter Configuration Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Description: ");
        String description = sc.nextLine().trim();
        System.out.print("Enter Size: ");
        String size = sc.nextLine().trim();
        System.out.print("Enter Bed: ");
        String bed = sc.nextLine().trim();
        System.out.print("Enter Capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();
        List<String> amenities = new ArrayList<>();
        System.out.println("Enter Amenities: ");
        System.out.println("(Press Enter after keying each amenity)");
        System.out.println("(Press C to confirm)");
        String a = sc.nextLine().trim();
        while (!a.toUpperCase().equals("C")) {
            amenities.add(a);
            a = sc.nextLine().trim();
        }

        RoomType roomType = new RoomType(name, description, size, bed, capacity, amenities, true);
        /*try {
            roomTypeEntitySessionBeanRemote.createNewRoomType(roomType);
            System.out.println("Room Type Successfully Created!\n");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }*/
    }

    // use case 8
    private void viewRoomTypeDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** View Room Type Details *****");
        System.out.print("Enter Room Type Configuration Name: ");
        String roomTypeInput = sc.nextLine().trim();
        try {
            RoomType roomType = roomTypeEntitySessionBeanRemote.viewRoomTypeDetails(roomTypeInput);
            System.out.println("Room Type Id: " + roomType.getRoomTypeId());
            System.out.println("Room Type Name: " + roomType.getName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Room Size: " + roomType.getRoomSize());
            System.out.println("No of Bed: " + roomType.getBed());
            System.out.println("Room Capacity: " + roomType.getCapacity());
            System.out.println("Amenities: ");
            for (String a : roomType.getAmenities()) {
                System.out.println(a);
            }
            System.out.println();

        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 9
    private void updateRoomType() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Update Room Type *****");
        System.out.print("Enter Room Type Configuration Name: ");
        String roomTypeInput = sc.nextLine().trim();
        try {
            RoomType roomType = roomTypeEntitySessionBeanRemote.viewRoomTypeDetails(roomTypeInput);
            System.out.println("Room Type Id: " + roomType.getRoomTypeId());
            System.out.println("Room Type Name: " + roomType.getName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Room Size: " + roomType.getRoomSize());
            System.out.println("No of Bed: " + roomType.getBed());
            System.out.println("Room Capacity: " + roomType.getCapacity());
            System.out.println("Amenities: ");
            for (String a : roomType.getAmenities()) {
                System.out.println(a);
            }
            System.out.println();

        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("What would you like to update?");
        System.out.println("1. Name \n 2. Description \n 3. Room Size \n 4. No of Bed \n 5. Room Capacity \n 6. Amenities");
        int updateChoice = 0;
        updateChoice = sc.nextInt();
        System.out.println();

        if (updateChoice >= 1 && updateChoice <= 6) {

        }

    }

    // use case 10
    private void deleteRoomType() {

    }

    // use case 11
    private void viewAllRoomTypes() {
        System.out.println("***** View All Room Types *****");
        try {
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.viewAllRoomTypes();
            for (RoomType r : roomTypes) {
                System.out.println("Room Type Id: " + r.getRoomTypeId() + ", Room Type Configuration: " + r.getName());
            }
            System.out.println();
        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 12
    private void createNewRoom() {

    }

    // use case 13
    private void updateRoom() {

    }

    // use case 14
    private void deleteRoom() {

    }

    // use case 15
    private void viewAllRooms() {
        System.out.println("***** View All Rooms *****");
        try {
            List<Room> rooms = roomEntitySessionBeanRemote.viewAllRooms();
            for (Room r : rooms) {
                System.out.println("Room Id: " + r.getRoomId() + ", Room Number: " + r.getRoomNumber() + ", Room Status: " + r.getRoomStatus());
            }
            System.out.println();
        } catch (RoomNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 17
    private void createNewRoomRate() {

    }

    // use case 18
    private void viewRoomRateDetails() {

    }

    // use case 19
    private void updateRoomRate() {

    }

    // use case 20
    private void deleteRoomRate() {

    }

    // use case 21
    private void viewAllRoomRates() {
        System.out.println("***** View All Room Rates *****");
        try {
            List<RoomRate> roomRates = roomRateEntitySessionBeanRemote.viewAllRoomRates();
            for (RoomRate r : roomRates) {
                // System.out.println("RoomRate Id: " + r.getRoomRateId() + ", Name: " + r.getName() + ", Room Status: " + r.getRoomStatus());
            }
            System.out.println();
        } catch (RoomRateNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
