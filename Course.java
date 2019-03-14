package scrame;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Course implements Serializable, Comparable<Course> {

    private String title;
    private String code;
    private int au;
    private String professor;
    private List<Class> classes;

    public Course(String title, String code, int au, String profName) {
        this.title = title;
        this.code = code;
        this.au = au;
        this.professor = profName;
        classes = new ArrayList<Class>();
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public int getAU() {
        return au;
    }

    public Professor getProf() {
        return Professor.getProfessorByName(professor);
    }

    public List<Class> getClassList() {
        return classes;
    }

    public boolean equals(Object O) {
        if (O instanceof Course) {
            Course C = (Course) O;
            return (getCode().equals(C.getCode()));
        }
        return false;
    }

    public Course getCourseByTitle(String title) {
        List list = getCourseList();
        for (int i = 0; i < list.size(); i++) {
            Course C = (Course) list.get(i);
            if (C.getTitle().equals(title))
                return C;
        }
        return null;
    }

    public static Course getCourseByCode(String code) {
        List list = getCourseList();
        for (int i = 0; i < list.size(); i++) {
            Course C = (Course) list.get(i);
            if (C.getCode().equals(code))
                return C;
        }
        return null;
    }

    public static List getCourseList() {
        return getCourseList("course.dat");
    }

    public static List getCourseList(String file) {
        List list = null;
        try {
            list = (ArrayList) SerializeDB.readSerializedObject(file);
        }
        catch ( Exception e ) {
        }
        if (list == null) list = new ArrayList();
        return list;
    }

    public static void menuCourse()
    {
        String MenuTitle = "Course";
        char choice = '\u0000';

        do {
            Menu.showMenu(MenuTitle);
            System.out.print("Enter choice: ");
            choice = CheckType.getChar();

            switch (choice) {
                case '1':
                    Course.printCourseList();
                    break;

                case '2':
                    System.out.println("Adding new course");
                    System.out.println("************************");

                    String code = "";
                    boolean codeError = true;

                    do {
                        System.out.print("Enter course code - ");
                        code = CheckType.getString();
                        if (code.equals("q") || code.equals("Q")) break;
                        if (Course.getCourseByCode(code) != null) {
                            System.out.println("ERROR : Another course with that code already exists. Please try again.");
                        }
                        else codeError = false;
                    } while (codeError);

                    if (!codeError) {
                        System.out.print("Enter course title - ");
                        String title = CheckType.getString();

                        System.out.print("Enter course AU - ");
                        int au = CheckType.getInt();

                        System.out.println("Select course professor:");
                        System.out.println("************************");
                        String profName = "";
                        List profList = (List) Professor.getProfessorList();
                        for (int i = 0; i < profList.size(); i++) {
                            Professor p = (Professor) profList.get(i);
                            System.out.println(i + 1 + ") " + p.getName() + " (" + p.getEmail() + ")");
                        }
                        System.out.println("************************");
                        System.out.print("Enter choice - ");

                        do {
                            try {
                                int profChoice = CheckType.getInt();
                                if (profChoice > 0 && profChoice <= profList.size()) {
                                    Professor P = (Professor) profList.get(profChoice-1);
                                    profName = P.getName();
                                    break;
                                }
                            }
                            catch (Exception e) {}
                            System.out.println("Invalid choice");
                        } while(true);

                        List list = Course.getCourseList();
                        Course c = new Course(title, code, au, profName);
                        if (list == null) list = new ArrayList();
                        list.add(c);
                        c.save(list);
                        System.out.println("Course " + c.getTitle() + " added!");
                    }
                    break;

                case '3':
                    System.out.print("Enter the course code: ");
                    code = CheckType.getString();
                    String storeCourseTitle = SearchCourse(code);
                    if(storeCourseTitle != null)
                        System.out.println(storeCourseTitle + " (" + code + ") is found");
                    else
                        System.out.println("There is no such course.");
                    break;

                case '0':
                    System.out.println("Exiting to previous menu >>>>");
                    break;
                case 'q':
                case 'Q':
                    Menu.terminateMenu();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 'q' && choice != 'Q' && choice != '0');
    }

    public static void printCourseList() {
        List list;
        String choice = "f";
        boolean skip = false;

        do {
            System.out.println();
            System.out.println("Courses");
            System.out.println("************************");

            list = getCourseList();
            Collections.sort(list);
            Course.save(list);
            if (list != null && list.size() > 0) {
                for (int i = 0 ; i < list.size() ; i++) {
                    Course c = (Course) list.get(i);
                    System.out.println(i+1 + ") " + c.code + " " + c.title);
                }
            }
            else {
                System.out.println("There exists no courses in the system.");
            }

            System.out.println();
            System.out.println("(0) Back to courses menu");
            System.out.println("(Q) Exit program");
            System.out.println("************************");

            System.out.print("Enter choice: ");
            choice = CheckType.getString();

            switch (choice) {
                case "0":
                    System.out.println("Exiting to previous menu >>>>");
                    break;

                case "q":
                case "Q":
                    Menu.terminateMenu();
                    break;

                default:
                    int choiceInt = 0;
                    try {
                        choiceInt = Integer.parseInt(choice);
                    }
                    catch (Exception e) {
                        System.out.println("Invalid choice");
                        break;
                    }
                    if (list == null || list.size() < choiceInt) {
                        System.out.println("That course does not exist.");
                    }
                    else {
                        Course C = (Course) list.get(choiceInt - 1);
                        showCourse(C);
                    }
            }
        } while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q") && !skip);
    }

    private static boolean showCourse(Course c) {
        String choice = "";
        boolean deleted = false;

        do {
            System.out.println();
            System.out.println("Course");
            System.out.println("************************************");
            System.out.println("Code:      " + c.getCode());
            System.out.println("Title:     " + c.getTitle());
            System.out.println("AU:        " + c.getAU());
            Professor p = c.getProf();
            System.out.println("Professor: " + p.getName() + " (" + p.getEmail() + ") (" + p.getContact() + ")");
            System.out.println();
            System.out.println("1) Edit course code");
            System.out.println("2) Edit course title");
            System.out.println("3) Edit course AU");
            System.out.println("4) Manage classes");
            System.out.println("5) Show registered students");
            System.out.println("6) Edit professor");
            System.out.println("7) Show course statistics");
            System.out.println("D) Delete course");
            System.out.println();
            System.out.println("0) Back to course list");
            System.out.println("Q) Exit program");
            System.out.println("************************************");
            System.out.print("Enter choice: ");

            choice = CheckType.getString();

            switch (choice) {
                case "0":
                    System.out.println("  Exiting to course list...");
                    break;

                case "1":
                    c.updateCode();
                    break;

                case "2":
                    c.updateTitle();
                    break;

                case "3":
                    c.updateAU();
                    break;

                case "4":
                    c.printClasses();
                    break;

                case "5":
                    StudentCourse.printRegisterList(c.code);
                    break;

                case "6":
                    c.updateProfessor();
                    break;

                case "7":
                    c.printStatistics();
                    break;

                case "d":
                case "D":
                    char confirm = 'n';
                    System.out.println();
                    System.out.println("  Are you sure you want to delete " +  c.getTitle() + "? This is irreversible.");
                    System.out.print("  Enter \"y\" to confirm: ");
                    confirm = CheckType.getChar();
                    if (confirm == 'y') {
                        // Update registered course list
                        StudentCourse.deleteCourse(c.getCode());
                        // Update component list
                        Component.deleteCourse(c.getCode());
                        String deletedTitle = c.getTitle();
                        String deletedCode = c.getCode();

                        List list = getCourseList();
                        deleted = list.remove(c);
                        c.save(list);
                        System.out.println("Deleted " + deletedTitle + " (" + deletedCode + ")");
                    }
                    break;

                case "q":
                case "Q":
                    Menu.terminateMenu();
                    break;

                default:
                    System.out.println("  Invalid choice.");
                    break;
            }
        }  while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q") && !deleted);

        return deleted;
    }

    private void printStatistics() {
        System.out.println("");
        System.out.println(code + " Statistics");
        System.out.println("************************");
        System.out.println("Students enrolled:       " + StudentCourse.countStudentsByCourse(code));
        System.out.println("Tutorial/Lab classes:    " + getClassList().size());
        List componentList = Component.getComponentListByCourse(code);
        System.out.println("Components:              " + componentList.size());
        System.out.println("************************");
    }

    // Print classes
    public void printClasses() {
        String choice = "";

        do {
            System.out.println("");
            System.out.println(getCode() + " Classes");
            System.out.println("************************");
            if (classes.size() > 0) {
                for (int i = 0; i < classes.size(); i++) {
                    Class C = classes.get(i);
                    System.out.println(i + 1 + ") " + C.getName() + " (S:" + C.getSize() + ", V:" + C.getVacancy() + ")");
                }
            }
            else {
                System.out.println("There exists no classes for this course.");
            }
            System.out.println("");
            System.out.println("(A) Add a class");
            System.out.println("");
            System.out.println("(0) Back to course " + getCode());
            System.out.println("(Q) Exit program");
            System.out.println("************************");
            System.out.print("Enter choice: ");

            choice = CheckType.getString();

            switch (choice) {
                case "0":
                    System.out.println("  Exiting to course " + getCode() + "...");
                    break;

                case "a":
                case "A":
                    addClass();
                    break;

                case "q":
                case "Q":
                    Menu.terminateMenu();
                    break;

                default:
                    int choiceInt = 0;
                    try {
                        choiceInt = Integer.parseInt(choice);
                    }
                    catch (Exception e) {
                        System.out.println("  Invalid choice.");
                        break;
                    }
                    if (classes.size() < choiceInt) {
                        System.out.println("That class does not exist.");
                    }
                    else {
                        Class cl = (Class) classes.get(choiceInt-1);
                        String classChoice;
                        boolean deleted = false;
                        do {
                            cl.menuClass();
                            classChoice = CheckType.getString();

                            switch (classChoice) {
                                case "0":
                                    System.out.println("  Exiting to " + getCode() + " class list >>>>");
                                    break;

                                // Edit class name
                                case "1":
                                    System.out.print("\nEnter new class name: ");
                                    String _name = CheckType.getString();

                                    if (cl.getName().equals(_name)) System.out.println("No change detected. Original class name preserved.");
                                    else if (classExists(_name)) System.out.println("ERROR : Another class with that name already exists. Please try again.");
                                    else {
                                        List list = getCourseList();
                                        int courseIndex = list.indexOf(this);
                                        if (courseIndex != -1) {
                                            cl.setName(_name);
                                            list.set(courseIndex, this);
                                            save(list);
                                            System.out.println("\n  Class name changed to: " + cl.getName());
                                        }
                                    }
                                    break;

                                // Edit class size
                                case "2":
                                    System.out.print("Enter new class size: ");
                                    int _size = CheckType.getInt();

                                    if (cl.getSize() == _size) System.out.println("No change detected. Original class size preserved.");
                                    else {
                                        List list = getCourseList();
                                        int courseIndex = list.indexOf(this);
                                        if (courseIndex != -1) {
                                            cl.setSize(_size);
                                            list.set(courseIndex, this);
                                            save(list);
                                            System.out.println("\n  Class size changed to: " + cl.getSize());
                                        }
                                    }
                                    break;

                                // View class list
                                case "3":
                                    List<Course> list = getCourseList();
                                    int courseIndex = list.indexOf(this);
                                    if (courseIndex != -1) {
                                        cl.printStudentList();
                                        list.set(courseIndex, this);
                                        save(list);
                                    }
                                    save(list);
                                    break;

                                case "d":
                                case "D":
                                    char confirm = 'n';
                                    System.out.println();
                                    System.out.println("  Are you sure you want to delete " +  cl.getName() + "? This is irreversible.");
                                    System.out.print("  Enter \"y\" to confirm: ");
                                    confirm = CheckType.getChar();
                                    if (confirm == 'y') {
                                        String deletedName = cl.getName();

                                        list = getCourseList();
                                        courseIndex = list.indexOf(this);
                                        if (courseIndex != -1) {
                                            deleted = classes.remove(cl);
                                            list.set(courseIndex, this);
                                            save(list);
                                            System.out.println("\n  Deleted " + deletedName);
                                        }
                                    }
                                    break;

                                case "q":
                                case "Q":
                                    Menu.terminateMenu();
                                    break;

                                default:
                                    System.out.println("  Invalid choice");
                            }
                        } while (!classChoice.equals("q") && !classChoice.equals("Q") && !classChoice.equals("0") && !deleted);
                    }
                    break;
            }
        } while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q"));
    }

    // Public setters
    // Update course title

    public void updateTitle() {
        System.out.print("Enter new title: ");
        String _title = CheckType.getString();

        if (_title.length() > 0) {
            if (_title.equals(getTitle())) {
                System.out.println("No change detected. Original title preserved.");
            }
            else {
                List list = getCourseList();
                int courseIndex = list.indexOf(this);
                if (courseIndex != -1) {
                    setTitle(_title);
                    list.set(courseIndex, this);
                    save(list);
                    System.out.println("Changed title of course to: " + getTitle());
                }
            }
        }
        else System.out.println("Invalid title");
    }
    // Update course code
    public void updateCode() {
        System.out.print("Enter new course code: ");
        String code = CheckType.getString();

        if (code.length() > 0) {
            if (code.equals(code)) {
                System.out.println("No change detected. Original course code preserved.");
            }
            else if (getCourseByCode(code) == null) {
                List list = getCourseList();
                int courseIndex = list.indexOf(this);
                if (courseIndex != -1) {
                    // Update registered course list
                    StudentCourse.updateCode(code, code);
                    this.code = code;
                    list.set(courseIndex, this);
                    save(list);
                    System.out.println("Changed code of course to: " + code);
                }
            }
            else System.out.println("ERROR : Another course with that code already exists.");
        }
        else System.out.println("Invalid course code");
    }
    // Update course AU
    public void updateAU() {
        System.out.print("Enter new course AU: ");
        int au = CheckType.getInt();

        if (this.au == au) {
            System.out.println("No change detected. Original course au preserved.");
        }
        else if (au > 0) {
            List list = getCourseList();
            int courseIndex = list.indexOf(this);
            if (courseIndex != -1) {
                this.au = au;
                list.set(courseIndex, this);
                save(list);
                System.out.println("Changed AU of course to: " + au);
            }
        }
        else System.out.println("Invalid course AU.");
    }

    // Get class
    public boolean classExists(String _name) {
        for (int i = 0; i < classes.size(); i++) {
            Class cl = classes.get(i);
            if (cl.getName().equals(_name)) {
                return true;
            }
        }
        return false;
    }

    // Add class
    public void addClass() {
        String name = "";
        int size = 0;

        System.out.println("");
        System.out.println(getCode() + ": Adding class");
        System.out.println("************************");

        boolean classError;
        do {
            classError = false;
            System.out.print("Enter class name: ");
            name = CheckType.getString();

            if (name.length() == 0) {
                classError = true;
                System.out.println("ERROR : Class name is required. Please try again.\n");
            }
            else {
                if (classExists(name)) {
                    classError = true;
                    System.out.println("ERROR : Another class with that name already exists. Please try again.\n");
                }
            }
        } while (classError);

        if (!classError) {
            System.out.print("Enter class size: ");
            size = CheckType.getInt();

            List list = getCourseList();
            int courseIndex = list.indexOf(this);
            if (courseIndex != -1) {
                Class cl = newClass(name, getCode(), size);
                if (cl != null) {
                    System.out.println("Class " + cl.getName() + " added.");
                    list.set(courseIndex, this);
                    save(list);
                }
            }
            else System.out.println("ERROR : Could not add class to course.");
        }
    }

    public Class newClass(String name, String code, int size) {
        try {
            Class cl = new Class(name, code, size);
            classes.add(cl);
            return cl;
        }
        catch (Exception e) {
            return null;
        }
    }

    // Remove class
    public void removeClass(Class class1) {
        classes.remove(class1);
    }

    // Set course title
    public void setTitle(String title)
    {
        this.title = title;
    }

    // Set course code
    public void setCode(String code)
    {
        this.code = code;
    }

    public void setAU(int au)
    {
        this.au = au;
    }

    public static void save(List list) {
        SerializeDB.writeSerializedObject("course.dat", list);
    }

    public int compareTo(Course cs) {
        int lastCmp = getCode().compareTo(cs.getCode());
        return (lastCmp != 0 ? lastCmp : getCode().compareTo(cs.getCode()));
    }

    public static String SearchCourse(String code)
    {
        List list=getCourseList();
        String storeCourseTitle = null;
        Boolean found=false;
        if (list != null && list.size() > 0) {
            for (int i = 0 ; i < list.size() ; i++) {
                Course c = (Course)list.get(i);
                if (c.getCode().compareTo(code)==0) {
                    storeCourseTitle=c.getTitle();
                    found = true;
                    break;
                }
            }
        }
        return storeCourseTitle;
    }

    public void updateProfessor() {
        do {
            System.out.println("Select new professor -");
            System.out.println("************************");
            Professor.displayProfessor();
            System.out.println("************************");
            System.out.print("Enter choice - ");

            try {
                List profList = (List) Professor.getProfessorList();
                int newProf = CheckType.getInt();
                if (newProf > 0 && newProf <= profList.size()) {
                    Professor p = (Professor) profList.get(newProf-1);
                    professor = p.getName();
                    System.out.println("Changed professor to " + p.getName());
                    break;
                }
            }
            catch (Exception e) {}
            System.out.println("Invalid choice.");
        } while(true);
    }
}