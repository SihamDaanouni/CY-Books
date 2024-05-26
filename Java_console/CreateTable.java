import java.sql.*;
public class CreateTable {
    public static void main(String[] args) throws SQLException {
        try {
            //Modifier par l'emplacement de la base de donnée, là c'est la mienne
            //Il faut créer une bdd à partir de database
            Connection co = DriverManager.getConnection("jdbc:sqlite:C:/Users/CYTech Student/IdeaProjects/SQL/src/Personne");
            Statement stmt = co.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Personne");
            while(res.next()){
                String name = res.getString("PRENOM");
                int age = res.getInt("AGE");
                System.out.println(name+" a "+age+" ans.");

            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
