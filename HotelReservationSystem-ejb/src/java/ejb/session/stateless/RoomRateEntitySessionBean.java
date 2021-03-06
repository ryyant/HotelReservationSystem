/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import static java.lang.System.out;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class RoomRateEntitySessionBean implements RoomRateEntitySessionBeanRemote, RoomRateEntitySessionBeanLocal {

    @EJB(name = "RoomTypeEntitySessionBeanLocal")
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewRoomRate(RoomRate roomRate, String roomTypeName) throws DuplicateException, RoomTypeNotFoundException {
        try {
            RoomType roomType = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = ?1")
                    .setParameter(1, roomTypeName)
                    .getSingleResult();
            em.persist(roomRate);
            roomRate.setRoomType(roomType);
            roomType.getRoomRates().add(roomRate);

            em.flush();
            return roomRate.getRoomRateId();

        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("Room Type does not exist!");
        } catch (PersistenceException e) {
            throw new DuplicateException("Room rate already created!\n");
        }
    }

    @Override
    public RoomRate viewRoomRateDetails(String roomRateName) throws RoomRateNotFoundException {
        try {
            RoomRate roomRate = (RoomRate) em.createQuery("SELECT r FROM RoomRate r WHERE r.name = ?1")
                    .setParameter(1, roomRateName)
                    .getSingleResult();

            return roomRate;

        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("No Such Room Rate!\n");
        }

    }

    @Override
    public List<RoomRate> viewAllRoomRates() throws RoomRateNotFoundException {

        List<RoomRate> roomRates = em.createQuery("SELECT r from RoomRate r")
                .getResultList();

        // Check if room type list is empty
        if (roomRates.isEmpty()) {
            throw new RoomRateNotFoundException("No room rates currently!\n");
        }

        return roomRates;

    }

    @Override
    public void updateRoomRate(RoomRate roomRate) {
        em.merge(roomRate);
    }

    @Override
    public void deleteRoomRate(RoomRate roomRate) {

        List<RoomType> roomTypes = em.createQuery("SELECT r from RoomType r")
                .getResultList();

        boolean used = false;
        for (RoomType roomType : roomTypes) {
            if (roomType.getRoomRates().contains(roomRate)) {
                used = true;
            }
        }

        if (used) {
            roomRate.setEnabled(false);
        } else {
            em.remove(roomRate);
        }

    }
}
