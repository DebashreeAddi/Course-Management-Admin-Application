package scrame;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class Student extends Person implements Comparable<Student>{
    private int studentYear;
    private String studentMatric;
    private char studentGender;
    private String studentAddress;

    public Student() {
        super("", "", 0);
        studentYear = 1;
        studentMatric = "";
        studentGender = 'M';
        studentAddress = "";
    }

    public Student(String name, String email, int contact, int studentYear, String studentMatric, char studentGender, String studentAddress){

        super(name, email, contact);
        this.studentYear = studentYear;
        this.studentMatric = studentMatric;
        this.studentGender = studentGender;
        this.studentAddress = studentAddress;
    }

    // Getter methods

    public int getStudentYear() {
        return studentYear;
    }

    public String getStudentMatric() {
        return studentMatric;
    }

    public char getStudentGender() {
        return studentGender;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public boolean equals(Object O) {
        if(O instanceof Student) {
            Student S = (Student) O;
            return (this.getStudentMatric().equals(S.getStudentMatric()));
        }
        return false;
    }

    public static Student getStudentByMatric(String studentMatric) {
        return getStudentByMatric(studentMatric, "student.dat");
    }

    public static Student getStudentByMatric (String studentMatric, String file) {
        List list = getStudentList(file);
        if (list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Student S = (Student)list.get(i);
                if (S.getStudentMatric().equals(studentMatric))
                    return S;
            }
        }
        return null;
    }

    public static List getStudentList() {
        return getStudentList("student.dat");
    }

    private static List getStudentList(String file) {
        List list = null;
        try {
            list = (ArrayList) SerializeDB.readSerializedObject(file);
        }
        catch (Exception e){
        }
        if (list == null)
            list = new ArrayList();
        return list;
    }

    // Validation Methods

    private static int validateYear() {
        int studentYear = 0;

        do {
            System.out.println ("Enter the student's year of study");
            try {
                studentYear = CheckType.getInt();
                return studentYear;
            }
            catch (NumberFormatException e){
                System.out.println ("ERROR : Invalid year of study. Ensure that the year contains only numbers");
            }
        } while(true);
    }

    // Setter Methods

    public void setStudentYear(int studentYear) {
        this.studentYear = studentYear;
    }

    public void setStudentGender(char studentGender) {
        this.studentGender = studentGender;
    }

    public void setStudentMatric (String studentMatric) {
        this.studentMatric = studentMatric;
    }

    public void setStudentAddress (String studentAddress) {
        this.studentAddress = studentAddress;
    }


    // Updater Methods

    public void updateStudentName() {
        System.out.println ("Enter new name - ");
        String name = CheckType.getString();

        if (name.length() > 0) {
            if (name.equals(getName())) {
                System.out.println ("Entered name is the one in the record. Original name preserved");
            }
            else {
                List list = getStudentList();
                int studentIndex = list.indexOf(this);
                if (studentIndex != -1) {
                    setName(name);
                    list.set(studentIndex, this);
                    save(list);
                    System.out.println ("Name has been updated. New name is " + getName());
                }
            }
        }
    }

    public void updateStudentMatric() {
        System.out.println ("Enter new matriculation number");
        String matric = CheckType.getString().toUpperCase();

        if (matric.length() > 0){
            if(matric.equals(studentMatric)) {
                System.out.println ("Entered matriculation number is the one in the record. Original name preserved");
            }
            else if (getStudentByMatric(matric) == null) {
                List list = getStudentList();
                int studentIndex = list.indexOf(this);
                if (studentIndex != -1) {
                    StudentCourse.updateStudentMatric(studentMatric, matric);
                    this.studentMatric = matric;
                    list.set(studentIndex, this);
                    save(list);
                    System.out.println ("Matriculation number has been updated. New number is " + matric);
                }
            }
            else
                System.out.println ("ERROR : There exists another student with the same matriculation number.");
        }
        else
            System.out.println ("Invalid matriculation number");
    }

    public void updateStudentYear() {
        int year = validateYear();

        if(year == this.studentYear) {
            System.out.println ("Entered year is the one in the record. Original year preserved");
        }
        else if (year > 0) {
            List list = getStudentList();
            int studentIndex = list.indexOf(this);
            if (studentIndex != -1) {
                this.studentYear = year;
                list.set(list.indexOf(this), this);
                save(list);
                System.out.println("Year has been updated. New year is " + year);
            }
        }
        else
            System.out.println ("Invalid Year of Study");
    }

    // Enroll in course
    public void enroll(String course) {
        StudentCourse.register(studentMatric, course);
    }

    public static void menuStudents() {
        char choice = '1';

        do {
            String MenuTitle="Student";
            Menu.showMenu(MenuTitle);
            System.out.print("Enter choice - ");
            choice = CheckType.getChar();

            switch (choice) {
                case '1':
                    Student.printStudentList();
                    break;
                case '2':
                    System.out.println("Adding new student");
                    System.out.println("************************");

                    // Process name
                    String name = validateName("student");

                    // Process email
                    String email = validateEmail("student");

                    // Process contact
                    int contact = validateContact("student");

                    // Process gender
                    char gender = validateGender("student");

                    // Process address
                    System.out.print("Enter student's address: ");
                    String address = CheckType.getString();
                    if (address.length() == 0) address = "NONE";

                    // Process matric
                    String matric = "";
                    boolean matricError = true;

                    do {
                        System.out.print("Enter student's matriculation no.: ");
                        matric = CheckType.getString().toUpperCase();
                        if (matric.equals("q") || matric.equals("Q")) break;
                        if (matric.length() == 0) System.out.println("ERROR : Matric no. is required. 'Q' to exit adding student.\n");
                        else if (Student.getStudentByMatric(matric) != null) {
                            System.out.println("ERROR : Another student with that matric no. already exists. Please try again.");
                        }
                        else matricError = false;
                    } while (matricError);

                    if (!matricError) {
                        int year = validateYear();

                        List list = Student.getStudentList();
                        Student s = new Student(name, email, contact, year, matric, gender, address);
                        if (list == null) list = new ArrayList();
                        list.add(s);
                        s.save(list);
                        System.out.println("\n  Student " + s.getName() + " added!");
                    }

                    break;

                case '3':
                    System.out.print("Enter the student's name: ");
                    name = CheckType.getString();
                    SearchStudent(name);
                    break;

                case '0':
                    System.out.println("  Exiting to previous menu...");
                    break;
                case 'q':
                case 'Q':
                    Menu.terminateMenu();
                    break;
                default:
                    System.out.println("  Invalid choice.");
            }
        } while (choice != '0' && choice != 'q' && choice != 'Q');
    }

    public static void printStudentList() {
        List list;
        String choice = "f";
        boolean skip = false;

        do {
            System.out.println();
            System.out.println("Students list");
            System.out.println("-----------------------");

            list = getStudentList();
            Collections.sort(list);
            Student.save(list);
            if (list != null && list.size() > 0) {
                for (int i = 0 ; i < list.size() ; i++) {
                    Student s = (Student)list.get(i);
                    System.out.println(i+1 + ") " + s.getName() + " (" + s.getStudentMatric() + ")");
                }
            }
            else {
                System.out.println("There are no students in the system.");
            }

            System.out.println();
            System.out.println("(0) Back to student menu");
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
                        System.out.println("That student does not exist");
                    }
                    else {
                        Student s = (Student) list.get(choiceInt-1);
                        showStudent(s);
                    }
            }
        } while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q") && !skip);
    }

    private static boolean showStudent(Student s) {
        String choice = "";
        boolean deleted = false;

        do {
            System.out.println();
            System.out.println("Student profile");
            System.out.println("************************");
            System.out.println("Name: " + s.getName());
            System.out.println("Matric: " + s.getStudentMatric());
            System.out.println("Year: " + s.getStudentYear());
            System.out.println("Gender: " + s.getStudentGender());
            System.out.println("Email: " + s.getEmail());
            System.out.println("Contact: " + s.getContact());
            System.out.println("Address: " + s.getStudentAddress());
            System.out.println();
            System.out.println("(1) Edit student");
            System.out.println("(2) Register for a course");
            System.out.println("(3) Show registered courses");
            System.out.println("(4) Print transcript");
            System.out.println("(D) Delete student");
            System.out.println();
            System.out.println("(0) Back to student list");
            System.out.println("(Q) Exit program");
            System.out.println("************************");
            System.out.print("Enter choice: ");

            choice = CheckType.getString();

            switch (choice) {
                case "0":
                    System.out.println("  Exiting to student list...");
                    break;

                case "1":
                    Student.editStudent(s);
                    break;

                case "2":

                    List<Course> courseList = Course.getCourseList();
                    Collections.sort(courseList);
                    if (courseList != null && courseList.size() > 0) {
                        System.out.println("Select a course from the list");
                        System.out.println("**********************************");

                        Course c;

                        for (int i = 0; i < courseList.size(); i++) {
                            c = (Course) courseList.get(i);
                            System.out.println(i + 1 + ") " + c.getCode() + " " + c.getTitle());
                        }

                        System.out.println("**********************************");
                        System.out.print("Enter choice - ");

                        int courseChoice = CheckType.getInt() - 1;
                        if ( courseChoice >= 0 && courseChoice < courseList.size() ) {
                            c = (Course) courseList.get(courseChoice);
                            List<Class> classList = c.getClassList();

                            if (classList.size() > 0) {
                                StudentCourse.register(s.getStudentMatric(), c.getCode());

                                if (StudentCourse.isEnrolled(s.getStudentMatric(), c.getCode())) {
                                    boolean studentFound = false;

                                    for (int i = 0; i < classList.size(); i++) {
                                        Class cl = (Class) classList.get(i);
                                        if (cl.inClass(s)) studentFound = true;
                                    }

                                    if (studentFound)
                                        System.out.println("Student is already enrolled in a class.");

                                    else {
                                        System.out.println("Select class");
                                        System.out.println("************************");
                                        for (int i = 0; i < classList.size(); i++) {
                                            Class cl = (Class) classList.get(i);
                                            System.out.println(i + 1 + ") " + cl.getName() + " (Size: " + cl.getSize() + ", Vacancy: " + cl.getVacancy() + ")");
                                        }
                                        System.out.println("************************");
                                        System.out.print("Enter choice - ");
                                        int classChoice = CheckType.getInt();

                                        switch (classChoice) {
                                            default:
                                                if (classChoice > 0 && classChoice <= classList.size()) {
                                                    try {
                                                        Class cl = classList.get(classChoice-1);
                                                        if (cl.getVacancy() > 0) {
                                                            cl.addStudent(s);
                                                            Course.save(courseList);
                                                        }
                                                        else System.out.println("Could not add student: class " + cl.getName() + " is full.");
                                                    }
                                                    catch (Exception e) {
                                                        System.out.println("Invalid choice");
                                                    }
                                                }
                                                else System.out.println("Invalid choice");
                                                break;
                                        }
                                    }
                                }
                            }
                            else
                                System.out.println("There are no classes available for this course!");
                        }
                    }

                    else {
                        System.out.println("No courses are available in the system.");
                    }

                    break;

                // Show registered courses
                case "3":
                    System.out.println("Registered courses:");
                    System.out.println("************************");

                    courseList = StudentCourse.getStudentCourses(s.getStudentMatric());

                    if (courseList.size() > 0) {
                        for (int i = 0; i < courseList.size(); i++) {
                            Course c = (Course) courseList.get(i);
                            System.out.print(i + 1 + ") " + c.getCode() + " " + c.getTitle());

                            // Print student's class or 'No class'
                            boolean found = false;
                            List<Class> classList = c.getClassList();
                            for (int o = 0; o < classList.size(); o++) {
                                Class cl = classList.get(o);
                                if (cl.inClass(s)) {
                                    System.out.print(" (" + cl.getName() + ")");
                                    found = true;
                                }
                            }
                            if (!found) System.out.print(" (No class)");
                            System.out.println();
                        }
                        System.out.println("************************");
                    }
                    else {
                        System.out.println("The student is not registered for any courses.");
                    }
                    break;

                // Print transcript
                case "4":
                    s.printTranscript();
                    break;

                case "d":
                case "D":
                    char confirm = 'n';
                    System.out.println();
                    System.out.println("  Are you sure you want to delete " +  s.getName() + "? This is irreversible.");
                    System.out.print("  Enter \"y\" to confirm: ");
                    confirm = CheckType.getChar();
                    if (confirm == 'y') {
                        // Update registered course list
                        StudentCourse.deleteStudent(s.getStudentMatric());
                        // Update component list
                        Component.deleteStudent(s);

                        String deletedName = s.getName();
                        String deletedMatric = s.getStudentMatric();

                        List list = getStudentList();
                        deleted = list.remove(s);
                        s.save(list);
                        System.out.println("\n  Deleted " + deletedName + " (" + deletedMatric + ")");
                    }
                    break;
                case "q":
                case "Q":
                    Menu.terminateMenu();
                    break;
            }
        }  while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q") && !deleted);

        return deleted;
    }

    private void printTranscript() {
        System.out.println("");
        System.out.println("Academic Transcript");
        System.out.println("**********************************");
        System.out.println("Name:              " + getName());
        System.out.println("Matriculation no.: " + studentMatric);
        System.out.println("Year of study:     " + studentYear);
        System.out.println("");
        List courseList = StudentCourse.getStudentCourses(studentMatric);
        // for each course, print score
        int max = 0;
        double totalAU = 0.0, totalGP = 0.0;
        double GPA = 0.0;
        for (int i = 0; i < courseList.size(); i++) {
            Course c = (Course) courseList.get(i);
            if ((c.getCode() + " " + c.getTitle()).length() + 3 > max) max = (c.getCode() + " " + c.getTitle()).length() + 3;
            totalAU += c.getAU();
        }
        for (int i = 0; i < courseList.size(); i++) {
            double score = 0.0;
            Course c = (Course) courseList.get(i);
            // for each component, add marks / weightage to score
            List componentList = Component.getComponentListByCourse(c.getCode());
            for (int o = 0; o < componentList.size(); o++) {
                Component cc = (Component) componentList.get(o);
                score += (double) (cc.getMarks(this) * cc.getWeightage() / 100);
                if (score >= 80) totalGP += 4 * c.getAU();
                else if (score >= 70) totalGP += 3 * c.getAU();
                else if (score >= 60) totalGP += 2 * c.getAU();
                else if (score >= 50) totalGP += c.getAU();
            }
            System.out.println(TextFormat.padRight(c.getCode() + " " + c.getTitle().toUpperCase() + ": ", max) + TextFormat.padRight(TextFormat.scoreToGrade(score), 7) + c.getAU() + " AUs");
        }
        GPA = (double) (totalGP / totalAU);
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(4);
        df.setRoundingMode(RoundingMode.DOWN);
        System.out.println("Total GP: " + totalGP);
        System.out.println("Total AU: " + totalAU);
        System.out.println("GPA: " + df.format(GPA));
        System.out.println("*****************************");
    }

    private static void editStudent(Student s) {
        String choice = "";

        do {
            System.out.println();
            System.out.println("Edit Student");
            System.out.println("************************");
            System.out.println("1) Name: " + s.getName());
            System.out.println("2) Matric: " + s.getStudentMatric());
            System.out.println("3) Year: " + s.getStudentYear());
            System.out.println("4) Gender: " + s.getStudentGender());
            System.out.println("5) Email: " + s.getEmail());
            System.out.println("6) Contact: " + s.getContact());
            System.out.println("7) Address: " + s.getStudentAddress());
            System.out.println();
            System.out.println("0) Back to student list");
            System.out.println("Q) Exit program");
            System.out.println("************************");
            System.out.print("Enter choice: ");

            choice = CheckType.getString();

            switch (choice) {
                case "0":
                    System.out.println("Exiting to student list >>>>");
                    break;

                // Edit name
                case "1":
                    s.updateStudentMatric();
                    break;

                // Edit matric no.
                case "2":
                    s.updateStudentMatric();
                    break;

                // Edit year
                case "3":
                    s.updateStudentYear();
                    break;

                // Edit gender
                case "4":
                    char _gender = validateGender("student");
                    if (s.getStudentGender() == _gender) System.out.println("No change detected. Original gender preserved.");
                    else {
                        s.setStudentGender(_gender);
                        System.out.println("Changed student's gender to: " + s.getStudentGender());
                    }
                    break;

                // Edit email
                case "5":
                    String _email = validateEmail("student");
                    if (s.getEmail().equals(_email)) System.out.println("No change detected. Original email preserved.");
                    else {
                        s.setEmail(_email);
                        System.out.println("Changed student's email to: " + s.getEmail());
                    }
                    break;

                // Edit contact
                case "6":
                    int _contact = validateContact("student");
                    if (s.getContact() == _contact) System.out.println("No change detected. Original contact preserved.");
                    else {
                        s.setContact(_contact);
                        System.out.println("Changed student's contact to: " + s.getContact());
                    }
                    break;

                // Edit address
                case "7":
                    System.out.print("Enter student's address: ");
                    String address = CheckType.getString();
                    if (address.length() == 0) address = "NONE";
                    if (s.getStudentAddress().equals(address)) System.out.println("No change detected. Original address preserved.");
                    else {
                        s.setStudentAddress(address);
                        System.out.println("Changed student's address to: " + s.getStudentAddress());
                    }
                    break;

                case "q":
                case "Q":
                    Menu.terminateMenu();
                    break;
            }
        } while (!choice.equals("0") && !choice.equals("q") && !choice.equals("Q"));
    }

    public static void save(List list) {
        SerializeDB.writeSerializedObject("student.dat", list);
    }
    public int compareTo(Student s) {
        int lastCmp = super.getName().compareTo(s.getName());
        return (lastCmp != 0 ? lastCmp : super.getName().compareTo(s.getName()));
    }

    public static void SearchStudent(String searchStr)
    {
        List list=getStudentList();
        int count = 0;
        if (list != null && list.size() > 0) {
            System.out.println();
            System.out.println("Search results:");
            System.out.println("---------------------");
            for (int i = 0 ; i < list.size() ; i++) {
                Student s = (Student)list.get(i);
                if (s.getName().toLowerCase().contains(searchStr.toLowerCase()) || s.getStudentMatric().toUpperCase().contains(searchStr.toUpperCase())) {
                    System.out.println(count + 1 + ") " + s.getName() + " (" + s.getStudentMatric() + ")");
                    count++;
                }
            }
        }
        if (count > 0) System.out.println("\nTotal " + count + " results found.");
        else System.out.println("That search query return 0 results.");
    }
}


