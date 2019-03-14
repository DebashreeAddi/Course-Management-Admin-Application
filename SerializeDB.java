package scrame;

import java.io.*;
import java.util.*;

public class SerializeDB {
    public static List readSerializedObject (String filename) {
        List pdetails = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;

        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            pdetails = (ArrayList<?>) in.readObject();
            in.close();
        } catch (IOException ex) {

        } catch (ClassNotFoundException ex) {

        }

        return pdetails;
    }

    public static void writeSerializedObject (String filename, List list) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try{
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(list);
            out.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
