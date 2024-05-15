import java.util.Calendar;
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
    protected String firstName;
    protected String title;
    protected Date timeBorrowStart;
    protected Date timeBorrowEnd;
    protected Boolean isReturn;
    protected Integer nbDelays;

    // Constructor who get the information with research data
    public Borrow(Integer ISBN, String mail, String name, String firstName, String title, Date timeBorrowEnd, Integer nbDelays) {
        this.ISBN = ISBN;
        this.mail = mail;
        // the next 3 parameters are used to make the list of borrow more readable without fetching new information from the database.
        this.name = name;
        this.firstName = firstName;
        this.title = title;
        // set to the actual date
        this.timeBorrowStart = new Date();
        this.timeBorrowEnd = timeBorrowEnd;
        // set to False because the client just borrow it and will not immediatly return it
        this.isReturn = Boolean.FALSE;
        this.nbDelays = nbDelays; // je sais pas encore si cette variable est utile (à mettre dans emprunt)
    }

    // First Condition, all the copy of the books are already borrow.
     public boolean isAlreadyBorrow() {
         Connection co = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;

         try {
             co = DriverManager.getConnection("jdbc:sqlite:database.db");

             // Verification that there is not enough books who have the same ISBN and aren't return
             String countISBNQuery = "SELECT COUNT(*) FROM Borrow WHERE ISBN = ? AND isReturn = FALSE";
             pstmt = co.prepareStatement(countISBNQuery);
             pstmt.setInt(1, this.ISBN);
             rs = pstmt.executeQuery();

             if (rs.next()) {
                 int count = rs.getInt(1);
                 return count >= MAX_BORROW_COUNT; // Return True because there is at least one book to borrow
             }

         } catch (SQLException e) {
             System.out.println("Error : " + e.getMessage());
         } finally {
             // cloturation of ressources
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

         return false; // if there are not any exception than return false
     }

     // Second Condition, the client has already borrow the maximum number of books.
     public boolean hasBorrowTooManyTimes() {
         Connection co = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;

         try {
             co = DriverManager.getConnection("jdbc:sqlite:database.db");

             // Count the number of books currently borrow by the client
             String countQuery = "SELECT COUNT(*) FROM Borrow WHERE mail = ? AND isReturn = FALSE";
             pstmt = co.prepareStatement(countQuery);
             pstmt.setString(1, this.mail);
             rs = pstmt.executeQuery();

             if (rs.next()) {
                 int count = rs.getInt(1);
                 return count >= MAX_BORROW_USER; // return True because the Client has not borrow the maximum number of book (10 in this case)
             }

         } catch (SQLException e) {
             System.out.println("Error : " + e.getMessage());
         } finally {
             // cloturation of ressources
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

         return false; // if there are not any exception than return false
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
                    "nbDelays INTEGER, " +
                    "PRIMARY KEY (ISBN, mail, timeBorrowStart), " +
                    "FOREIGN KEY (ISBN) REFERENCES Books(ISBN), " +
                    "FOREIGN KEY (mail) REFERENCES Users(mail)" +
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
            pstmt.setString(4, this.firstName);
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

     /**
      * BestBooksLastMonth.
      *
      * This method determinate the book most borrowed based on the last month information
      *
      * @return It's a void, does not return anything
      */
     public static void bestBooksLastMonth() {
         Connection co = null;
         try {
             co = DriverManager.getConnection("jdbc:sqlite:database.db");

             // recuperate the date from one month ago
             Calendar cal = Calendar.getInstance();
             cal.add(Calendar.MONTH, -1);
             Date oneMonthAgo = cal.getTime();

             // SQL querry to select the most borrowed books during the last month
             String selectSQL = "SELECT title, COUNT(*) AS borrowCount " +
                     "FROM Borrow " +
                     "WHERE timeBorrowStart >= ? " +
                     "GROUP BY title " +
                     "ORDER BY borrowCount DESC " +
                     "LIMIT 10"; // We only select the top 10.

             PreparedStatement pstmt = co.prepareStatement(selectSQL);
             pstmt.setTimestamp(1, new Timestamp(oneMonthAgo.getTime()));
             ResultSet rs = pstmt.executeQuery();

             // Display the result that has been stocked in the resultSet after ther SQL Querry execution
             System.out.println("Best books borrowed last month:");
             while (rs.next()) {
                 String title = rs.getString("title");
                 int borrowCount = rs.getInt("borrowCount");
                 System.out.println(title + ": " + borrowCount + " borrows");
             }
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         } finally {
             // Deconnexion
             try {
                 if (co != null) {
                     co.close();
                 }
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
         }
     }

  /**
     * deleteUser
     *
     * Delete the user from the database, and take back his borrowed books
     *
     * @param email The email adress of the client who will be deleted from the database.
     * @return deleteUser doesn't return anything.
     */

    public void deleteUser(String email) {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");


            // looking for the client by his email to delete from the database
            String deleteSQL = "DELETE FROM Client WHERE mail = ?";

            //the client is deleted from the database
            PreparedStatement pstmt = co.prepareStatement(deleteSQL);
            pstmt.setString(1, email);


            pstmt.executeUpdate();
            System.out.println("The client has been deleted from the client database");

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

        // create a new connection with a value of 0 to close it when the request has been sent, to take back his books
        co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");

            // Update isReturn to 1 for the client with the given email
            String updateSQL = "UPDATE Books SET isReturn = 1 WHERE mail = ?";
            PreparedStatement updatePstmt = co.prepareStatement(updateSQL);
            updatePstmt.setString(1, email);

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
        // these next few lines are used to disconnect the driver
        try {
            if (co != null) {
                co.close();
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

    }

    /**
     * lateBook
     *
     * Check if the client have delays on his borrows, and if there are 3 or more, the user is deleted from the database.
     *
     * @param email The email adress of the client who will be checked.
     * @return lateBook doesn't return anything.
     */

    public void lateBook(String email) {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");


            // looking for the client by his email to count his delays
            String lateSearch = "SELECT COUNT(*) AS count FROM Borrow " +
                    "WHERE mail = (SELECT id FROM Client WHERE email = ?) AND nbDelay = 1";
            //there is no delay variable in the database

            PreparedStatement pstmt = co.prepareStatement(lateSearch);
            pstmt.setString(1, email);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.executeQuery();
            int nbdelay=0;
            //getting the delays
            nbdelay = rs.getInt("count");
            if(nbdelay==0){
                //the client has no delays, nothing to do
                System.out.println("The client has no delay");
            }
            else if (nbdelay>=3) {
                //the client has 3 or more delays, he will be ban from the database and his boroowed books will be returned
                System.out.println("The client has 3 or more delays, the client will be banned");
                deleteUser(email);
            }
            else{
                //had delays, showing a warning only
                System.out.println("The client has delays on"+nbdelay+"books, don't forgot to return the books borrowed");
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
        // these next few lines is used to deconnect the driver
        try{
            if(co != null){
                co.close();
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    /**
     * updateDelay
     *
     * Check if the borrowed books will be returned with delay. updateDelay has no parameters and no return value
     */

    public void updateDelay() {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            co = DriverManager.getConnection("jdbc:sqlite:database.db");

            // Update delay to 1 where dateTimeBorrowEnd is less than the current timestamp
            String updateDelaySQL = "UPDATE Books SET nbDelay = 1 WHERE dateTimeBorrowEnd < CURRENT_TIMESTAMP";
            PreparedStatement updateDelayPstmt = co.prepareStatement(updateDelaySQL);

            int rowsUpdated = updateDelayPstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The delay field has been updated for overdue clients");
            } else {
                System.out.println("No overdue clients found");
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
        try {
            if (co != null) {
                co.close();
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

    }

     // -------------------------------------------------------TEST-------------------------------------------//
     //public static void main(String[] args) {
     // test un the main
     //bestBooksLastMonth();
     //}
}


