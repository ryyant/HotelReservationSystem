/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewRoom(String roomNumber, String roomTypeName) throws RoomTypeNotFoundException, DuplicateException {
        try {

            Room room = new Room(roomNumber);

            RoomType roomType = (RoomType) em.createQuery("SELECT r from RoomType r WHERE r.name = ?1")
                    .setParameter(1, roomTypeName)
                    .getSingleResult();

            room.setRoomType(roomType);
            em.persist(room);
            em.flush();

            return room.getRoomId();

        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("Room Type does not exist!");

        } catch (PersistenceException e) {
            throw new DuplicateException("Room already created!\n");
        }

    }

    @Override
    public List<Room> viewAllRooms() throws RoomNotFoundException {

        List<Room> rooms = em.createQuery("SELECT r from Room r")
                .getResultList();

        // Check if room type list is empty
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("No rooms currently!\n");
        }

        return rooms;
    }

    @Override
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        try {
            Room room = (Room) em.createQuery("SELECT r from Room r WHERE r.roomNumber = ?1")
                    .setParameter(1, roomNumber)
                    .getSingleResult();
            room.getRoomType();
            room.getReservation();
            return room;

        } catch (NoResultException e) {
            throw new RoomNotFoundException("Room does not exist!\n");
        }
    }

    @Override
    public void updateRoom(Room room) {
        em.merge(room);
    }

    @Override
    public void deleteRoom(Room room) {

        Room managedRoom = em.merge(room);

        if (room.getReservation() != null) {
            managedRoom.setEnabled(false);
            managedRoom.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
        } else {
            em.remove(managedRoom);
        }
    }

    @Override
    public List<RoomType> searchRoom(int numOfRoomsReq, Date checkInDate, Date checkOutDate) throws RoomNotFoundException {

        // Room Type : Count Required for search.
        HashMap<RoomType, Integer> roomTypeToQty = new HashMap<>();

        // list of available rooms
        List<Room> availRooms = new ArrayList<>();

        // list of available room types
        List<RoomType> availRoomTypes = new ArrayList<>();

        // get all ROOM that are enabled and available
        List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.roomStatus = ?1 AND r.enabled = ?2")
                .setParameter(1, RoomStatusEnum.AVAILABLE)
                .setParameter(2, true)
                .getResultList();

        // add free rooms to new list
        for (Room r : rooms) {
            // get those with no reservations
            Reservation res = r.getReservation();
            if (res == null) {
                availRooms.add(r);
            } // those with reservations: checkInDateInput >= reservation.checkOutDate OR those with reservations: checkOutDateInput <= reservation.checkInDate
            else if (res.getCheckOutDate().compareTo(checkInDate) < 0 || res.getCheckInDate().compareTo(checkOutDate) > 0) {
                availRooms.add(r);
            }
        }

        // add all the distinct room types of these avail rooms
        for (Room r : availRooms) {
            RoomType roomType = r.getRoomType();
            if (roomTypeToQty.containsKey(roomType)) {
                int qty = roomTypeToQty.get(roomType);
                roomTypeToQty.put(roomType, qty + 1);
            } else {
                roomTypeToQty.put(roomType, 1);
            }
        }

        // check if avail room types are SUFFICIENT
        for (RoomType rt : roomTypeToQty.keySet()) {
            rt.getRoomRates().size(); // LAZY LOADING
            if (roomTypeToQty.get(rt) >= numOfRoomsReq) {
                availRoomTypes.add(rt);
            }
        }

        // exception if no rooms left for any type
        if (roomTypeToQty.isEmpty()) {
            throw new RoomNotFoundException("Insufficient rooms available for this time period at the moment!\n");
        }

        return availRoomTypes;
    }

    @Override
    public double onlineDayPrevailingRate(Date date, RoomType roomType) {

        List<RoomRate> roomRates = roomType.getRoomRates();

        double normalRate = 0;
        double peakRate = 0;
        double promotionRate = 0;
        
        for (RoomRate r : roomRates) {

            if (r.getEnabled()) {

                if (r.getRateType() == RateTypeEnum.NORMAL) {
                    normalRate = r.getRatePerNight();
                }

                if (r.getValidityStartDate() != null || r.getValidityEndDate() != null) {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String startDate = formatter.format(r.getValidityStartDate());
                    String endDate = formatter.format(r.getValidityEndDate());
                    String dayDate = formatter.format(date);
                    System.out.println("startDate " + startDate);
                    System.out.println("endDate " + endDate);
                    System.out.println("dayDate " + dayDate);

                    // 4 CONDIIIONS: 
                    // DAY DATE => VALIDITYSTARTDATE OR DAY DATE == VALIDITY START DATE (DATE STRING)
                    // DAY DATE <= VALIDITYENDDATE OR DAY DATE == VALIDITY END DATE (DATE STRING)
                    boolean timeValid = r.getValidityStartDate().compareTo(date) <= 0 && r.getValidityEndDate().compareTo(date) >= 0;
                    boolean dateValid = startDate.compareTo(dayDate) == 0 || endDate.compareTo(dayDate) == 0;
                    System.out.println("timevalid " + timeValid);
                    System.out.println("dateValid" + dateValid);

                    if (timeValid && dateValid) {
                        if (r.getRateType() == RateTypeEnum.PEAK) {
                            normalRate = r.getRatePerNight();
                        }

                        if (r.getRateType() == RateTypeEnum.PROMOTION) {
                            normalRate = r.getRatePerNight();
                        }
                    }

                }

            }
        }

        if (promotionRate > 0) {
            return promotionRate;
        } else if (peakRate > 0) {
            return peakRate;
        } else {
            return normalRate;
        }

    }

    @Override
    public double walkInDayPrevailingRate(Date date, RoomType roomType) {

        List<RoomRate> roomRates = roomType.getRoomRates();

        double publishedRate = 0;

        for (RoomRate r : roomRates) {

            if (r.getEnabled()) {

                if (r.getRateType() == RateTypeEnum.PUBLISHED) {
                    publishedRate = r.getRatePerNight();
                }
            }

        }

        return publishedRate;

    }

}
