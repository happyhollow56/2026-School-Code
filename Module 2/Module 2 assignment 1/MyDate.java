import java.util.GregorianCalendar;

/**
 * MyDate class represents a date with year, month, and day.
 * Month is 0-based (0 = January, 11 = December).
 * 
 * UML Diagram:
 * +----------------------------------+
 * |           MyDate                 |
 * +----------------------------------+
 * | - year: int                      |
 * | - month: int                     |
 * | - day: int                       |
 * +----------------------------------+
 * | + MyDate()                       |
 * | + MyDate(elapsedTime: long)      |
 * | + MyDate(year, month, day)       |
 * | + getYear(): int                 |
 * | + getMonth(): int                |
 * | + getDay(): int                  |
 * | + setDate(elapsedTime: long):    |
 * |     void                         |
 * +----------------------------------+
 */
public class MyDate {
    private int year;
    private int month;
    private int day;

    /** No-arg constructor creates a MyDate object for the current date */
    public MyDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        this.year = calendar.get(GregorianCalendar.YEAR);
        this.month = calendar.get(GregorianCalendar.MONTH);
        this.day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /** 
     * Constructs a MyDate object with a specified elapsed time 
     * since midnight, January 1, 1970, in milliseconds 
     */
    public MyDate(long elapsedTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(elapsedTime);
        this.year = calendar.get(GregorianCalendar.YEAR);
        this.month = calendar.get(GregorianCalendar.MONTH);
        this.day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /** 
     * Constructs a MyDate object with the specified year, month, and day 
     * Month is 0-based (0 = January)
     */
    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /** Getter for year */
    public int getYear() {
        return year;
    }

    /** Getter for month (0-based) */
    public int getMonth() {
        return month;
    }

    /** Getter for day */
    public int getDay() {
        return day;
    }

    /** Sets a new date using the elapsed time */
    public void setDate(long elapsedTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(elapsedTime);
        this.year = calendar.get(GregorianCalendar.YEAR);
        this.month = calendar.get(GregorianCalendar.MONTH);
        this.day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
    }
}
