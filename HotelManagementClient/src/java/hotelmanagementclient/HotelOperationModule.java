package hotelmanagementclient;

import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class HotelOperationModule {

    private Employee currentEmployeeEntity;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    public HotelOperationModule() {
    }

    public HotelOperationModule(Employee currentEmployeeEntity, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote) {
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

            OUTER:
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
                        //deleteRoomType();
                        break;
                    case 4:
                        //updateRoomType();
                        break;
                    case 5:
                        viewAllRoomTypes();
                        break;
                    case 6:
                        //createNewRoom();
                        break;
                    case 7:
                        //updateRoom();
                        break;
                    case 8:
                        //deleteRoom();
                        break;
                    case 9:
                        //viewAllRooms();
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

        while (true) {
            System.out.println("*** Sales Manager View ***");
            System.out.println("-----------------------");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: Update Room Rate");
            System.out.println("3: Delete Room Rate");
            System.out.println("3: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("7: Logout\n");
            int response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();
                System.out.println();

                if (response == 1) {
                } else if (response == 2) {
                } else if (response == 3) {
                } else if (response == 4) {
                } else if (response == 5) {
                } else if (response == 6) {
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
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
        try {
            roomTypeEntitySessionBeanRemote.createNewRoomType(roomType);
            System.out.println("Room Type Successfully Created!\n");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }
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

}
