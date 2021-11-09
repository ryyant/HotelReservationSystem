/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ryyant
 */
@Remote
public interface GuestEntitySessionBeanRemote {

    public Guest guestLogin(String username, String password) throws GuestNotFoundException;
    public Guest guestRegister(String usernameInput, String passwordInput, String nameInput, String emailInput, String phoneNumberInput, String passportNumberInput) throws DuplicateException;

}
