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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public Long createReservation(Reservation reservation) {
        em.persist(reservation);
        em.flush();

        return reservation.getReservationId();
    }

    public Reservation reserveRoom(Long roomTypeId, int quantity, Guest currentGuest, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate) {

        currentGuest = em.merge(currentGuest);
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        double amount = priceMapping.get(roomType);
        Reservation newReservation = new Reservation(amount, quantity, checkInDate, checkOutDate);
        em.persist(newReservation);

        newReservation.setRoomType(roomType);
        newReservation.setOccupant(currentGuest);

        currentGuest.getReservations().add(newReservation);

        return newReservation;

    }

    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation with ID: " + reservationId + " does not exist!");
        }
    }
}
