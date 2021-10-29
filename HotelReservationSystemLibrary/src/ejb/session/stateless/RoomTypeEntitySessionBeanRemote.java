/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author user
 */
@Remote
public interface RoomTypeEntitySessionBeanRemote {
        public long createNewRoomType(RoomType roomType) throws DuplicateException;
        public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException;
        public RoomType viewRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;

}