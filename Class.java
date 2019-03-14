package scrame;

import java.awt.*;
import java.beans.IntrospectionException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Class implements Serializable {
    private String professorName;
    private String name;
    private String course;
    private int size;
    private ArrayList<Student> students;

    public Class (String name, String course, int size) {
        professorName = "";
        this.name = name;
        this.course = course;
        this.size = size;
        students = new ArrayList<Student>();
    }

    // Getter Methods

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public int getSize() {
        return size;
    }

    public int getVacancy() {
        int vacancy = size - students.size();
        return vacancy;
    }

    // Setter Methods

    public void setName (String name) {
        this.name = name;
    }

    public void setSize (int size) {
        this.size = size;
    }

    public void menuClass () {
        System.out.println("");
        System.out.println ("Edit Class Menu");
        System.out.println ("************************");
        System.out.println ("(1) Name - " + this.name);
        System.out.println ("(2) Size - " + this.size + "[Vacancy left - " + getVacancy() + "]");
        System.out.println ("(3) View student roster");
        System.out.println ("(D) Delete class");
        System.out.println ("");
        System.out.println ("(0) Back to " + this.course + "class list");
        System.out.println ("(Q) Quit Program");
        System.out.println ("************************");
        System.out.println ("Enter your choice");
    }

    public boolean equals (Object O){
        if (O instanceof Class) {
            Class C = (Class)O;
            return (getName().equals(C.getName()));
        }
        return false;
    }

    // Check for existance of student in class
    public boolean inClass (Student S) {
        // Check if student exists in <student> array
        if (students != null && students.size() > 0){
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                if (S.equals(student)) return true;
            }
        }
        return false;
    }

    public void addStudent (Student S) {
        if (inClass(S))
            System.out.println (S.getName() + "(" + S.getStudentMatric() + ") is already preesence in this class roster");
        else {
            students.add(S);
            System.out.println(S.getName() + "(" + S.getStudentMatric() + ") has been successfully enrolled in the class");
        }
    }

    public boolean removeStudent (Student S) {
        if (students.remove(S))
            return true;
        return false;
    }

    public void printStudentList() {
        String choice = "";
        do {
            if (students.size() > 0){
                System.out.println ( name + " Student Roster");
                System.out.println ("************************");
                for (int i =0 ; i < students.size(); i++) {
                    Student S = students.get(i);
                    System.out.println ((i+1) + ")" + S.getName() + "(" + S.getStudentMatric() + ")");
                }
                System.out.println();
                System.out.println ("(0) Back to " + name + "menu");
                System.out.println ("(Q) Quit Program");
                System.out.println ("************************");
                System.out.println ("Enter choice - ");

                choice = CheckType.getString();

                switch (choice) {
                    case "0" :
                        System.out.println ("Exiting to " + name + "menu >>>>");
                        break;

                    case "q" :
                    case "Q" :
                        Menu.terminateMenu();
                        break;

                    default :
                        int studentChoice;
                        String choice2;

                        try {
                            studentChoice = Integer.parseInt(choice);
                            if (studentChoice > 0 && studentChoice <= students.size()){
                                Student S = students.get(studentChoice - 1);

                                do {
                                    System.out.println (name + " > " + S.getName());
                                    System.out.println ("************************");
                                    System.out.println ("(1) Remove student from class");
                                    System.out.println ();
                                    System.out.println ("(0) Back to " + name + " student list");
                                    System.out.println ("(Q) Quit Program");
                                    System.out.println ("************************");
                                    System.out.println ("Enter choice - ");

                                    choice2 = CheckType.getString();

                                    switch (choice2) {
                                        case "0" :
                                            System.out.println ("Exiting to " + name + "student roster >>>>");
                                            break;

                                        case "1":
                                            if (removeStudent(S)) {
                                                System.out.println ("Remove student from class.");
                                            }
                                            else {
                                                System.out.println ("Could not remove student from class");
                                            }
                                            break;

                                        case "q":
                                        case "Q":
                                            Menu.terminateMenu();
                                            break;

                                        default:
                                            System.out.println ("Invalid choice");
                                    }
                                } while(!choice2.equals("0") && !choice2.equals("q") && !choice2.equals("Q") && !choice2.equals("1"));
                            }

                            else {
                                System.out.println("Invalid choice");
                            }
                        }

                        catch (Exception e) {
                            System.out.println ("Invalid choice");
                        }
                        break;
                }
            }

            else{
                System.out.println("There are no students enrolled in this class");
            }
        } while (students.size() > 0 && !choice.equals("0") && !choice.equals("q") && !choice.equals("Q"));
    }

    public static List getClassList() {
        return getClassList("class.dat");
    }

    public static List getClassList (String file){
        List list = null;
        try {
            list = (ArrayList) SerializeDB.readSerializedObject(file);
        }
        catch (Exception e) {}
            if (list == null) list = new ArrayList();
            return list;
    }

    public static Class getClassByName (String name) {
        List list = getClassList();
        for (int i = 0; i < list.size(); i++) {
            Class C = (Class) list.get(i);
            if (C.getName().equals(name)) {
                return C;
            }
        }
        return null;
    }

    public void save (List list) {
        SerializeDB.writeSerializedObject("class.dat", list);
    }
}
