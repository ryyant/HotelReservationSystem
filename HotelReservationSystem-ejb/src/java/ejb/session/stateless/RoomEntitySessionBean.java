/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
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
    public long createNewRoom(int roomNumber, Long roomTypeId, Long roomRateId) throws DuplicateException {
        try {
            Room room = new Room(roomNumber, RoomStatusEnum.AVAILABLE, true);
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            room.setRoomType(roomType);
            RoomRate roomRate = em.find(RoomRate.class, roomRateId);
            room.setRoomRate(roomRate);
            em.persist(room);
            em.flush();
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
    public void updateRoom(int roomNumber, int newRoomNumber, RoomStatusEnum newRoomStatus) throws RoomNotFoundException {

        try {
            Room room = (Room) em.createQuery("SELECT r from Room r WHERE r.roomNumber = ?1")
                    .setParameter(1, roomNumber)
                    .getSingleResult();

            if (newRoomNumber > 0) {
                room.setRoomNumber(newRoomNumber);
            }
            if (newRoomStatus != null) {
                room.setRoomStatus(newRoomStatus);
            }

        } catch (NoResultException e) {
            throw new RoomNotFoundException("Room does not exist!");
        }

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

}
