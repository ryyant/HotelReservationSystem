/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Partner;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.enumeration.RoomStatusEnum;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;

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

    public Reservation reserveRoom(Long roomTypeId, int quantity, Partner partner, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate) {

        partner = em.merge(partner);
        RoomType roomType = em.find(RoomType.class, roomTypeId);

        double amount = priceMapping.get(roomType);
        Reservation newReservation = new Reservation(amount, quantity, checkInDate, checkOutDate);
        em.persist(newReservation);

        newReservation.setRoomType(roomType);
        newReservation.setPartner(partner);

        partner.getReservations().add(newReservation);

        return newReservation;

    }

    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {

        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            reservation.getOccupant();
            reservation.getPartner();
            reservation.getRoomType();
            reservation.getRooms().size();
            List<Room> rooms = reservation.getRooms();
            for (Room r : rooms) {
                r.getRoomType();
            }
            reservation.getReports().size();
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation with ID: " + reservationId + " does not exist!\n");
        }
    }

    @Override
    public List<Reservation> retrieveReservationsByOccupantId(Long occupantId) throws ReservationNotFoundException {

        Occupant occupant = em.find(Occupant.class, occupantId);
        
        System.out.println(occupant);

        occupant.getReservations().size();
        
        System.out.println("here");
        System.out.println(occupant.getReservations());
        
        List<Reservation> reservations = occupant.getReservations();

        System.out.println(reservations.toString());

        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                reservation.getRoomType();
            }
            return reservations;
        } else {
            throw new ReservationNotFoundException("You do not have any reservations made!\n");
        }
    }

    @Override
    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId) throws ReservationNotFoundException {

        Partner partner = em.find(Partner.class, partnerId);

        List<Reservation> reservations = partner.getReservations();

        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                reservation.getRoomType();
            }
            return reservations;
        } else {
            throw new ReservationNotFoundException("You do not have any reservations made!\n");
        }
    }

    @Override
    public List<Reservation> getAllReservations() {

        List<Reservation> reservations = em.createQuery("SELECT r from Reservation r")
                .getResultList();

        for (Reservation r : reservations) {
            r.getReports().size();
            r.getOccupant();
            r.getPartner();
            r.getRoomType();
            r.getRooms().size();
        }

        return reservations;
    }

    // TIMER method to loop all reservations at 2am
    // ADD TIMER ANNOTATION HERE
    @Override
    public void allocateCurrentDayReservations() {

        Date now = new Date();

        // get all reservations checking in today
        List<Reservation> reservations = em.createQuery("SELECT r from Reservation r WHERE r.checkInDate = ?1")
                .setParameter(1, now)
                .getResultList();

        // LOOP ALL RESERVATIONS
        for (Reservation r : reservations) {
            allocateRoomsForReservation(r);
        }
    }

    // MANUAL TRIGGER
    @Override
    public void allocateCurrentDayReservations(Date futureDate) {

        // get all reservations checking in today
        List<Reservation> reservations = em.createQuery("SELECT r from Reservation r WHERE r.checkInDate = ?1")
                .setParameter(1, futureDate)
                .getResultList();

        // LOOP ALL RESERVATIONS
        for (Reservation r : reservations) {
            allocateRoomsForReservation(r);
        }
    }

    // allocate rooms for ONE reservation
    @Override
    public void allocateRoomsForReservation(Reservation reservation) {

        // manage the reservation
        reservation = em.merge(reservation);
        int requiredQuantity = reservation.getQuantity();
        List<Room> allocations = new ArrayList<>();
        allocations.addAll(reservation.getRooms());

        // check if already allocated previously for this day's reservation (same day check-in)
        if (reservation.getRooms().size() > 0) {
            return;
        }
        System.out.println("getting rooms...");

        // get available rooms of reservation's room type
        List<Room> requiredRooms = em.createQuery("SELECT r from Room r WHERE r.roomType = ?1 AND r.enabled = ?2 AND r.roomStatus = ?3")
                .setParameter(1, reservation.getRoomType())
                .setParameter(2, true)
                .setParameter(3, RoomStatusEnum.AVAILABLE)
                .getResultList();

        System.out.println(requiredRooms);

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
        System.out.println(requiredRooms);

        // allocate required rooms
        int index = 0;
        while (requiredQuantity > 0) {
            if (index >= requiredRooms.size()) {
                break;
            }
            Room room = requiredRooms.get(index);
            allocations.add(room);
            room.setReservation(reservation);
            room.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
            requiredQuantity--;
            index++;

        }

        // allocate upgrade rooms
        index = 0;
        while (requiredQuantity > 0) {
            if (index >= higherRooms.size()) {
                break;
            }
            Room higherRoom = higherRooms.get(index);
            allocations.add(higherRoom);
            higherRoom.setReservation(reservation);
            higherRoom.setRoomStatus(RoomStatusEnum.NOT_AVAILABLE);
            Report report = new Report(1);
            em.persist(report);
            reservation.getReports().add(report);
            requiredQuantity--;
            index++;

        }

        // if no more rooms and higher rooms, type 2 exception
        System.out.println(requiredQuantity);

        while (requiredQuantity > 0) {
            Report report = new Report(2);
            em.persist(report);
            reservation.getReports().add(report);
            requiredQuantity--;
        }
        System.out.println(requiredQuantity);

        // set allocations to reservation
        reservation.setRooms(allocations);
    }

    @Override
    public void checkOut(String ppNum, String roomNumber) throws RoomNotFoundException {
        try {
            Room room = (Room) em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = ?1")
                    .setParameter(1, roomNumber)
                    .getSingleResult();

            Occupant occupant = (Occupant) em.createQuery("SELECT r FROM Occupant r WHERE r.passportNumber = ?1")
                    .setParameter(1, ppNum)
                    .getSingleResult();

            if (!occupant.getReservations().contains(room.getReservation())) {
                throw new RoomNotFoundException("Room does not belong to occupant!");
            }

            room.setRoomStatus(RoomStatusEnum.AVAILABLE);
            Reservation reservation = room.getReservation();
            reservation.getRooms().remove(room);
            room.setReservation(null);

            // if after diassociating reservation and string for room checkout,
            // no more rooms left to checkout in reservation,
            // remove reservation.
            if (reservation.getRooms().isEmpty()) {
                // remove reservation record
                Partner p = reservation.getPartner();
                if (p != null) {
                    reservation.setPartner(null);
                    p.getReservations().remove(reservation);

                }
                Occupant o = reservation.getOccupant();
                if (o != null) {
                    reservation.setOccupant(null);
                    o.getReservations().remove(reservation);
                }

                reservation.setRooms(null);
                reservation.setReports(null);
                em.remove(reservation);
            }

        } catch (NoResultException ex) {
            throw new RoomNotFoundException("This room / occupant does not exist!\n");
        }
    }

}
