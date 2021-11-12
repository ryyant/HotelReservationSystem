/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface RoomEntitySessionBeanLocal {

    public List<Room> viewAllRooms() throws RoomNotFoundException;

    public void updateRoom(Room room);

    public long createNewRoom(int roomNumber, String roomTypeName) throws RoomTypeNotFoundException, DuplicateException;

    public Room getRoomByRoomNumber(int roomNumber) throws RoomNotFoundException;

    public void deleteRoom(Room room);

    public double onlineDayPrevailingRate(Date date, RoomType roomType);

    public double walkInDayPrevailingRate(Date date, RoomType roomType);

    public List<RoomType> searchRoom(int numOfRoomsReq, Date checkInDate, Date checkOutDate) throws RoomNotFoundException;

}
