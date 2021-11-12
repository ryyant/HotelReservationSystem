/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Reservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DuplicateException;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class OccupantEntitySessionBean implements OccupantEntitySessionBeanRemote, OccupantEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public Occupant occupantRegister(Occupant walkInGuest) throws DuplicateException {
        try {
            em.persist(walkInGuest);
            em.flush();
            return walkInGuest;
        } catch (PersistenceException e) {
            throw new DuplicateException("Occupant exists!");
        }
    }


    public Occupant retrieveOccupantByPassport(String passportNum) throws OccupantNotFoundException {

        try {
            Query query = em.createQuery("SELECT o FROM Occupant WHERE o.passportNumber := passportNumber ");
            query.setParameter("passportNumber", passportNum);
            Occupant occupant = (Occupant) query.getSingleResult();
            int size = occupant.getReservations().size();
            
            if (size != 0) {
                for (Reservation reservation : occupant.getReservations()) {
                    reservation.getRooms().size();
                    reservation.getReport();
                    reservation.getRoomType();
                }
            }

            return occupant;

        } catch (Exception ex) {
            throw new OccupantNotFoundException("Occupant cannot be found!");
        }

    }
}
