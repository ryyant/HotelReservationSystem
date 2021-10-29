/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.DuplicateException;
import util.exception.PartnerNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

  @Override
    public long createNewRoomType(RoomType roomType) throws DuplicateException {
        try {
            
            // Check if partner already exists
            List<RoomType> roomTypes = em.createQuery("SELECT r from RoomType r WHERE r.name = ?1")
                    .setParameter(1, roomType.getName())
                    .getResultList();
            
            if (roomTypes.size() == 1) {
                throw new DuplicateException("Room Type Created!");
            }
          
            // create room type instance and persist to db
            
            em.persist(roomType);
            em.flush();
            return roomType.getRoomTypeId();
            
        } catch (DuplicateException e) {
            throw e;
        
        }
    }

    @Override
    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException {
        try {
            
            List<RoomType> roomTypes = em.createQuery("SELECT r from RoomType r")
                    .getResultList();
            
            // Check if room type list is empty
            if (roomTypes.size() == 0) {
                throw new RoomTypeNotFoundException("No Room Types currently!");
            }
            
            return roomTypes;
            
        } catch (RoomTypeNotFoundException e) {
            throw e;
        }
    }
    
    @Override 
    public RoomType viewRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            if(roomType == null) {
                throw new RoomTypeNotFoundException("No Such Room Type!");
            } else {
                return roomType;
            }
            
        } catch (RoomTypeNotFoundException e) {
            throw e;
        }
        
    }
    
    
    
    
    
}
