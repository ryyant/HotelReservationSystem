/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
import util.exception.DuplicateException;
import util.exception.EmployeeNotFoundException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author ryyant
 */
public class SystemAdministrationModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    private Employee currentEmployeeEntity;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(Employee currentEmployeeEntity, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote) {
        this();
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuSystemAdministration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentEmployeeEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** System Administrator View ***");
            System.out.println("-----------------------");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("-----------------------");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("-----------------------");
            System.out.println("5: Logout\n");
            int response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();
                System.out.println();

                switch (response) {
                    case 1:
                        createNewEmployee();
                        break;
                    case 2:
                        viewAllEmployees();
                        break;
                    case 3:
                        createNewPartner();
                        break;
                    case 4:
                        viewAllPartners();
                        break;
                    case 5:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    // use case 3
    private void createNewEmployee() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Create New Employee *****");
        System.out.println("Enter Username:");
        String usernameInput = sc.nextLine().trim();
        System.out.println("Enter Password:");
        String passwordInput = sc.nextLine().trim();

        System.out.println("Select User Role:");
        System.out.println("1. System Admin");
        System.out.println("2. Operation Manager");
        System.out.println("3. Sales Manager");
        System.out.println("4. Relation Officer\n");
        int response = 0;

        UserRoleEnum userRole = null;

        OUTER:
        while (response < 1 || response > 4) {
            System.out.print("> ");
            response = sc.nextInt();

            switch (response) {
                case 1:
                    userRole = UserRoleEnum.SYSTEM_ADMIN;
                    break;
                case 2:
                    userRole = UserRoleEnum.OPERATION_MANAGER;

                    break;
                case 3:
                    userRole = UserRoleEnum.SALES_MANAGER;
                    break;
                case 4:
                    userRole = UserRoleEnum.RELATION_OFFICER;
                    break;
                default:
                    System.out.println("Invalid option, please try again!\n");
                    break;
            }
        }

        Employee newEmployee = new Employee(usernameInput, passwordInput, userRole);
        try {
            employeeEntitySessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("Employee Successfully Created!\n");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 4
    private void viewAllEmployees() {
        System.out.println("***** View All Employees *****");
        try {
            List<Employee> employees = employeeEntitySessionBeanRemote.viewAllEmployees();
            for (Employee e : employees) {
                System.out.println("Employee Id: " + e.getEmployeeId() + ", Employee Username: " + e.getUsername() + ", Employee role: " + e.getUserRole().toString());
            }
            System.out.println();
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // use case 5
    private void createNewPartner() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** Create New Partner *****");
        System.out.println("Enter Username:");
        String usernameInput = sc.nextLine().trim();
        System.out.println("Enter Password:");
        String passwordInput = sc.nextLine().trim();

        try {
            partnerEntitySessionBeanRemote.createNewPartner(usernameInput, passwordInput);
            System.out.println("Partner Successfully Created!\n");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }

    }

    // use case 6
    private void viewAllPartners() {
        System.out.println("***** View All Partners *****");
        try {
            List<Partner> partners = partnerEntitySessionBeanRemote.viewAllPartners();
            for (Partner p : partners) {
                System.out.println("Partner Id: " + p.getPartnerId() + ", Partner Username: " + p.getUsername());
            }
            System.out.println();
        } catch (PartnerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
