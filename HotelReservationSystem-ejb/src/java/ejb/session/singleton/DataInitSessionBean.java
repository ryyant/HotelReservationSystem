/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.InvalidInputException;

/**
 *
 * @author ryyant
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "EmployeeEntitySessionBeanLocal")
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            if(em.find(Employee.class, 1l) == null) {

                Long employeeId = employeeEntitySessionBeanLocal.createNewEmployee(new Employee("admin", "password", UserRoleEnum.SYSTEM_ADMIN));
            }
        } catch (DuplicateException | InvalidInputException e) {
            System.out.println(e.getMessage());
        } 
    }

}
