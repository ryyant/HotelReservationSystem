/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewRoomType(RoomType roomType, List<RoomRate> roomRates) throws DuplicateException {
        try {
            em.persist(roomType);
            em.flush();
            roomType.setRoomRates(roomRates);
            return roomType.getRoomTypeId();

        } catch (PersistenceException ex) {
            throw new DuplicateException("Room type already created!\n");
        }
    }

    @Override
    public RoomType viewRoomTypeDetails(String roomTypeName) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = (RoomType) em.createQuery("SELECT r FROM RoomType r WHERE r.name = ?1")
                    .setParameter(1, roomTypeName)
                    .getSingleResult();

            return roomType;

        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No Such Room Type!\n");
        }

    }

    @Override
    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException {

        List<RoomType> roomTypes = em.createQuery("SELECT r from RoomType r")
                .getResultList();

        // Check if room type list is empty
        if (roomTypes.isEmpty()) {
            throw new RoomTypeNotFoundException("No room types currently!\n");
        }

        return roomTypes;

    }

    @Override
    public void updateRoomType(RoomType roomType) {
        em.merge(roomType);
    }

    @Override
    public void deleteRoomType(RoomType roomType) {

        RoomType managedRoomType = em.merge(roomType);

        List<RoomType> roomTypesUsedInRoom = em.createQuery("SELECT r from Room r WHERE r.roomType.roomTypeId = ?1")
                .setParameter(1, roomType.getRoomTypeId())
                .getResultList();

        List<RoomType> roomTypesInReservation = em.createQuery("SELECT r from Reservation r WHERE r.roomType.roomTypeId = ?1")
                .setParameter(1, roomType.getRoomTypeId())
                .getResultList();

        List<RoomRate> roomTypesInRoomRate = em.createQuery("SELECT r from RoomRate r WHERE r.roomType.roomTypeId = ?1")
                .setParameter(1, roomType.getRoomTypeId())
                .getResultList();

        // Check if room type is used
        if (roomTypesUsedInRoom.size() > 0 || roomTypesInReservation.size() > 0 || roomTypesInRoomRate.size() > 0) {
            // mark as disabled if used
            managedRoomType.setEnabled(false);
        } else {
            em.remove(managedRoomType);
        }
    }

    @Override
    public void setNextHigherRoomType(Long roomTypeId, Long nextHigherRoomTypeId) throws RoomTypeNotFoundException {

        try {
            RoomType nextHigherRoomTypeEntity = em.find(RoomType.class, nextHigherRoomTypeId);
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            roomType.setNextHigherRoomType(nextHigherRoomTypeEntity);

        } catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("Room Type not found!");
        }
    }
}
