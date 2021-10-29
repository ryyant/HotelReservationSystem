/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidInputException;

/**
 *
 * @author ryyant
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long employeeLogin(String username, String password) throws EmployeeNotFoundException {
        try {
            
            // Check if username and password correct
            Employee employee = (Employee) em.createQuery("SELECT e from Employee e WHERE e.username = ?1 AND e.password = ?2")
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getSingleResult();
            
            return employee.getEmployeeId();
            
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Wrong username / password!");
        }
    }
    

    @Override
    public long createNewEmployee(String username, String password, String userRole) throws DuplicateException, InvalidInputException {
        try {
            
            // Check if role input fits the 4 different possibilities
            UserRoleEnum userRoleEnum;
            if (userRole.equals("System Admin")) {
                userRoleEnum = UserRoleEnum.SYSTEM_ADMIN;
            } else if (userRole.equals("Operation Manager")) {
                userRoleEnum = UserRoleEnum.OPERATION_MANAGER;
            } else if (userRole.equals("Sales Manager")) {
                userRoleEnum = UserRoleEnum.SALES_MANAGER;
            } else if (userRole.equals("Relation Officer")) {
                userRoleEnum = UserRoleEnum.RELATION_OFFICER;
            } else {
                throw new InvalidInputException();
            }
            
            // Check if employee already exists
            List<Employee> employees = em.createQuery("SELECT e from Employee e WHERE e.username = ?1")
                    .setParameter(1, username)
                    .getResultList();
            
            if (employees.size() == 1) {
                throw new DuplicateException();
            }
          
            // create employee instance and persist to db
            Employee employee = new Employee(username, password, userRoleEnum);
            em.persist(employee);
            em.flush();
            return employee.getEmployeeId();
            
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Invalid Role Input!");
        } catch (DuplicateException e) {
            throw new DuplicateException("Username taken!");
        }
    }
    
    @Override
    public List<Employee> viewAllEmployees() throws EmployeeNotFoundException {
        try {
            List<Employee> employees = em.createQuery("SELECT e from Employee e")
                    .getResultList();
            
            // Check if employee list is empty
            if (employees.size() == 0) {
                throw new EmployeeNotFoundException();
            }
            
            return employees;
            
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException("No employees currently!");
        }
    }
    
}
