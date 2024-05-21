import java.util.*;

public class Date {
    protected int day;
    protected int month;
    protected int year;

    // Constructor for the class Date
    public Date (int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    //                ############################               VERIFIER qu'on ne crache pas; faire une boucle pour refaire.               ##############################################

    public boolean isBisextile(){
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // Verification of the validity of the date entered
    public boolean isValid() {
        if (month < 1 || month > 12)
            return false;
        if (day < 1 || day > 31)
            return false;

        if (month == 2){
            if (isBisextile()) {
                return day <= 29;
            }
            else {
                return day <= 28;
            }
        }

        else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }

    // Getter day
    public int getDay(){
        return day;
    }
    // Getter mounth
    public int getMounth(){
        return month;
    }

    // Getter year
    public int getYear(){
        return year;
    }

    // Setter of the complete date

    public void setDate(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Display the date if needed

    //public void displayDate (){
    //    system.out.println("Date : " +day+ "/" +month+ "/" +year);
    //


}
