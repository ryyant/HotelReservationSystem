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

        while (true) {
            System.out.println("*** Guest Relation Officer View ***");
            System.out.println("-----------------------");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("-----------------------");
            System.out.println("3: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("-----------------------");
            System.out.println("7: Logout\n");
            int response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                } else if (response == 2) {
                } else if (response == 3) {
                } else if (response == 4) {
                } else if (response == 5) {
                } else if (response == 6) {
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }
    }
}
