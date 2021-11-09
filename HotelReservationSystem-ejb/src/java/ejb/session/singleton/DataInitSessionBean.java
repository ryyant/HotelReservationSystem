/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.enumeration.UserRoleEnum;

/**
 *
 * @author ryyant
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {

        if (em.find(Employee.class, 1l) == null) {
            em.persist(new Employee("admin", "password", UserRoleEnum.SYSTEM_ADMIN));
    
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date start;
        Date end;
        try {
            start = dateFormat.parse("02-10-2010");
            end = dateFormat.parse("02-11-2010");

            RoomRate roomRate = new RoomRate("name", RateTypeEnum.NORMAL, 200.0, start, end);
            if (em.find(RoomRate.class, 1l) == null) {
                em.persist(roomRate);
 

                List<String> amenities = new ArrayList<>(Arrays.asList("toothbrush"));
                RoomType roomType = new RoomType("Deluxe Room", "description", "3", "1", 5, amenities, true);
                roomType.getRoomRates().add(roomRate);
                if (em.find(RoomType.class, 1l) == null) {
                    em.persist(roomType);
                   
                }
                Room room = new Room(0101, RoomStatusEnum.AVAILABLE, true);
                room.setRoomType(roomType);
                if (em.find(Room.class, 1l) == null) {
                    em.persist(room);
                    

                }
            }
        } catch (ParseException ex) {
            System.out.println("Invalid Input!");
        }

    }
}
