import java.io.File;
import java.sql.ResultSet;

import java.sql.*;
import java.util.Scanner;

public class Librairy extends User {
    public Librairy(String name, String firstname, String address, String mail, String phone) {
        super(name, firstname, address, mail, phone);
    }

    /**
     * @throws SQLException
     */
    public void Se_connecter() throws SQLException {
        Scanner compte = new Scanner(System.in);
        System.out.println("Mettez votre mail fournie par la bibliothèque pour vous connecter. ");
        String id = compte.nextLine();
        System.out.println(id);


        File myFile = new File("database");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 8);
        chemin = chemin + "src/database";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM library");
        while (res.next()) {
            String mail = res.getString("mail");
            if (mail.equals(id)) {
                System.out.println("le client existe");
                String firstname = res.getString("firstname");

                int mot_de_passe = res.getInt("mot_de_passe");
                System.out.println(name + " est root sur " + firstname + " avec pour password " + mot_de_passe);
                setFirstName(firstname);
                setName(name);
                setConnected(true);
            } else {
                System.out.println("faux");
            }


        }
        co.close();
    }

    /**
     * @param prenom
     * @param someone
     * @return
     * @throws SQLException
     */

    public boolean verif_co(String prenom, Client someone) throws SQLException {
        System.out.println(prenom);
        File myFile = new File("database");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 8);
        chemin = chemin + "src/database";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM Client ");
        while (res.next()) {
            String firstname = res.getString("firstname");
            if (firstname.equals(prenom)) { // existe dans la table
                //firstname, name, address, mail, phone)
                String name = res.getString("name");
                String address = res.getString("address");
                String mail = res.getString("mail");
                String phone = res.getString("phone");

                someone.setFirstName(firstname);
                someone.setName(name);
                someone.setAddress(address);
                someone.setMail(mail);
                someone.setPhone(phone);
                someone.setConnected(true);

                System.out.println("trouver");
                co.close();
                return true;
            }
        }
        co.close();
        System.out.println("over");

        System.out.println("oui");
        return false;

    }


    public void connectionClosed(Connection co) {
        try {
            if (co != null) {
                co.close();
            }
        } catch (SQLException e) {
            // in case of cloturation error
            e.printStackTrace();
        }
    }

    public void create(Client someone) {
        // create a new connection with a value of 0 to close it when the request has been sent
        Connection co = null;
        try {
            // generate the Driver manager
            File myFile = new File("database");
            String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
            chemin = chemin.substring(0, chemin.length() - 8);
            chemin = chemin + "src/database";
            co = DriverManager.getConnection(chemin);

            Statement stmt = co.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS Client (" +
                    "name TEXT, " +
                    "firstName TEXT, " +
                    "address TEXT, " +
                    "mail TEXT PRIMARY KEY, " +
                    "phone TEXT)";
            stmt.execute(createTableSQL);

            String request = "INSERT INTO Client (name, firstName, address, mail, phone) " +
                    "VALUES (?, ?, ?, ?, ?)";
            // preparedStatement instead of statement because we want to integrate parameters
            PreparedStatement pstmt = co.prepareStatement(request);
            // Set values for parameters
            pstmt.setString(1, someone.getName());
            pstmt.setString(2, someone.getFirstName());
            pstmt.setString(3, someone.getaddress());
            pstmt.setString(4, someone.getMail());
            pstmt.setString(5, someone.getPhone());

            pstmt.executeUpdate();
            System.out.println("User creation is a success");

        } catch (SQLException e) {
            // the exceptions adapt to sql
            System.out.print(e.getMessage());
        }
        connectionClosed(co);
    }


    // update the name of the user
    public void updateName(Client someone) {
        Connection co = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a new name for " + someone.getFirstName() + " " + someone.getName() + " :");
            String newName = scanner.nextLine();

            File myFile = new File("database");
            String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
            chemin = chemin.substring(0, chemin.length() - 8);
            chemin = chemin + "src/database";
            co = DriverManager.getConnection(chemin);

            String rec = "UPDATE Client SET name = ? WHERE mail = ?";
            try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                pstmt.setString(1, newName);
                pstmt.setString(2, someone.getMail());
                pstmt.executeUpdate();
                System.out.println("User name update is a success");
                someone.setName(newName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connectionClosed(co);
    }


    // update the first name of the user
    public void updateFirstName(Client someone) {
        Connection co = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a new first name for " + someone.getFirstName() + " " + someone.getName() + " :");
            String newFirstName = scanner.nextLine();

            File myFile = new File("database");
            String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
            chemin = chemin.substring(0, chemin.length() - 8);
            chemin = chemin + "src/database";
            co = DriverManager.getConnection(chemin);

            String rec = "UPDATE Client SET firstname = ? WHERE mail = ?";
            try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                pstmt.setString(1, newFirstName);
                pstmt.setString(2, someone.getMail());
                pstmt.executeUpdate();
                System.out.println("User first name update is a success");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connectionClosed(co);
    }

    // update the address of the user
    public void updateAddress(Client someone) {
        Connection co = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a new address for " + someone.getFirstName() + " " + someone.getName() + " :");
            String newAddress = scanner.nextLine();

            File myFile = new File("database");
            String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
            chemin = chemin.substring(0, chemin.length() - 8);
            chemin = chemin + "src/database";
            co = DriverManager.getConnection(chemin);

            String rec = "UPDATE Client SET address = ? WHERE mail = ?";
            try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                pstmt.setString(1, newAddress);
                pstmt.setString(2, someone.getMail());
                pstmt.executeUpdate();
                System.out.println("User address update is a success");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connectionClosed(co);
    }

    // update the phone number of the user
    public void updatePhone(Client someone) {
        Connection co = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a new phone number for " + someone.getFirstName() + " " + someone.getName() + " :");
            String newPhone = scanner.nextLine();

            File myFile = new File("database");
            String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
            chemin = chemin.substring(0, chemin.length() - 8);
            chemin = chemin + "src/database";
            co = DriverManager.getConnection(chemin);

            String rec = "UPDATE Client SET phone = ? WHERE mail = ?";
            try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                pstmt.setString(1, newPhone);
                pstmt.setString(2, someone.getMail());
                pstmt.executeUpdate();
                System.out.println("User phone update is a success");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connectionClosed(co);
    }

    /**
     * getBorrow
     * <p>
     * show all the borrow return or all the borrow present associated with a client.
     *
     * @param someone The client whose borrowings are to be retrieved.
     */

    public void getBorrow(Client someone) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        // Scanner actual or history
        System.out.println("Choose an option :");
        System.out.println("1. Borrow");
        System.out.println("2. History");
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        Integer selectIsReturn = null;

        switch (choice) {
            case 1:
                selectIsReturn = 0;
                break;
            case 2:
                selectIsReturn = 1;
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
                break;
        }

        scanner.close();

        Connection co = DriverManager.getConnection("jdbc:sqlite:database.db");

        // prepare to select all the borrow from the users
        String request = "SELECT * FROM Borrow WHERE mail = ? AND isReturn = ?";
        PreparedStatement pstmt = co.prepareStatement(request);

        // replace the mail with the user one
        pstmt.setString(1, someone.getMail());
        pstmt.setInt(2, selectIsReturn);
        if (getMail() != "_") ;
        {
            ;
        }

        // execute
        ResultSet rs = pstmt.executeQuery();

        // loop to show all the result
        while (rs.next()) {
            Integer ISBN = rs.getInt("ISBN");
            String mail = rs.getString("mail");
            String name = rs.getString("name");
            String firstname = rs.getString("firstName");
            String title = rs.getString("title");
            Timestamp timeBorrowStart = rs.getTimestamp("timeBorrowStart");
            Timestamp timeBorrowEnd = rs.getTimestamp("timeBorrowEnd");
            Boolean isReturn = rs.getBoolean("isReturn");
            Integer nbDelays = rs.getInt("nbDelays");

            // display it on the console
            System.out.println("ISBN: " + ISBN);
            System.out.println("Mail: " + mail);
            System.out.println("Name: " + name);
            System.out.println("First Name: " + firstname);
            System.out.println("Title: " + title);
            System.out.println("Time Borrow Start: " + timeBorrowStart);
            System.out.println("Time Borrow End: " + timeBorrowEnd);
            System.out.println("Is Return: " + isReturn);
            System.out.println("Number of Delays: " + nbDelays);
            System.out.println("---------------------------------------");
        }

        // cloturation of ressources
        rs.close();
        pstmt.close();
        co.close();
    }


    public void select(Client someone, book laws, Borrow manip) throws SQLException {
        //
        //
        //
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter l'Isbn voulue");
        int valeur = sc.nextInt();


        File myFile = new File("database");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 8);
        chemin = chemin + "src/database";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();

        ResultSet res = stmt.executeQuery("SELECT * FROM Books");
        while (res.next()) {
            String isbn = res.getString("isbn");
            if (isbn.equals(valeur)) { // existe dans la table
                //firstname, name, address, mail, phone)
                String mail = someone.getMail(); //
                String firstname = someone.getFirstName();
                String name = someone.getName();
                String title = res.getString("titre");


                someone.setFirstName(firstname);
                someone.setName(name);
                someone.setAddress(address);
                someone.setMail(mail);
                someone.setPhone(phone);
                someone.setConnected(true);

                manip.toBorrow();

            }
        }


    }

    /**
     * getBorrow overload
     * <p>
     * show all the borrow return or all the borrow present associated with a client. OFFLINE VERSION
     */
    public void getBorrow() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        // Scanner search mail
        System.out.print("Enter a mail :");
        String searchedMail = scanner.nextLine();
        // Scanner actual or history
        System.out.println("Choose an option :");
        System.out.println("1. Borrow");
        System.out.println("2. History");
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        Integer selectIsReturn = null;

        switch (choice) {
            case 1:
                selectIsReturn = 0;
                break;
            case 2:
                selectIsReturn = 1;
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
                break;
        }

        scanner.close();

        Connection co = DriverManager.getConnection("jdbc:sqlite:database.db");

        // prepare to select all the borrow from the users
        String request = "SELECT * FROM Borrow WHERE mail = ? AND isReturn = ?";
        PreparedStatement pstmt = co.prepareStatement(request);

        // replace the mail with the user one
        pstmt.setString(1, searchedMail);
        pstmt.setInt(2, selectIsReturn);
        if (getMail() != "_") ;
        {
            ;
        } // Je pense qu'un equal() serait plus adapter   -Amaury

        // execute
        ResultSet rs = pstmt.executeQuery();

        // loop to show all the result
        while (rs.next()) {
            Integer ISBN = rs.getInt("ISBN");
            String mail = rs.getString("mail");
            String name = rs.getString("name");
            String firstname = rs.getString("firstName");
            String title = rs.getString("title");
            Timestamp timeBorrowStart = rs.getTimestamp("timeBorrowStart");
            Timestamp timeBorrowEnd = rs.getTimestamp("timeBorrowEnd");
            Boolean isReturn = rs.getBoolean("isReturn");
            Integer nbDelays = rs.getInt("nbDelays");

            // display it on the console
            System.out.println("ISBN: " + ISBN);
            System.out.println("Mail: " + mail);
            System.out.println("Name: " + name);
            System.out.println("First Name: " + firstname);
            System.out.println("Title: " + title);
            System.out.println("Time Borrow Start: " + timeBorrowStart);
            System.out.println("Time Borrow End: " + timeBorrowEnd);
            System.out.println("Is Return: " + isReturn);
            System.out.println("Number of Delays: " + nbDelays);
            System.out.println("---------------------------------------");
        }

        // cloturation of ressources
        rs.close();
        pstmt.close();
        co.close();
    }
}

    //public crate
// j'ai pas touché le code, juste des commentaires
