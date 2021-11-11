/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public Reservation reserveRoom(Long roomTypeId, int quantity, Guest currentGuest, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate);

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

}
