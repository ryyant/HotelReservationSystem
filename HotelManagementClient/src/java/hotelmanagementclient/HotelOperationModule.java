package hotelmanagementclient;

import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import entity.Report;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.ReportNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class HotelOperationModule {

    private Employee currentEmployeeEntity;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    public HotelOperationModule() {
    }

    public HotelOperationModule(Employee currentEmployeeEntity, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
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
                        viewRoomAllocationExceptionReport();
                        break;
                    case 9:
                        System.out.println("Logged out!\n");
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
        System.out.println("Enter Amenities: ");
        String a = sc.nextLine().trim();
        List<String> amenities = Arrays.asList(a.split(", "));

        // System.out.println("Choose Room Rates: ");
        RoomType roomType = new RoomType(name, description, size, bed, capacity, amenities);

        try {
            System.out.println("Choose Next Room Types,");
            System.out.println("0: NO next higher room type");
            List<RoomType> options = roomTypeEntitySessionBeanRemote.viewAllRoomTypes();
            for (RoomType r : options) {
                System.out.println(r.getRoomTypeId() + ": " + r.getName());
            }
            System.out.print("Enter Next Higher Room Type:");
            Long higherRoom = sc.nextLong();

            Long roomTypeId = roomTypeEntitySessionBeanRemote.createNewRoomType(roomType, null);
            if (higherRoom > 0) {
                roomTypeEntitySessionBeanRemote.setNextHigherRoomType(roomTypeId, higherRoom);
            }
            System.out.println("Room Type Successfully Created!\n");
        } catch (DuplicateException | RoomTypeNotFoundException e) {
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
            if (roomType.getAmenities().isEmpty()) {
                System.out.println("No amenities currently.");
            }
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
        System.out.println("2. Description");
        System.out.println("3. Size");
        System.out.println("4. Bed");
        System.out.println("5. Capacity");
        System.out.println("6. Add a Amenity");
        System.out.println("7. Remove a Amenity");
        System.out.println("8. Enable Room");

        int action = sc.nextInt();
        sc.nextLine();

        if (action == 1) {
            System.out.print("New Room Type Name >");
            String newName = sc.nextLine();
            roomType.setName(newName);
        }
        if (action == 2) {
            System.out.print("Description >");
            String desc = sc.nextLine();
            roomType.setDescription(desc);
        }
        if (action == 3) {
            System.out.print("New Size >");
            String size = sc.nextLine();
            roomType.setRoomSize(size);
        }
        if (action == 4) {
            System.out.print("New Bed >");
            String bed = sc.nextLine();
            roomType.setBed(bed);
        }
        if (action == 5) {
            System.out.print("New Capacity >");
            int cap = sc.nextInt();
            sc.nextLine();
            roomType.setCapacity(cap);
        }
        if (action == 6) {
            System.out.print("Add Amenity >");
            String a = sc.nextLine();
            roomType.getAmenities().add(a);
        }
        if (action == 7) {
            System.out.print("Remove Amenity >");
            String a = sc.nextLine();
            roomType.getAmenities().remove(a);
        }
        if (action == 8) {
            System.out.print("Enable Room Type? (Y/N) >");
            String enableInput = sc.nextLine().trim();
            if (enableInput.equalsIgnoreCase("Y")) {
                roomType.setEnabled(true);
            }
            if (enableInput.equalsIgnoreCase("N")) {
                roomType.setEnabled(false);
            }
        }

        roomTypeEntitySessionBeanRemote.updateRoomType(roomType);
        System.out.println("Room Type succesfully updated!");

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

            // MERGE HERE
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
            System.out.println("Room succesfully deleted!\n");
        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
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

    // use case 14
    private void viewRoomAllocationExceptionReport() {
        Scanner sc = new Scanner(System.in);
        System.out.println("***** View Room Allocation Reports *****");
        List<Reservation> reservations = reservationEntitySessionBeanRemote.getAllReservations();
        for (Reservation r : reservations) {
            List<Report> reports = r.getReports();
            for (Report report : reports) {
                System.out.println("Report: " + report.getReportId() + ", type: " + report.getType());
            }

        }
        if (reservations.size() == 0) {
            System.out.println("No Exception Reports found Currently!\n");
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
            switch (rateTypeInput) {
                case "PUBLISHED":
                    rateType = RateTypeEnum.PUBLISHED;
                    break;
                case "NORMAL":
                    rateType = RateTypeEnum.NORMAL;
                    break;
                case "PEAK":
                    rateType = RateTypeEnum.PEAK;
                    break;
                case "PROMOTION":
                    rateType = RateTypeEnum.PROMOTION;
                    break;
                default:
                    System.out.println("Invalid Rate Type, try again!\n");
                    break;
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
            System.out.println("ENTER to go Back\n");
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date validityStartDate, validityEndDate;

        System.out.println("***** Update Room Rate *****");
        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Rate Type");
        System.out.println("3. Rate Per Night");
        System.out.println("4. Validity Start Date");
        System.out.println("5. Validity End Date");
        System.out.println("6. Enable Room Rate\n");

        int action = sc.nextInt();
        sc.nextLine();

        if (action == 1) {
            System.out.print("New Room Name >");
            String newName = sc.nextLine();
            roomRate.setName(newName);
        }
        if (action == 2) {
            RateTypeEnum rateType = null;
            while (rateType == null) {
                System.out.print("Enter Rate Type (PUBLISHED/NORMAL/PEAK/PROMOTION) > ");
                String rateTypeInput = sc.nextLine();
                switch (rateTypeInput) {
                    case "PUBLISHED":
                        rateType = RateTypeEnum.PUBLISHED;
                        break;
                    case "NORMAL":
                        rateType = RateTypeEnum.NORMAL;
                        break;
                    case "PEAK":
                        rateType = RateTypeEnum.PEAK;
                        break;
                    case "PROMOTION":
                        rateType = RateTypeEnum.PROMOTION;
                        break;
                    default:
                        System.out.println("Invalid Rate Type, try again!\n");
                        break;
                }
            }
            roomRate.setRateType(rateType);
        }
        if (action == 3) {
            System.out.print("New Room Rate Per Night (e.g. 150.0) >");
            double ratePerNight = sc.nextDouble();
            sc.nextLine();
            roomRate.setRatePerNight(ratePerNight);
        }
        if (action == 4) {
            System.out.print("Validity Start Date >");
            try {
                validityStartDate = dateFormat.parse(sc.nextLine());
                roomRate.setValidityStartDate(validityStartDate);
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (action == 5) {
            System.out.print("Validity End Date >");
            try {
                validityEndDate = dateFormat.parse(sc.nextLine());
                roomRate.setValidityEndDate(validityEndDate);
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (action == 6) {
            System.out.print("Enable Room Rate? (Y/N) >");
            String enableInput = sc.nextLine().trim();
            if (enableInput.equalsIgnoreCase("Y")) {
                roomRate.setEnabled(true);
            }
            if (enableInput.equalsIgnoreCase("N")) {
                roomRate.setEnabled(false);
            }
        }

        // MERGE HERE
        roomRateEntitySessionBeanRemote.updateRoomRate(roomRate);
        System.out.println("Room succesfully updated!\n");
    }
    // use case 20

    private void deleteRoomRate(RoomRate roomRate) {
        System.out.println("***** Delete Room Rate *****");
        roomRateEntitySessionBeanRemote.deleteRoomRate(roomRate);
        System.out.println("Room Rate succesfully deleted!\n");
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
