package scrame;

public class TextFormat {
    public static String capitalize(String text){
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    public static String scoreToGrade(double score) {
        double gradeDist = 90.0;
        double gradeA = 80.0;
        double gradeB = 70.0;
        double gradeC = 60.0;
        double gradeD = 50.0;

        if (score >= gradeDist) return "DIST";
        else if (score >= gradeA) return "A";
        else if (score >= gradeB) return "B";
        else if (score >= gradeC) return "C";
        else if (score >= gradeD) return "D";

        return "F";
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}

