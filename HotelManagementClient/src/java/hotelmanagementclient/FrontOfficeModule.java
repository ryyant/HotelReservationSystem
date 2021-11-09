/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementclient;

import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author ryyant
 */
public class FrontOfficeModule {

    private Employee currentEmployeeEntity;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(Employee currentEmployeeEntity) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;

    }

    public void menuFrontOffice() throws EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, " + currentEmployeeEntity.getUsername() + "!\n");

        OUTER:
        while (true) {
            System.out.println("*** Guest Relation Officer View ***");
            System.out.println("-----------------------");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("-----------------------");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("-----------------------");
            System.out.println("5: Logout\n");
            int response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
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
}
