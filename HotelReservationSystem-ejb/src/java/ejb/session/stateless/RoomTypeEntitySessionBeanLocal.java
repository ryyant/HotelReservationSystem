/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author user
 */
@Local
public interface RoomTypeEntitySessionBeanLocal {

    public long createNewRoomType(RoomType roomType) throws DuplicateException;
    
    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException;
    
    public RoomType viewRoomTypeDetails(String roomTypeInput) throws RoomTypeNotFoundException;

    public void deleteRoomType(String roomTypeName) throws RoomTypeNotFoundException;

    public void updateRoomType() throws RoomTypeNotFoundException;
    
}
