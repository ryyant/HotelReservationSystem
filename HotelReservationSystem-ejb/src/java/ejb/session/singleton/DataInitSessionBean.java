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
            em.persist(new Employee("sysadmin", "password", UserRoleEnum.SYSTEM_ADMIN));
            em.persist(new Employee("opmanager", "password", UserRoleEnum.OPERATION_MANAGER));
            em.persist(new Employee("salesmanager", "password", UserRoleEnum.SALES_MANAGER));
            em.persist(new Employee("guestrelo", "password", UserRoleEnum.RELATION_OFFICER));
        }

        if (em.find(RoomType.class, 1l) == null) {

            // LOAD ROOM TYPES
            RoomType grandSuiteType = new RoomType("Grand Suite");
            grandSuiteType.setNextHigherRoomType(null);
            em.persist(grandSuiteType);

            RoomType juniorSuiteType = new RoomType("Junior Suite");
            juniorSuiteType.setNextHigherRoomType(grandSuiteType);
            em.persist(juniorSuiteType);

            RoomType familyRoomType = new RoomType("Family Room");
            familyRoomType.setNextHigherRoomType(juniorSuiteType);
            em.persist(familyRoomType);

            RoomType premierRoomType = new RoomType("Premier Room");
            premierRoomType.setNextHigherRoomType(familyRoomType);
            em.persist(premierRoomType);

            RoomType deluxeRoomType = new RoomType("Deluxe Room");
            deluxeRoomType.setNextHigherRoomType(premierRoomType);
            em.persist(deluxeRoomType);

            RoomRate roomRate = null;

            // DELUXE ROOM RATES
            roomRate = new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, 50.0);
            roomRate.setRoomType(deluxeRoomType);
            deluxeRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);
            roomRate = new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, 100.0);
            roomRate.setRoomType(deluxeRoomType);
            deluxeRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);

            // PREMIER ROOM TYPES
            roomRate = new RoomRate("Premier Room Normal", RateTypeEnum.NORMAL, 100.0);
            roomRate.setRoomType(premierRoomType);
            premierRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);
            roomRate = new RoomRate("Premier Room Published", RateTypeEnum.PUBLISHED, 200.0);
            roomRate.setRoomType(premierRoomType);
            premierRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);

            // FAMILY ROOM TYPES
            roomRate = new RoomRate("Family Room Normal", RateTypeEnum.NORMAL, 150.0);
            roomRate.setRoomType(familyRoomType);
            familyRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);
            roomRate = new RoomRate("Family Room Published", RateTypeEnum.PUBLISHED, 300.0);
            roomRate.setRoomType(familyRoomType);
            familyRoomType.getRoomRates().add(roomRate);
            em.persist(roomRate);

            // JUNIOR SUITE TYPES
            roomRate = new RoomRate("Junior Suite Normal", RateTypeEnum.NORMAL, 200.0);
            roomRate.setRoomType(juniorSuiteType);
            juniorSuiteType.getRoomRates().add(roomRate);
            em.persist(roomRate);
            roomRate = new RoomRate("Junior Suite Published", RateTypeEnum.PUBLISHED, 400.0);
            roomRate.setRoomType(juniorSuiteType);
            juniorSuiteType.getRoomRates().add(roomRate);
            em.persist(roomRate);

            // GRAND SUITE TYPES
            roomRate = new RoomRate("Grand Suite Normal", RateTypeEnum.NORMAL, 250.0);
            roomRate.setRoomType(grandSuiteType);
            grandSuiteType.getRoomRates().add(roomRate);
            em.persist(roomRate);
            roomRate = new RoomRate("Grand Suite Published", RateTypeEnum.PUBLISHED, 500.0);
            roomRate.setRoomType(grandSuiteType);
            grandSuiteType.getRoomRates().add(roomRate);
            em.persist(roomRate);

            // ROOMS
            Room room;

            room = new Room("0101");
            room.setRoomType(deluxeRoomType);
            em.persist(room);
            em.flush();
            room = new Room("0201");
            room.setRoomType(deluxeRoomType);
            em.persist(room);
            room = new Room("0301");
            room.setRoomType(deluxeRoomType);
            em.persist(room);
            room = new Room("0401");
            room.setRoomType(deluxeRoomType);
            em.persist(room);
            room = new Room("0501");
            room.setRoomType(deluxeRoomType);
            em.persist(room);
            em.flush();

            room = new Room("0102");
            room.setRoomType(premierRoomType);
            em.persist(room);
            room = new Room("0202");
            room.setRoomType(premierRoomType);
            em.persist(room);
            room = new Room("0302");
            room.setRoomType(premierRoomType);
            em.persist(room);
            room = new Room("0402");
            room.setRoomType(premierRoomType);
            em.persist(room);
            room = new Room("0502");
            room.setRoomType(premierRoomType);
            em.persist(room);

            room = new Room("0103");
            room.setRoomType(familyRoomType);
            em.persist(room);
            room = new Room("0203");
            room.setRoomType(familyRoomType);
            em.persist(room);
            room = new Room("0303");
            room.setRoomType(familyRoomType);
            em.persist(room);
            room = new Room("0403");
            room.setRoomType(familyRoomType);
            em.persist(room);
            room = new Room("0503");
            room.setRoomType(familyRoomType);
            em.persist(room);

            room = new Room("0104");
            room.setRoomType(juniorSuiteType);
            em.persist(room);
            room = new Room("0204");
            room.setRoomType(juniorSuiteType);
            em.persist(room);
            room = new Room("0304");
            room.setRoomType(juniorSuiteType);
            em.persist(room);
            room = new Room("0404");
            room.setRoomType(juniorSuiteType);
            em.persist(room);
            room = new Room("0504");
            room.setRoomType(juniorSuiteType);
            em.persist(room);

            room = new Room("0105");
            room.setRoomType(grandSuiteType);
            em.persist(room);
            room = new Room("0205");
            room.setRoomType(grandSuiteType);
            em.persist(room);
            room = new Room("0305");
            room.setRoomType(grandSuiteType);
            em.persist(room);
            room = new Room("0405");
            room.setRoomType(grandSuiteType);
            em.persist(room);
            room = new Room("0505");
            room.setRoomType(grandSuiteType);
            em.persist(room);

        }

    }
}
