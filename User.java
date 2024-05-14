import java.text.DateFormat;
import java.util.Date;
import java.sql.*;

public abstract class  User {
    protected String name;
    protected String firstName;
    protected String address;
    protected String mail;
    protected String phone;

    //*protected Date dateOfBirth;
    protected boolean connected;

    //User constructor
    public User (String name,String firstName,String address,String mail,String phone){
        this.name=name;
        this.firstName=firstName;
        this.address=address;
        this.mail=mail;
        this.phone=phone;
        this.connected=false;
    }


        // name setter
       public void setName(String name){
                this.name = name ;
        }
        
        // first name setter
        public void setFirstName(String firstName){
                this.firstName = firstName ;
        }

        // adress setter
        public void setAddress(String address){
                this.address = address ;
        }

        // mail setter
        public void setMail(String mail){
                this.mail = mail ;
        }

        // phone setter
        public void setPhone(String phone){
                this.phone = phone ;
        }

        // connection setter
        public void setConnected(boolean connected){
                this.connected = connected ;
        }


        // name getter
        public String getName(){
                return this.name ;
        }

        // first name getter
        public String getFirstName(){
                return this.firstName ;
        }

        // adress getter
        public String getaddress(){return this.address ;
        }

        // mail getter
        public String getMail(){
                return this.mail ;
        }

        // phone getter
        public String getPhone(){
                return this.phone ;
        }

        // connection getter
        public boolean getConnected(){
                return this.connected ;
        }


        // Methode SQL/Connexion
    // ! DON'T FORGET TO CHANGE THE NAME OF THE DATABASE !
    
        public void connectionClosed(Connection co){
            try{
                if(co != null){
                    co.close();
                }
            } catch (SQLException e) {
                // in case of cloturation error
                e.printStackTrace();
            }
        }
    
        // methode to create an user
        public void createUser() {
            // create a new connection with a value of 0 to close it when the request has been sent
            Connection co = null;
            try{
                // generate the Driver manager
                co = DriverManager.getConnection("jdbc:sqlite:database.db");
    
                Statement stmt = co.createStatement();
    
                String createTableSQL = "CREATE TABLE IF NOT EXISTS USER (id INTEGER PRIMARY KEY, name TEXT, firstName TEXT, adresse TEXT, mail TEXT, phone TEXT)";
                stmt.execute(createTableSQL);
    
                String request = "INSERT INTO USER (name, firstName, adresse, mail, phone) " +
                        "VALUES (?, ?, ?, ?, ?)";
                // preparedStatement instead of statement because we want to integrate parameters
                PreparedStatement pstmt = co.prepareStatement(request);
                // Set values for parameters
                pstmt.setString(1, this.Name);
                pstmt.setString(2, this.FirstName);
                pstmt.setString(3, this.address);
                pstmt.setString(4, this.mail);
                pstmt.setString(5, this.phone);
    
                pstmt.executeUpdate();
                System.out.println("User creation is a success");
    
            } catch (SQLException e) {
                // the exceptions adapt to sql
                System.out.print(e.getMessage());
            }
            connectionClosed(co);
        }
    
    
    /*
        // update user
        public void updateUser(String n, String fn, String adrs, String ph) {
            Connection co = null;
            try{
                co = DriverManager.getConnection("jdbc:sqlite:database.db");
    
                String rec = "UPDATE USER SET name = ?, firstName = ?, adresse = ?, phone = ? WHERE mail = ?";
                PreparedStatement pstmt = co.prepareStatement(rec);
    
                pstmt.setString(1, n);
                pstmt.setString(2, fn);
                pstmt.setString(3, adrs);
                pstmt.setString(4, ph);
                pstmt.setString(5, this.mail);
    
                pstmt.executeUpdate();
                System.out.println("User update is a success");
    
            } catch (SQLException e) {
                System.out.print(e.getMessage());
            }
            connectionClosed(co);
        }
    */
    
            // update the name of the user
            public void updateName(String newName) {
                try (Connection co = DriverManager.getConnection("jdbc:sqlite:database.db")) {
                    String rec = "UPDATE USER SET name = ? WHERE mail = ?";
                    try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                        pstmt.setString(1, newName);
                        pstmt.setString(2, this.mail);
                        pstmt.executeUpdate();
                        System.out.println("User name update is a success");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
    
            // update the first name of the user
        public void updateFirstName(String newFirstName) {
            try (Connection co = DriverManager.getConnection("jdbc:sqlite:database.db")) {
                String rec = "UPDATE USER SET firstname = ? WHERE mail = ?";
                try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                    pstmt.setString(1, newFirstName);
                    pstmt.setString(2, this.mail);
                    pstmt.executeUpdate();
                    System.out.println("User first name update is a success");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    
            // update the address of the user
        public void updateAddress(String newAddress) {
            try (Connection co = DriverManager.getConnection("jdbc:sqlite:database.db")) {
                String rec = "UPDATE USER SET address = ? WHERE mail = ?";
                try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                    pstmt.setString(1, newAddress);
                    pstmt.setString(2, this.mail);
                    pstmt.executeUpdate();
                    System.out.println("User address update is a success");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    
            // update the phone number of the user
        public void updatePhone(String newPhone) {
            try (Connection co = DriverManager.getConnection("jdbc:sqlite:database.db")) {
                String rec = "UPDATE USER SET phone = ? WHERE mail = ?";
                try (PreparedStatement pstmt = co.prepareStatement(rec)) {
                    pstmt.setString(1, newPhone);
                    pstmt.setString(2, this.mail);
                    pstmt.executeUpdate();
                    System.out.println("User phone update is a success");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    
}






