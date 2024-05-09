import java.sql.*;
import java.sql.DriverManager;


public class CreateTable
{
    public static void main(String[] args)
    {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");

    }
}