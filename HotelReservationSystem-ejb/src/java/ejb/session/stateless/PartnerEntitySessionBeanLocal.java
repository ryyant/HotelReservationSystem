/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface PartnerEntitySessionBeanLocal {

    public long createNewPartner(String username, String password) throws DuplicateException;

    public List<Employee> viewAllPartners() throws PartnerNotFoundException;
    
}
