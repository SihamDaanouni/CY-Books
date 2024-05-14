import java.util.Date;
import java.sql.*;

 /*
    For testing put this in Main :
            // setup a date of return to 24hour after the borrow
            Date currentDate = new Date();
            long millisInOneDay = 24 * 60 * 60 * 1000; // Nombre de millisecondes dans une journée
            Date date24HoursLater = new Date(currentDate.getTime() + millisInOneDay);

            Borrow firstLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow SndLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow TrdLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow FrthLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow FivLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow SixLoan = new Borrow(1234,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow loan1 = new Borrow(1235,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow loan2 = new Borrow(1236,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow loan3 = new Borrow(1237,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow loan4 = new Borrow(1238,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            Borrow loan5 = new Borrow(1239,"mail@test","Pro","Amo","Le mythe de Sisyphe",date24HoursLater,0);
            firstLoan.toBorrow();
            SndLoan.toBorrow();
            TrdLoan.toBorrow();
            FrthLoan.toBorrow();
            FivLoan.toBorrow();
            SixLoan.toBorrow();
            loan1.toBorrow();
            loan2.toBorrow();
            loan3.toBorrow();
            loan4.toBorrow();
            loan5.toBorrow();
 */
public class Borrow {
     private static final int MAX_BORROW_COUNT = 5;
     private static final int MAX_BORROW_USER = 10;

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
        // set to False because the client just borrow it and will not immediatly return it
        this.isReturn = Boolean.FALSE;
        this.nbDelays = nbDelays; // je sais pas encore si cette variable est utile (à mettre dans emprunt)
    }

    // First Condition, all the copies of the books are already borrowed.
    public boolean isAlreadyBorrow() throws SQLException {
        Connection co = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");
    
            // Verification that there are not enough books with the same ISBN and not returned
            String countISBNQuery = "SELECT COUNT(*) FROM Borrow WHERE ISBN = ? AND isReturn = FALSE";
            pstmt = co.prepareStatement(countISBNQuery);
            pstmt.setInt(1, this.ISBN);
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int count = rs.getInt(1);
                return count >= MAX_BORROW_COUNT; // Returns true because there is at least one book to borrow
            }
    
        } finally {
            // Closing resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (co != null) {
                    co.close();
                }
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    
        return false; // Returns false if there are no exceptions
    }
    
    // Second Condition, the client has already borrowed the maximum number of books.
    public boolean hasBorrowTooManyTimes() throws SQLException {
        Connection co = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");
    
            // Count the number of books currently borrowed by the client
            String countQuery = "SELECT COUNT(*) FROM Borrow WHERE mail = ? AND isReturn = FALSE";
            pstmt = co.prepareStatement(countQuery);
            pstmt.setString(1, this.mail);
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int count = rs.getInt(1);
                return count >= MAX_BORROW_USER; // Returns true because the Client has not borrowed the maximum number of books (10 in this case)
            }
    
        } finally {
            // Closing resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (co != null) {
                    co.close();
                }
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    
        return false; // Returns false if there are no exceptions
    }



     // methode to Borrow the "ticket" into the database
    public void toBorrow() {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");

            Statement stmt = co.createStatement();

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
                    "nbDelays INTEGER " +
                    ")";
            stmt.execute(createTableSQL);

            // FIRST CONDITION
            if (isAlreadyBorrow()) {
                System.out.println("Error: The ISBN is already borrowed (" + MAX_BORROW_COUNT + "). Unable to make a new borrow.");
                return; // Cancel insertion
            }
            // SECOND CONDITION
            if (hasBorrowTooManyTimes()) {
                System.out.println("Error: The client has already borrowed " + MAX_BORROW_USER + " books. Unable to make a new borrow.");
                return; // Cancel insertion
            }
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
            System.out.println("Error : " + e.getMessage());
        }
        // these next few lines is used to deconnect the driver
        try{
            if(co != null){
                co.close();
            }
        } catch (SQLException e) {
            // in case of cloturation error
            System.out.println("Error : " + e.getMessage());
        }
    }
}