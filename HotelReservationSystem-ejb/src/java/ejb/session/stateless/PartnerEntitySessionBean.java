/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
            
            // Check if partner already exists
            List<Partner> partners = em.createQuery("SELECT p from Partner p WHERE p.username = ?1")
                    .setParameter(1, username)
                    .getResultList();
            
            if (partners.size() == 1) {
                throw new DuplicateException();
            }
          
            // create employee instance and persist to db
            Partner partner = new Partner(username, password);
            em.persist(partner);
            em.flush();
            return partner.getEmployeeId();
            
        } catch (DuplicateException e) {
            throw new DuplicateException("Username taken!");
        }
    }
    
    @Override
    public List<Employee> viewAllPartners() throws PartnerNotFoundException {
        try {
            List<Partner> partners = em.createQuery("SELECT p from Partner p")
                    .getResultList();
            
            // Check if employee list is empty
            if (partner.size() == 0) {
                throw new PartnerNotFoundException();
            }
            
            return employees;
            
        } catch (PartnerNotFoundException e) {
            throw new PartnerNotFoundException("No partners currently!");
        }
    }
    

}
