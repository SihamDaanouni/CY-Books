import java.text.DateFormat;
import java.util.Date;

public abstract class  User {
    protected String Name;
    protected String FirstName;
    protected String address;
    protected String mail;
    protected  String phone;

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
        public String getAdress(){
                return this.adress ;
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
        public boolean isConnected(){
                return this.connected ;
        }

        // methode to create an user
        public void createUser() {
            try{
                // for now I set up the database name with "database.db"
                Connection co = DriverManager.getConnection("jdbc:sqlite:database.db");
    
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
                System.out.println("User creation was a success");
    
            } catch (SQLException e) {
                // the exceptions adapt to sql
                System.out.print(e.getMessage());
            }
        }
}






