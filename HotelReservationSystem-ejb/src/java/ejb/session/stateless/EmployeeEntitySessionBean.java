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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Employee employeeLogin(String username, String password) throws EmployeeNotFoundException {
        try {

            // Check if username and password correct
            Employee employee = (Employee) em.createQuery("SELECT e from Employee e WHERE e.username = ?1 AND e.password = ?2")
                    .setParameter(1, username)
                    .setParameter(2, password)
                    .getSingleResult();

            return employee;

        } catch (NoResultException e) {
            throw new EmployeeNotFoundException("Wrong username / password!\n");
        }
    }

    @Override
    public long createNewEmployee(Employee newEmployee) throws DuplicateException {

        try {
            em.persist(newEmployee);
            em.flush();
            return newEmployee.getEmployeeId();
        } catch (PersistenceException e) {
            throw new DuplicateException("Username taken!\n");
        }

    }

    @Override
    public List<Employee> viewAllEmployees() throws EmployeeNotFoundException {
        List<Employee> employees = em.createQuery("SELECT e from Employee e")
                .getResultList();

        // Check if employee list is empty
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees currently!\n");
        }

        return employees;
    }

}
