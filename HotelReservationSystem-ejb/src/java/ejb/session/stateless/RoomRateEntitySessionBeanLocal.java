/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface RoomRateEntitySessionBeanLocal {

    public long createNewRoomType(RoomRate roomRate) throws DuplicateException;

    public RoomRate viewRoomRateDetails(String roomRateName) throws RoomRateNotFoundException;

    public List<RoomRate> viewAllRoomRates() throws RoomRateNotFoundException;

    public void updateRoomRate(RoomRate roomRate);

    public void deleteRoomRate(RoomRate roomRate);
    
}
