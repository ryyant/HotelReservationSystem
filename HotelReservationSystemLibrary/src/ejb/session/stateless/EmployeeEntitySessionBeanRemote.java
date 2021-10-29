/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidInputException;

/**
 *
 * @author ryyant
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
        
    public long employeeLogin(String username, String password) throws EmployeeNotFoundException;
    
    public List<Employee> viewAllEmployees() throws EmployeeNotFoundException;

    public long createNewEmployee(String username, String password, String userRole) throws DuplicateException, InvalidInputException;

}