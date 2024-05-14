

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, ParserConfigurationException, IOException, InterruptedException, SAXException {

        // declaration des objets de base

       Librairy actif = new Librairy("none", "none" ,"none" , "none", "none" );
       Client someone = new Client("_", "-" ,"_" , "_", "_" );
       book laws =new book("_","_","_","_","_","_","_");







       // ##############################  MENUE  ####################################


        boolean session = true;
        boolean activate = true;
        while (activate){ coLibrary(actif) ;
            System.out.println(actif.getName());
            System.out.println(actif.getFirstName());
            if(actif.getConnected()){activate=false;}



        }






        while (session){
            menu();
            Scanner choix= new Scanner(System.in);
            System.out.println("que voulez vous tester ?  ");
            int valeur =choix.nextInt();
          if (valeur==1) {
              coCrea(someone,actif);
              System.out.println(" test co client || create client ");

            }
          else if (valeur==2) {test(laws);}

          else if (valeur==4){if (!someone.getConnected()){System.out.println("personne n'est co");}
                              else {deco(someone);}}


          else if (valeur==5){session=false;
                            exit(0);}


            }



        User siham= new Librairy("siham", "test" ,"Cytech" , "a@i.fr", "077889" );


    }
    public static void test (book laws) throws SQLException, ParserConfigurationException, IOException, InterruptedException, SAXException {
            laws.toString();
            laws.recup();
            String search =  "Arkady Martine";
            laws.recherche(search);
            laws.toString();
    }


    public static Librairy coLibrary(Librairy actif) throws SQLException {
        actif.Se_connecter();
        return actif;

    }
    public static Client coCrea(Client someone,Librairy actif) throws SQLException {
        boolean alpha;

        // entrer le nom du clien
        // si booleen true ok
        // si boolen false crer client

        Scanner perso =  new Scanner(System.in);
        System.out.println(" votre prenom ? ");
        String id =perso.nextLine();
        someone.toString();
        alpha=actif.verif_co(id , someone);
        someone.toString();
        System.out.println("existe "+alpha);

        if (!alpha){
            someone.recupdata();
            System.out.println("ajout d'element");
            someone.toString();
            actif.create(someone);
            //actif.
        }

        return someone ;
    }

    public static Client deco(Client someone) throws SQLException {
      someone.toString();
      someone = new Client("_", "-" ,"_" , "_", "_" );
      someone.toString();


        return someone ;
    }

    public static void  menu(){
        System.out.println(" votre fonction pour commencer ;");

        System.out.println(" 1 : test co client || create client ");
        System.out.println(" 2: recherche par titre ");

        System.out.println(" 2.1 : emprunt du livre  ");
        System.out.println(" 4 : deco client ");
        System.out.println(" 5 : deco de la bibliotequaire ( fin de l 'exe ) ");


    }








}
