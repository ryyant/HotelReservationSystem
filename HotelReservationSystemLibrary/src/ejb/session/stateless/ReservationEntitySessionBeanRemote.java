/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Report;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReportNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public List<Reservation> retrieveReservationsByOccupantId(Long occupantId) throws ReservationNotFoundException;

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    public Reservation reserveRoom(Long roomTypeId, int quantity, Occupant occupant, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate);

    public List<Report> getAllReports() throws ReportNotFoundException;

    public void allocateCurrentDayReservations();

    public void allocateRoomsForReservation(Reservation r);

}
