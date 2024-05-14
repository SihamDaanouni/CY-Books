import java.util.*;

public class DateAndTime {
    protected int day;
    protected int month;
    protected int year;
    protected int hour;
    protected int minute;
    protected int second;

    // Constructor for the class DateTime
    public DateAndTime(int day, int month, int year, int hour, int minute, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    // Check if the year is a leap year
    public boolean isLeapYear() {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // Check if the date is valid
    public boolean isValid() {
        if (month < 1 || month > 12)
            return false;
        if (day < 1 || day > 31)
            return false;
        if (month == 2) {
            if (isLeapYear()) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }

    // Check if the time is valid
    public boolean isTimeValid() {
        return (hour >= 0 && hour < 24) && (minute >= 0 && minute < 60) && (second >= 0 && second < 60);
    }

    // Getter for day
    public int getDay() {
        return day;
    }

    // Getter for month
    public int getMonth() {
        return month;
    }

    // Getter for year
    public int getYear() {
        return year;
    }

    // Getter for hour
    public int getHour() {
        return hour;
    }

    // Getter for minute
    public int getMinute() {
        return minute;
    }

    // Getter for second
    public int getSecond() {
        return second;
    }

    // Setter for the complete date and time
    public void setDateTime(int day, int month, int year, int hour, int minute, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * isDateExpired
     *
     * This method verify if a date has been passed comparing this date with the current date
     *
     * @return Boolean : true if the date has been passed
     *                 : false if the date has not been passed
     */
    public boolean isBorrowingPeriodExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime borrowingEndDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return currentDateTime.isAfter(borrowingEndDateTime);
    }
// Display the date if needed

    //public void displayDate (){
    //    system.out.println("Date : " +day+ "/" +month+ "/" +year+ "/ à : " + hour + ":" + minute+ " : " + second");
    //}

