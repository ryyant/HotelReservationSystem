/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Occupant;
import entity.Reservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public Guest guestRegister(String usernameInput, String passwordInput, String nameInput, String emailInput, String phoneNumberInput, String passportNumberInput) throws DuplicateException {
        Guest guest = new Guest(usernameInput, passwordInput, nameInput,  emailInput, phoneNumberInput, passportNumberInput);
        
       try {
           em.persist(guest);
           em.flush();
           return guest;
       } catch (PersistenceException e) {
           throw new DuplicateException("Guest exists!");
       }
    }

    @Override
    public Guest guestLogin(String username, String password) throws GuestNotFoundException {
        try {

            // Check if username and password correct
            Guest guest = (Guest) em.createQuery("SELECT g from Guest g WHERE g.username = ?1 AND g.password = ?2")
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getSingleResult();
            
            // lazy fetching
            guest.getReservations().size();
            for (Reservation r : guest.getReservations()) {
                r.getRoomType();
            }

            return guest;

        } catch (NoResultException e) {
            throw new GuestNotFoundException("Wrong username / password!\n");
        }
    }
    
}
