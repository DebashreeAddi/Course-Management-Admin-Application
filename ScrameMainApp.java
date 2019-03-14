package scrame;

import java.awt.*;
import java.util.*;

public class ScrameMainApp {
    public static void main (String[] args) {
        boolean success = false;
        boolean loaded = false;
        char choice = '1';
        Database db = new Database();

        do {
            Menu.showMenu();
            choice = CheckType.getChar();

            switch (choice) {
                case '1' :
                    Professor.menuProfessors();
                    break;

                case '2' :
                    Student.menuStudents();
                    break;

                case '3' :
                    Course.menuCourse();
                    break;

                case '4' :
                    Component.menuComponent();
                    break;

                case '5' :
                    if (loaded == false) {
                        db.rawAll();
                        System.out.println("Loading Database cases");
                    }
                    else
                        System.out.println ("Database has been loaded");

                case 'q' :
                case 'Q' :
                    Menu.terminateMenu();
                    break;

                default :
                    System.out.println ("Invalid choice");
            }
        } while (choice != 'q' && choice != 'Q');
    }
}
