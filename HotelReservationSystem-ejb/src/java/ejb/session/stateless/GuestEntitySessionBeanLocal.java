/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface GuestEntitySessionBeanLocal {

    public Guest guestLogin(String username, String password) throws GuestNotFoundException;

    public Guest guestRegister(String usernameInput, String passwordInput, String nameInput, String emailInput, String phoneNumberInput, String passportNumberInput) throws DuplicateException;

}
