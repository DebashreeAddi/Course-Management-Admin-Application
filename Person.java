package scrame;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private String email;
    private int contact;

    public Person (String name, String email, int contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    // Getter methods

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getContact() {
        return contact;
    }

    // Setter methods

    public void setName (String name) {
        this.name = name;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setContact (int contact) {
        this.contact = contact;
    }

    // Validation methods

    public static String validateName (String person) {
        String name = "";

         do {
            System.out.println("Enter" + person + " 's name - ");
            name = CheckType.getString();
            if (name.length() == 0){
                System.out.println("ERROR : Invalid name. Ensure that the name contains atleast one character");
            }
            else if(name.matches(".*\\d+.*")){
                System.out.println("ERROR : Invalid name. Ensure that the name does not contain any numbers");
            }
            else
                return name;
        } while(true);
    }

    public static int validateContact (String person) {
        int contact;
         do {
             try {
                 System.out.println ("Enter" + person + " 's contact - ");
                 contact = CheckType.getInt();
                 return contact;
             }
             catch (Exception e) {
                 System.out.println("ERROR : Invalid contact number. Ensure that the contact number contains only digits");
             }
        } while(true);
    }

    public static char validateGender (String person) {
        char gender;

        do {
            System.out.println ("Enter" + person + " 's gender (M/F) - ");
            gender = CheckType.getChar();
            if (gender == 'M' || gender == 'm')
                return 'M';
            else if (gender =='F' || gender == 'f')
                return 'F';
            else
                System.out.println ("ERROR : Invalid gender. Ensure that the gender is 'M' or 'F' ");
        } while(true);
    }

    public static String validateEmail (String person) {
        String email;
        do {
            System.out.println ("Enter" + person + " 's gender (M/F) - ");
            email = CheckType.getString();
            if (email.length() > 0)
                return email;
            else
                System.out.println ("ERROR : Invalid email. Ensure that the email is not NULL");
        } while(true);
    }
}


