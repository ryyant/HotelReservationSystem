/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Occupant;
import entity.Reservation;
import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class OccupantEntitySessionBean implements OccupantEntitySessionBeanRemote, OccupantEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public Occupant retrieveOccupantByNameAndPassport(String name, String passportNum) throws OccupantNotFoundException {

        try {
            Query query = em.createQuery("SELECT o FROM Occupant WHERE o.name := name AND o.passportNumber := passportNumber ");
            query.setParameter("name", name).setParameter("passportNumber", passportNum);
            Occupant occupant = (Occupant) query.getSingleResult();
            int size = occupant.getReservations().size();
            if (size != 0) {

                for (Reservation reservation : occupant.getReservations()) {
                    reservation.getRooms().size();
                    reservation.getReport();
                    reservation.getRoomType();
                    
                    List<Room> rooms = reservation.getRooms();
                    for (Room room : rooms) {
                        room.getRoomType();
                    }
                }
            }

            return occupant;

        } catch (Exception ex) {
            throw new OccupantNotFoundException("Occupant with name " + name + " and passport number " + passportNum + " cannot be found!");
        }

    }
}
