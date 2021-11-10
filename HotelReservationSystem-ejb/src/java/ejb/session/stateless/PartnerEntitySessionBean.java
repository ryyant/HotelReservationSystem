/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
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
}