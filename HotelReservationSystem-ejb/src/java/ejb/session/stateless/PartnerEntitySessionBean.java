/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewPartner(String username, String password) throws DuplicateException {
        try {
            Partner partner = new Partner(username, password);
            em.persist(partner);
            em.flush();
            return partner.getPartnerId();

        } catch (PersistenceException e) {
            throw new DuplicateException("Username taken!\n");
        }
    }

    /*@Override 
    public String retrieveUsernameByPartnerId(Long partnerId) {
        Partner
    }
     */
    @Override
    public List<Partner> viewAllPartners() throws PartnerNotFoundException {
        List<Partner> partners = em.createQuery("SELECT p from Partner p")
                .getResultList();

        // Check if employee list is empty
        if (partners.isEmpty()) {
            throw new PartnerNotFoundException("No partners currently!\n");
        }

        return partners;
    }

    @Override
    public Partner partnerLogin(String username, String password) throws PartnerNotFoundException {
        try {

            // Check if username and password correct
            Partner partner = (Partner) em.createQuery("SELECT p from Partner p WHERE p.username = ?1 AND p.password = ?2")
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getSingleResult();

            // lazy fetching
            partner.getReservations().size();
            for (Reservation r : partner.getReservations()) {
                r.getRoomType();
            }

            return partner;

        } catch (NoResultException e) {
            throw new PartnerNotFoundException("Wrong username / password!\n");
        }
    }
}
