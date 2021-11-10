/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.Room;
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

    public Reservation reserveRoom(Long roomId, Guest currentGuest) {
        Reservation newReservation = new Reservation();
        Room room = em.find(Room.class, roomId);

        newReservation.getRooms().add(room);
        newReservation.setOccupant(currentGuest);

        currentGuest.getReservations().size();
        currentGuest.getReservations().add(newReservation);
        
        return newReservation;

    }

    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation with ID : " + reservationId + " does not exist!");
        }
    }
}
