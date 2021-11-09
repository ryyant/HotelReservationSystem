/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author ryyant
 */
@Remote
public interface RoomEntitySessionBeanRemote {

    public List<Room> viewAllRooms() throws RoomNotFoundException;

    public long createNewRoom(int roomNumber, Long roomTypeId) throws DuplicateException;

    public void deleteRoom(int roomNumber) throws RoomNotFoundException;

    public void updateRoom(Room room) throws RoomNotFoundException;

}
