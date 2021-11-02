/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidInputException;

/**
 *
 * @author ryyant
 */
@Local
public interface EmployeeEntitySessionBeanLocal {
    
    public Employee employeeLogin(String username, String password) throws EmployeeNotFoundException;
    
    public List<Employee> viewAllEmployees() throws EmployeeNotFoundException;

    public long createNewEmployee(Employee employee) throws DuplicateException;

}
