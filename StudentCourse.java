package scrame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentCourse implements Serializable {
    private String student;
    private String course;

    public StudentCourse() {
        student = "";
        course = "";
    }

    public StudentCourse(String student, String course) {
        this.student = student;
        this.course = course;
    }

    public static void updateStudentMatric(String studentMatric, String matric) {
    }

    public String getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    // Get student registered courses

    public static List<Course> getStudentCourses(String matric) {
        List<StudentCourse> list = getRegisterList();
        List<Course> courseList = new ArrayList<Course>();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.student.equals(matric))
                courseList.add(Course.getCourseByCode(r.course));
        }
        return courseList;
    }

    // Get course registered students
    public static List<Student> getCourseStudents(String course) {
        List<StudentCourse> list = getRegisterList();
        List<Student> studentList = new ArrayList<Student>();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.course.equals(course))
                studentList.add(Student.getStudentByMatric(course));
        }
        return studentList;
    }

    // Check if student is registered
    public static boolean isEnrolled(String matric, String course) {
        if (matric.length() > 0) {
            List list = getRegisterList();
            for (int i = 0; i < list.size(); i++) {
                StudentCourse r = (StudentCourse) list.get(i);
                if (r.student.equals(matric) && r.course.equals(course))
                    return true;
            }
        }
        return false;
    }

    // Register student for a course
    public static boolean register(String student, String course) {
        if (student.length() > 0) {
            List list = getRegisterList();

            if (!isEnrolled(student, course)) {
                try {
                    StudentCourse r = new StudentCourse(student, course);
                    list.add(r);
                    save(list);
                    System.out.println("Student successfully enrolled in course.");
                    return true;
                }
                catch (Exception e) {
                    System.out.println("ERROR : Could not add student to course.");
                }
            }
            else {
                System.out.println("ERROR : Student is already registered for this course.");
            }
        }
        else {
            System.out.println("ERROR : Student does not exist.");
        }
        return false;
    }

    // Update matric numbers
    public static void updateMatric(String oldMatric, String newMatric) {
        List list = getRegisterList();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.student.equals(oldMatric)) {
                int rIndex = list.indexOf(r);
                if (rIndex != -1) {
                    r.student = newMatric;
                    list.set(rIndex, r);
                    save(list);
                }
            }
        }
    }

    // Update course codes
    public static void updateCode(String oldCode, String newCode) {
        List list = getRegisterList();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.course.equals(oldCode)) {
                int rIndex = list.indexOf(r);
                if (rIndex != -1) {
                    r.course = newCode;
                    list.set(rIndex, r);
                    save(list);
                }
            }
        }
    }

    // Delete student
    public static void deleteStudent(String matric) {
        List list = getRegisterList();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.student.equals(matric)) {
                list.remove(r);
                save(list);
            }
        }
    }

    // Delete course
    public static void deleteCourse(String code) {
        List list = getRegisterList();
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.course.equals(code)) {
                list.remove(r);
                save(list);
            }
        }
    }

    public static void printRegisterList(String course) {
        List list = getRegisterList();
        if (list != null && list.size() > 0) {
            System.out.println("Students enrolled");
            System.out.println("************************");
            for (int i = 0; i < list.size(); i++) {
                StudentCourse r = (StudentCourse) list.get(i);
                if (r.course.equals(course)) {
                    Student s = Student.getStudentByMatric(r.student);
                    System.out.println(s.getName() + " (" + s.getStudentMatric() + ")");
                }
            }
            System.out.println("************************");
        }
        else
            System.out.println("There are no students registered for this course.");
    }

    public static int countStudentsByCourse(String course) {
        List list = getRegisterList();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            StudentCourse r = (StudentCourse) list.get(i);
            if (r.course.equals(course))
                count++;
        }
        return count;
    }

    public static List getRegisterList() {
        return getRegisterList("studentcourse.dat");
    }

    public static List getRegisterList(String file) {
        List list = null;
        try {
            list = (ArrayList) SerializeDB.readSerializedObject(file);
        }
        catch ( Exception e ) {
        }
        if (list == null)
            list = new ArrayList();
        return list;
    }

    private static void save(List list) {
        SerializeDB.writeSerializedObject("studentcourse.dat", list);
    }
}
