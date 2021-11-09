/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author user
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public Reservation reserveRoom(Long roomId, Guest currentGuest);

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    
}
