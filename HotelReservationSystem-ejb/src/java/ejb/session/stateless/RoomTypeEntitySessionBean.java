/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    public long createNewRoomType(RoomType roomType) throws DuplicateException {
        try {
            em.persist(roomType);
            em.flush();
            return roomType.getRoomTypeId();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DuplicateException("Room type already created!\n");
        }
    }

    @Override
    public RoomType viewRoomTypeDetails(String roomTypeInput) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = (RoomType) em.createQuery("SELECT r FROM RoomType r WHERE r.name = ?1")
                    .setParameter(1, roomTypeInput)
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

}
