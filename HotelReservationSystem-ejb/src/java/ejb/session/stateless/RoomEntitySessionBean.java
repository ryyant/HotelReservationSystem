/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.enumeration.RoomStatusEnum;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewRoom(int roomNumber, Long roomTypeId) throws DuplicateException {
        try {
            Room room = new Room(roomNumber, RoomStatusEnum.AVAILABLE, true);
            em.persist(room);
            em.flush();

            RoomType roomType = em.find(RoomType.class, roomTypeId);
            room.setRoomType(roomType);
            return room.getRoomId();

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
    public void updateRoom(Room room) throws RoomNotFoundException {
        em.merge(room);
    }

    @Override
    public void deleteRoom(int roomNumber) throws RoomNotFoundException {

        try {
            Room room = (Room) em.createQuery("SELECT r from Room r WHERE r.roomNumber = ?1")
                    .setParameter(1, roomNumber)
                    .getSingleResult();

            // no reservation
            if (room.getReservation()
                    == null) {
                em.remove(room);
            } else {
                room.setEnabled(false);
            }

        } catch (NoResultException e) {
            throw new RoomNotFoundException("Room does not exist!");
        }
    }
    

    @Override
    public List<Room> searchRoom(Date checkInDateInput, Date checkOutDateInput) throws RoomNotFoundException {

        // get all available rooms
        List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.roomStatus = ?1 AND r.enabled = TRUE")
                .setParameter(1, RoomStatusEnum.AVAILABLE)
                .getResultList();

        List<Room> availRooms = new ArrayList<>();

        for (Room r : rooms) {
            r.getRoomType();
            r.getReservation();

            Reservation res = r.getReservation();

            // get those with no reservations
            if (res == null) {
                availRooms.add(r);
            }

            // those with reservations: checkInDateInput >= reservation.checkOutDate
            // OR those with reservations: checkOutDateInput <= reservation.checkInDate
            else if (res.getCheckOutDate().compareTo(checkInDateInput) < 0 || res.getCheckInDate().compareTo(checkOutDateInput) > 0) {
                availRooms.add(r);
            }

        }
        
        if (availRooms.isEmpty()) {
            throw new RoomNotFoundException("No rooms available for this time period at the moment!");
        }

        return availRooms;
    }
    
    @Override
    public void allocateRooms() {
        Date now = new Date();
        
        // get all reservations checking in today
        List<Reservation> reservations = em.createQuery("SELECT r from Reservation r WHERE checkInDate = ?1")
                .setParameter(1, now)
                .getResultList();

        for (Reservation r : reservations) {
            
            Room room = r.getRoom();
            
            if (room.getRoomStatus().equals("AVAILABLE")) {
                room.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
            } else {
                // automatically allocate upgrade logic here
                
                
                //Report report = new Report(e);
            }
        }
        
        
        
        
        
    }
    

}
