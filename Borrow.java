import java.util.Date;
import java.sql.*;

 /*
    For testing put this in Main :
            // setup a date of return to 24hour after the borrow
                Date currentDate = new Date();
                long millisInOneDay = 24 * 60 * 60 * 1000; // Nombre de millisecondes dans une journée
                Date date24HoursLater = new Date(currentDate.getTime() + millisInOneDay);

                Borrow firstLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
                firstLoan.toBorrow();
 */
public class Borrow {
    protected Integer ISBN;
    protected String mail;
    protected String name;
    protected String firstname;
    protected String title;
    protected Date timeBorrowStart;
    protected Date timeBorrowEnd;
    protected Boolean isReturn;
    protected Integer nbDelays;

    // Constructor who get the information with research data
    public Borrow(Integer ISBN, String mail, String name, String firstname, String title, Date timeBorrowEnd, Integer nbDelays) {
        this.ISBN = ISBN;
        this.mail = mail;
        // the next 3 parameters are used to make the list of borrow more readable without fetching new information from the database.
        this.name = name;
        this.firstname = firstname;
        this.title = title;
        // set to the actual date
        this.timeBorrowStart = new Date();
        this.timeBorrowEnd = timeBorrowEnd;
        // set to true because the client just borrow it
        this.isReturn = Boolean.FALSE;
        this.nbDelays = nbDelays; // je sais pas encore si cette variable est utile (à mettre dans emprunt)
    }

    public void toBorrow() {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");

            Statement stmt = co.createStatement();

            // this is only in purpose of a test
            String dropTableSQL = "DROP TABLE IF EXISTS Borrow";
            stmt.executeUpdate(dropTableSQL);

            // create the table if not exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Borrow (" +
                    "ISBN INTEGER, " +
                    "mail VARCHAR(255), " +
                    "name VARCHAR(255), " +
                    "firstname VARCHAR(255), " +
                    "title VARCHAR(255), " +
                    "timeBorrowStart TIMESTAMP, " +
                    "timeBorrowEnd TIMESTAMP, " +
                    "isReturn BOOLEAN, " +
                    "nbDelays INTEGER, " +
                    ")";
            stmt.execute(createTableSQL);

            // insert our new borrow ticket
            String insertSQL = "INSERT INTO Borrow (ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn, nbDelays) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = co.prepareStatement(insertSQL);

            pstmt.setInt(1, this.ISBN);
            pstmt.setString(2, this.mail);
            pstmt.setString(3, this.name);
            pstmt.setString(4, this.firstname);
            pstmt.setString(5, this.title);
            // these two below are convert into Timestamp in the database
            pstmt.setTimestamp(6, new Timestamp(timeBorrowStart.getTime()));
            pstmt.setTimestamp(7, new Timestamp(timeBorrowEnd.getTime()));
            pstmt.setBoolean(8, this.isReturn);
            pstmt.setInt(9, this.nbDelays);

            pstmt.executeUpdate();
            System.out.println("The borrow creation is a success");

            System.out.println("Time Borrow Start: " + this.timeBorrowStart);
            System.out.println("Time Borrow End: " + this.timeBorrowEnd);

        } catch (SQLException e) {
            // the exceptions adapt to sql
            System.out.print(e.getMessage());
        }
        // these next few lines is used to deconnect the driver
        try{
            if(co != null){
                co.close();
            }
        } catch (SQLException e) {
            // in case of cloturation error
            e.printStackTrace();
        }
    }
}
