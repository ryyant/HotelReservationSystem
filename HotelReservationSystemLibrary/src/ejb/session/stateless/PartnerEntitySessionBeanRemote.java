/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author ryyant
 */
@Remote
public interface PartnerEntitySessionBeanRemote {
    
    public long createNewPartner(String username, String password) throws DuplicateException;

    public List<Partner> viewAllPartners() throws PartnerNotFoundException;
    
}
