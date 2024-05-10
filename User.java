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
    
}






