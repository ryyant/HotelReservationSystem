/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import entity.Occupant;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.enumeration.RoomStatusEnum;
import util.exception.DuplicateException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author user
 */
@WebService(serviceName = "ReservationWebService")
@Stateless()
public class ReservationWebService {

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(String username, String password) throws PartnerNotFoundException {
        return partnerEntitySessionBeanLocal.partnerLogin(username, password);
    }

    @WebMethod(operationName = "viewAllPartners")
    public List<Partner> viewAllPartners() throws PartnerNotFoundException {
        return partnerEntitySessionBeanLocal.viewAllPartners();
    }

    /*@WebMethod(operationName = "retrieveUsernameByPartnerId")
    public long retrieveUsernameByPartnerId(Long partnerId) throws PartnerNotFoundException {
        return 1;
    }*/
    @WebMethod(operationName = "searchRoom")
    public List<RoomType> searchRoom(int numOfRoomsReq, Date checkInDate, Date checkOutDate) throws RoomNotFoundException {
        List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.roomStatus = ?1 AND r.enabled = ?2")
                .setParameter(1, RoomStatusEnum.AVAILABLE)
                .setParameter(2, true)
                .getResultList();
        for (Room room : rooms) {
            em.detach(room);
            //room.getReservation().setRooms(null);
            room.setReservation(null);

            for (RoomRate roomRate : room.getRoomType().getRoomRates()) {
                em.detach(roomRate);
                roomRate.setRoomType(null);
            }
        }

        return roomEntitySessionBeanLocal.searchRoom(numOfRoomsReq, checkInDate, checkOutDate);
    }

    @WebMethod(operationName = "reserveRoom")
    public Reservation reserveRoom(Long roomTypeId, int quantity, Partner partner, HashMap<RoomType, Double> priceMapping, Date checkInDate, Date checkOutDate) {
        return reservationEntitySessionBeanLocal.reserveRoom(roomTypeId, quantity, partner, priceMapping, checkInDate, checkOutDate);
    }

    @WebMethod(operationName = "allocateRoomsForReservation")
    public void allocateRoomsForReservation(Reservation reservation) {
        reservationEntitySessionBeanLocal.allocateRoomsForReservation(reservation);
    }

    @WebMethod(operationName = "retrieveReservationByReservationId")
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        return reservationEntitySessionBeanLocal.retrieveReservationByReservationId(reservationId);
    }

    @WebMethod(operationName = "retrieveReservationsByPartnerId")
    public List<Reservation> retrieveReservationsByPartnerId(Long partnerId) throws ReservationNotFoundException {
        return reservationEntitySessionBeanLocal.retrieveReservationsByPartnerId(partnerId);
    }

    @WebMethod(operationName = "")
    public double onlineDayPrevailingRate(Date date, RoomType roomType) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        System.out.println(date);
        cal.setTime(date);
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);

        date = toDate(xmlDate);
        
        return roomEntitySessionBeanLocal.onlineDayPrevailingRate(date, roomType);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public static Date toDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }

}
