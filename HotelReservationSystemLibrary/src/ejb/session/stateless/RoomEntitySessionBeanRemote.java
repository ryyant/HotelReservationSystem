/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ryyant
 */
@Remote
public interface RoomEntitySessionBeanRemote {


    public List<Room> viewAllRooms() throws RoomNotFoundException;

    public void updateRoom(Room room);

    public long createNewRoom(int roomNumber, String roomTypeName) throws RoomTypeNotFoundException, DuplicateException;
    
    public void allocateRooms();

    public Room getRoomByRoomNumber(int roomNumber) throws RoomNotFoundException;

    public void deleteRoom(Room room);

    public double onlineDayPrevailingRate(Date date, RoomType roomType);

    public HashMap<RoomType, Double> searchRoom(Date checkInDateInput, Date checkOutDateInput) throws RoomNotFoundException;


}
