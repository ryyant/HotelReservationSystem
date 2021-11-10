package hotelmanagementclient;

import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
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
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("-----------------------");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("-----------------------");
            System.out.println("9: Logout\n");
            int response = 0;

            while (response < 1 || response > 9) {
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
                        viewAllRoomTypes();
                        break;
                    case 4:
                        createNewRoom();
                        break;
                    case 5:
                        updateRoom();
                        break;
                    case 6:
                        deleteRoom();
                        break;
                    case 7:
                        viewAllRooms();
                        break;
                    case 8:
                        //viewRoomAllocationExceptionReport();
                        break;
                    case 9:
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
            System.out.println("3: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("4: Logout\n");
            int response = 0;

            while (response < 1 || response > 4) {
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
                        viewAllRoomRates();
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
        // System.out.println("Choose Room Rates: ");

        RoomType roomType = new RoomType(name, description, size, bed, capacity, amenities);
        try {
            roomTypeEntitySessionBeanRemote.createNewRoomType(roomType, null);
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
            
            System.out.println("What would you like to do?");
            System.out.println("1. Update Room Type");
            System.out.println("2. Delete Room Type");
            System.out.println("3. Back");
            int action = sc.nextInt();
            sc.nextLine();

            if (action == 1) {
                updateRoomType(roomType);
            }
            if (action == 2) {
                deleteRoomType(roomType);
            }

        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 9
    private void updateRoomType(RoomType roomType) {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Update Room Type *****");
        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Rate Type");
        System.out.println("3. Rate Per Night");
        System.out.println("4. Validity Start Date");
        System.out.println("5. Validity End Date");
        System.out.println("ENTER to go back");
        int updateChoice = 0;
        updateChoice = sc.nextInt();
        System.out.println();

        if (updateChoice >= 1 && updateChoice <= 6) {

        }

    }

    // use case 10
    private void deleteRoomType(RoomType roomType) {
        Scanner sc = new Scanner(System.in);
        System.out.println("***** Delete Room Type *****");
        roomTypeEntitySessionBeanRemote.deleteRoomType(roomType);
        System.out.println("Room Type succesfully deleted!");
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
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Create New Room *****");
        System.out.print("Enter Room Number: ");
        int roomNum = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Room Type Name: ");
        String roomTypeName = sc.nextLine();
        sc.nextLine();

        try {
            roomEntitySessionBeanRemote.createNewRoom(roomNum, roomTypeName);
            System.out.println("Room Type Successfully Created!\n");
        } catch (DuplicateException | RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 13
    private void updateRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.println("***** Update Room *****");
        System.out.print("Enter Room Number to update: ");
        int roomNumber = sc.nextInt();
        System.out.println();

        try {
            Room room = roomEntitySessionBeanRemote.getRoomByRoomNumber(roomNumber);
            System.out.println("What would you like to update?");
            System.out.println("1. Room Number");
            System.out.println("2. Room Status");
            System.out.println("3. Enabled");
            int action = sc.nextInt();
            sc.nextLine();

            if (action == 1) {
                System.out.print("New Room Number (4 DIGITS) >");
                int newRoomNumber = sc.nextInt();
                sc.nextLine();
                room.setRoomNumber(newRoomNumber);
            }
            if (action == 2) {
                System.out.print("New Room Status (1. AVAIL, 2. NOT AVAIL) >");
                int response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    room.setRoomStatus(RoomStatusEnum.AVAILABLE);
                }
                if (response == 2) {
                    room.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
                }
            }
            if (action == 3) {
                System.out.print("Enable Room? (Y/N) >");
                String enableInput = sc.nextLine().trim();
                if (enableInput.equalsIgnoreCase("Y")) {
                    room.setEnabled(true);
                }
                if (enableInput.equalsIgnoreCase("N")) {
                    room.setEnabled(false);
                }
            }
            
            roomEntitySessionBeanRemote.updateRoom(room);
            System.out.println("Room succesfully updated!");
        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // use case 14
    private void deleteRoom() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Delete Room *****");
        System.out.print("Enter Room Number to delete: ");
        int roomNumber = sc.nextInt();
        System.out.println();
        try {
            Room room = roomEntitySessionBeanRemote.getRoomByRoomNumber(roomNumber);
            roomEntitySessionBeanRemote.deleteRoom(room);
            System.out.println("Room succesfully deleted!");
        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // use case 15
    private void viewAllRooms() {
        Scanner sc = new Scanner(System.in);

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
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Create New Room Rate *****");
        System.out.print("Enter Room Rate Name: ");
        String roomRateName = sc.nextLine();

        RateTypeEnum rateType = null;

        while (rateType == null) {
            System.out.print("Enter Rate Type (PUBLISHED/NORMAL/PEAK/PROMOTION): ");
            String rateTypeInput = sc.nextLine();
            if (rateTypeInput.equals("PUBLISHED")) {
                rateType = RateTypeEnum.PUBLISHED;
            } else if (rateTypeInput.equals("NORMAL")) {
                rateType = RateTypeEnum.NORMAL;
            } else if (rateTypeInput.equals("PEAK")) {
                rateType = RateTypeEnum.PEAK;
            } else if (rateTypeInput.equals("PROMOTION")) {
                rateType = RateTypeEnum.PROMOTION;
            } else {
                System.out.println("Invalid Rate Type, try again!");
            }
        }

        System.out.print("Enter Rate Per Night: ");
        double ratePerNight = sc.nextDouble();
        sc.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date validityStartDate, validityEndDate;

        try {
            System.out.print("Enter Validity Start Date: ");
            validityStartDate = dateFormat.parse(sc.nextLine());

            System.out.print("Enter Validity End Date: ");
            validityEndDate = dateFormat.parse(sc.nextLine());

            RoomRate roomRate = new RoomRate(roomRateName, rateType, ratePerNight, validityStartDate, validityEndDate);
            roomRateEntitySessionBeanRemote.createNewRoomRate(roomRate);
            System.out.println("Room Rate Successfully Created!\n");

        } catch (ParseException | DuplicateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // use case 18
    private void viewRoomRateDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** View Room Rate Details *****");
        System.out.print("Enter Room Rate Name: ");
        String roomRateInput = sc.nextLine().trim();
        try {
            RoomRate roomRate = roomRateEntitySessionBeanRemote.viewRoomRateDetails(roomRateInput);
            System.out.println("Room Rate Id: " + roomRate.getRoomRateId());
            System.out.println("Name: " + roomRate.getName());
            System.out.println("Rate Type: " + roomRate.getRateType());
            System.out.println("Rate Per Night: " + roomRate.getRatePerNight());
            System.out.println("Enabled: " + roomRate.getEnabled());
            System.out.println("Validity Start Date: " + roomRate.getValidityStartDate());
            System.out.println("Validity End Date: " + roomRate.getValidityStartDate());
            System.out.println();

            System.out.println("What would you like to do?");
            System.out.println("1. Update Room Rate");
            System.out.println("2. Delete Room Rate");
            System.out.println("ENTER to go Back");
            int action = sc.nextInt();
            sc.nextLine();

            if (action == 1) {
                updateRoomRate(roomRate);
            }
            if (action == 2) {
                deleteRoomRate(roomRate);
            }

        } catch (RoomRateNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 19
    private void updateRoomRate(RoomRate roomRate) {
        Scanner sc = new Scanner(System.in);
        System.out.println("***** Update Room Rate *****");
        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Rate Type");
        System.out.println("3. Rate Per Night");
        System.out.println("4. Validity Start Date");
        System.out.println("5. Validity End Date");
        int action = sc.nextInt();
        sc.nextLine();

        if (action == 1) {
        }
        if (action == 2) {
        }

        roomRateEntitySessionBeanRemote.updateRoomRate(roomRate);
        System.out.println("Room succesfully updated!");
    }

    // use case 20
    private void deleteRoomRate(RoomRate roomRate) {
        Scanner sc = new Scanner(System.in);
        System.out.println("***** Delete Room Rate *****");
        roomRateEntitySessionBeanRemote.deleteRoomRate(roomRate);
        System.out.println("Room Rate succesfully deleted!");
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
