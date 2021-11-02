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

/**
 *
 * @author ryyant
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
        
    public Employee employeeLogin(String username, String password) throws EmployeeNotFoundException;
    
    public List<Employee> viewAllEmployees() throws EmployeeNotFoundException;

    public long createNewEmployee(Employee employee) throws DuplicateException;

}