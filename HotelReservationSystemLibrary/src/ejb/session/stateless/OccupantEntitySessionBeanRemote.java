/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface OccupantEntitySessionBeanRemote {

    public Occupant occupantRegister(Occupant walkInGuest) throws DuplicateException;

    public Occupant retrieveOccupantByPassport(String passportNum) throws OccupantNotFoundException;

}
