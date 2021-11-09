/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Guest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Guest guestLogin(String username, String password) throws GuestNotFoundException {
        try {

            // Check if username and password correct
            Guest guest = (Guest) em.createQuery("SELECT g from Guest g WHERE g.username = ?1 AND g.password = ?2")
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getSingleResult();

            return guest;

        } catch (Exception e) {
            throw new GuestNotFoundException("Wrong username / password!\n");
        }
    }

}
