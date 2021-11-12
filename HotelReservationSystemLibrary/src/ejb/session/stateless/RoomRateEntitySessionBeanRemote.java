/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ryyant
 */
@Remote
public interface RoomRateEntitySessionBeanRemote {

   public long createNewRoomRate(RoomRate roomRate, String roomTypeName) throws DuplicateException, RoomTypeNotFoundException;

    public RoomRate viewRoomRateDetails(String roomRateName) throws RoomRateNotFoundException;

    public List<RoomRate> viewAllRoomRates() throws RoomRateNotFoundException;

    public void updateRoomRate(RoomRate roomRate);

    public void deleteRoomRate(RoomRate roomRate);
}
