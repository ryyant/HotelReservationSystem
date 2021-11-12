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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    public long createNewRoom(int roomNumber, String roomTypeName) throws RoomTypeNotFoundException, DuplicateException {
        try {
            Room room = new Room(roomNumber);
            em.persist(room);
            RoomType roomType = (RoomType) em.createQuery("SELECT r from RoomType r WHERE r.name = ?1")
                    .setParameter(1, roomTypeName)
                    .getSingleResult();

            room.setRoomType(roomType);
            return room.getRoomId();

        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("Room Type does not exist!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
    public Room getRoomByRoomNumber(int roomNumber) throws RoomNotFoundException {
        try {
            Room room = (Room) em.createQuery("SELECT r from Room r WHERE r.roomNumber = ?1")
                    .setParameter(1, roomNumber)
                    .getSingleResult();

            return room;

        } catch (NoResultException e) {
            throw new RoomNotFoundException("Room does not exist!");
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
    public HashMap<RoomType, Double> searchRoom(String searchType, Date checkInDateInput, Date checkOutDateInput) throws RoomNotFoundException {

        // RoomType Name : Amount for period stated.
        HashMap<RoomType, Double> map = new HashMap<>();
        List<Room> availRooms = new ArrayList<>();
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
            else if (res.getCheckOutDate().compareTo(checkInDateInput) < 0 || res.getCheckInDate().compareTo(checkOutDateInput) > 0) {
                availRooms.add(r);
            }
        }

        // add all the distinct room types of these avail rooms
        for (Room r : availRooms) {
            RoomType roomType = r.getRoomType();
            if (!availRoomTypes.contains(roomType)) {
                availRoomTypes.add(roomType);
            }
        }

        if (availRooms.isEmpty()) {
            throw new RoomNotFoundException("No rooms available for this time period at the moment!\n");
        }

        // loop room types available, check the total amount for all the days
        for (RoomType roomType : availRoomTypes) {
            if (roomType.getEnabled()) {
                Calendar checkInCal = Calendar.getInstance();
                checkInCal.setTime(checkInDateInput);
                System.out.println("checkInCal: " + checkInCal.getTime());

                Calendar checkOutCal = Calendar.getInstance();
                checkOutCal.setTime(checkOutDateInput);
                System.out.println("checkOutCal: " + checkOutCal.getTime());
                
                double amount = 0;
                System.out.println("avail room types: " + roomType);

                while (checkInCal.before(checkOutCal)) {
                    checkInCal.add(Calendar.DAY_OF_MONTH, 1);
                    Date d = checkInCal.getTime();
                    if (searchType.equals("Online")) {
                        amount += onlineDayPrevailingRate(d, roomType);
                    }
                    if (searchType.equals("Walk-in")) {
                        amount += walkInDayPrevailingRate(d, roomType);
                    }
                }
                map.put(roomType, amount);
            }

        }

        return map;
    }

    @Override
    public double onlineDayPrevailingRate(Date date, RoomType roomType) {

        List<RoomRate> roomRates = roomType.getRoomRates();

        double normalRate = 0;
        double peakRate = 0;
        double promotionRate = 0;

        for (RoomRate r : roomRates) {
            System.out.println(r.getName());

            if (r.getEnabled()) {

                if (r.getRateType() == RateTypeEnum.NORMAL) {
                    normalRate = r.getRatePerNight();
                }

                if (r.getValidityStartDate() != null || r.getValidityEndDate() != null) {
                    boolean typeValid = r.getValidityStartDate().compareTo(date) <= 0 && r.getValidityEndDate().compareTo(date) >= 0;
                    if (r.getRateType() == RateTypeEnum.PEAK && typeValid) {
                        normalRate = r.getRatePerNight();
                    }

                    if (r.getRateType() == RateTypeEnum.PROMOTION && typeValid) {
                        normalRate = r.getRatePerNight();
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
            System.out.println(r.getName());

            if (r.getEnabled()) {

                if (r.getRateType() == RateTypeEnum.PUBLISHED) {
                    publishedRate = r.getRatePerNight();
                }
            }

        }

        return publishedRate;

    }

}
