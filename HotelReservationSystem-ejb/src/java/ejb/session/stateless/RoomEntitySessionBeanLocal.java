/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RoomStatusEnum;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface RoomEntitySessionBeanLocal {

    public List<Room> viewAllRooms() throws RoomNotFoundException;

    public long createNewRoom(int roomNumber, Long roomTypeId, Long roomRateId) throws DuplicateException;

    public void updateRoom(int roomNumber, int newRoomNumber, RoomStatusEnum newRoomStatus) throws RoomNotFoundException;

    public void deleteRoom(int roomNumber) throws RoomNotFoundException;
    
}
