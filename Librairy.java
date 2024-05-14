import java.io.File;
import java.sql.ResultSet;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Librairy extends User {
    public Librairy(String name, String firstname, String address, String mail, String phone) {
        super(name, firstname, address, mail, phone );
    }

    public void Se_connecter() throws SQLException {
        Scanner compte = new Scanner(System.in);
        System.out.println("qu'elle' est votre compte utilisateur  ? ");
        String id = compte.nextLine();
        System.out.println(id);

        File myFile = new File("library");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 7);
        chemin = chemin + "src/library";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM library");
        while (res.next()) {
            String name = res.getString("identifiant");
            if (name.equals(id)) {
                System.out.println("oui");
                String firstname = res.getString("firstname");

                int mot_de_passe = res.getInt("mot_de_passe");
                System.out.println(name + " est root sur " + firstname + " avec pour password " + mot_de_passe);
                setFirstName(firstname);
                setName(name);
                setConnected(true);
            } else {
                System.out.println("o");
            }


        }
        co.close();


    }

    public boolean verif_co(String prenom , Client someone) throws SQLException {
        System.out.println(prenom);
        File myFile = new File("Client");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 6);
        chemin = chemin + "src/Client";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM Client");
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
            return true;}
        }
        co.close();
        System.out.println("over");

        System.out.println("oui");
        return false;

    }

    public void create(Client someone) throws SQLException {
        File myFile = new File("Client");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 6);
        chemin = chemin + "src/Client";
        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        String f=someone.getFirstName();
        String n=someone.getName();
        String a=someone.getaddress();
        String m=someone.getMail();
        String p=someone.getPhone();



        String SQL = ("INSERT INTO Client VALUES ('"+f+"','"+n+"','"+a+"','"+m+"','"+p+"')");
        stmt.executeUpdate(SQL);
        co.close();
        someone.setConnected(true);

    }

    //public crate
// j'ai pas touché le code, juste des commentaires
}