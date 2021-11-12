/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Report;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.RoomStatusEnum;
import util.exception.ReportNotFoundException;
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

    public Reservation reserveRoom(Long roomTypeId, int quantity, Occupant occupant, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate) {

        occupant = em.merge(occupant);
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        double amount = priceMapping.get(roomType);
        Reservation newReservation = new Reservation(amount, quantity, checkInDate, checkOutDate);
        em.persist(newReservation);

        newReservation.setRoomType(roomType);
        newReservation.setOccupant(occupant);

        occupant.getReservations().add(newReservation);

        return newReservation;

    }

    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {

        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            reservation.getRoomType();

            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation with ID: " + reservationId + " does not exist!");
        }
    }

    @Override
    public List<Reservation> retrieveReservationsByOccupantId(Long occupantId) throws ReservationNotFoundException {

        Occupant occupant = em.find(Occupant.class, occupantId);

        List<Reservation> reservations = occupant.getReservations();

        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                reservation.getRoomType();
            }
            return reservations;
        } else {
            throw new ReservationNotFoundException("You do not have any reservations made!");
        }
    }

    @Override
    public List<Report> getAllReports() throws ReportNotFoundException {
        List<Report> reports = em.createQuery("SELECT r from Report r")
                .getResultList();

        if (reports.isEmpty()) {
            throw new ReportNotFoundException("There are currently no room allocation reports!");
        }

        return reports;
    }

    // TIMER method to loop all reservations at 2am
    // ADD TIMER ANNOTATION HERE
    @Override
    public void allocateCurrentDayReservations() {

        Date now = new Date();

        // get all reservations checking in today
        List<Reservation> reservations = em.createQuery("SELECT r from Reservation r WHERE checkInDate = ?1")
                .setParameter(1, now)
                .getResultList();

        // LOOP ALL RESERVATIONS
        for (Reservation r : reservations) {
            allocateRoomsForReservation(r);
        }
    }

    // allocate rooms for ONE reservation
    @Override
    public void allocateRoomsForReservation(Reservation reservation) {

        reservation = em.merge(reservation);
        int requiredQuantity = reservation.getQuantity();
        List<Room> allocations = reservation.getRooms();

        // get available rooms of reservation's room type
        List<Room> requiredRooms = em.createQuery("SELECT r from Room r WHERE r.roomType = ?1 AND r.enabled = ?2 AND r.roomStatus = ?3")
                .setParameter(1, reservation.getRoomType())
                .setParameter(2, true)
                .setParameter(3, RoomStatusEnum.AVAILABLE)
                .getResultList();

        // get available rooms of reservation's higher room type
        RoomType higherRoomType = reservation.getRoomType().getNextHigherRoomType();
        List<Room> higherRooms = new ArrayList<>();
        if (higherRoomType != null) {
            higherRooms = em.createQuery("SELECT r from Room r WHERE r.roomType = ?1 AND r.enabled = ?2 AND r.roomStatus = ?3")
                    .setParameter(1, higherRoomType)
                    .setParameter(2, true)
                    .setParameter(3, RoomStatusEnum.AVAILABLE)
                    .getResultList();
        }

        // if enough required rooms available, allocate.
        if (requiredRooms.size() >= requiredQuantity) {
            // allocate required rooms 
            for (int i = 0; i < requiredQuantity; i++) {
                Room room = requiredRooms.get(i);
                allocations.add(room);
                room.setReservation(reservation);
                room.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
            }
            return;
        }

        // if enough higher rooms available, allocate.
        if (higherRooms.size() >= requiredQuantity) {
            // allocate upgrade rooms
            for (int i = 0; i < requiredQuantity; i++) {
                Room higherRoom = higherRooms.get(i);
                allocations.add(higherRoom);
                higherRoom.setReservation(reservation);
                higherRoom.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
                Report report = new Report(1);
                reservation.setReport(report);
            }
            return;
        }

        // if no more rooms and higher rooms, type 2 exception
        Report report = new Report(2);
        reservation.setReport(report);

    }
}
