/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author user
 */
@Local
public interface OccupantEntitySessionBeanLocal {
    
    public Occupant retrieveOccupantByPassport(String passportNum) throws OccupantNotFoundException;

    public Occupant occupantRegister(Occupant walkInGuest) throws DuplicateException;

}
