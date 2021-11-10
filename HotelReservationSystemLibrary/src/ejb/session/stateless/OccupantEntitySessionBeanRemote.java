/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import javax.ejb.Remote;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface OccupantEntitySessionBeanRemote {

    public Occupant retrieveOccupantByNameAndPassport(String name, String passportNum) throws OccupantNotFoundException;
    
}
