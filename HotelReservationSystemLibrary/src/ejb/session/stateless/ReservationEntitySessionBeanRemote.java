/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public List<Reservation> retrieveReservationsByOccupantId(Long occupantId) throws ReservationNotFoundException;

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    public Reservation reserveRoom(Long roomTypeId, int quantity, Occupant occupant, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate);

    public Reservation reserveRoom(Long roomTypeId, int quantity, Partner partner, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate);

    public List<Reservation> getAllReservations();

    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId) throws ReservationNotFoundException;

    public void allocateCurrentDayReservations();

    public void allocateRoomsForReservation(Reservation r);

    public void allocateCurrentDayReservations(Date futureDate);

    public void checkOut(int roomNumber) throws RoomNotFoundException;

}
