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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.RateTypeEnum;
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
            em.persist(new Employee("opmanager", "password", UserRoleEnum.OPERATION_MANAGER));
            em.persist(new Employee("salesmanager", "password", UserRoleEnum.SALES_MANAGER));
            em.persist(new Employee("guestrelo", "password", UserRoleEnum.RELATION_OFFICER));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date start;
        Date end;

        try {
            start = dateFormat.parse("02-10-2010");
            end = dateFormat.parse("02-11-2010");

            if (em.find(RoomRate.class, 1l) == null) {

                List<String> amenities = new ArrayList<>();
                amenities.add("toothbrush");
                amenities.add("swimming pool");
                RoomType roomType = new RoomType("Deluxe Room", "description blah bah", "3", "1", 5, amenities);
                em.persist(roomType);

                RoomRate roomRate = null;
                roomRate = new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, 100.0, start, end);
                roomRate.setRoomType(roomType);
                roomType.getRoomRates().add(roomRate);
                em.persist(roomRate);

                roomRate = new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, 50.0, start, end);
                roomRate.setRoomType(roomType);
                roomType.getRoomRates().add(roomRate);
                em.persist(roomRate);

                Room room = new Room(0101);
                room.setRoomType(roomType);
                em.persist(room);
            }

        } catch (ParseException ex) {
            System.out.println("Invalid Input!");
        }

    }
}
