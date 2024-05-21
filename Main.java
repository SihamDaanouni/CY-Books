

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
       DateAndTime today = new DateAndTime(0,0,0,0,0,0);

        //j'ai rajouté un attribut thème dans le books donc j'ai rajouté un underscore en plus







       // 1 - actid co
        // acrif .boorox (someone)
                    //methode boreow  somone . get conected
                    //client -->








       //  ##############################  MENUE  ####################################


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
            System.out.println("Que voulez vous faire ?");
            int valeur =choix.nextInt();
          if (valeur==1) {
              coCrea(someone,actif);
              System.out.println("Connexion client ou création de client");

            }
          else if (valeur==2) {test(laws);}

          else if (valeur==4){if (!someone.getConnected()){System.out.println("Aucun client n'est connecté.");}
                              else {deco(someone);}}


          else if (valeur==5){session=false;
                            exit(0);}
          else if (valeur==3){laws.reset();}



           else if (valeur==6){
               // bloquer si non co
              actif.updateName(someone);}
          else if (valeur==7){
            actif.updateFirstName(someone);
          }
          else if (valeur==8) {
              actif.updateAddress(someone);
          }
          else if (valeur==9) {
              actif.updatePhone(someone);
          }
          else if (valeur==10) {
              actif.select(someone,laws);

          }

        }



        User siham= new Librairy("siham", "test" ,"Cytech" , "a@i.fr", "077889" );


    }
    public static void test (book laws) throws SQLException, ParserConfigurationException, IOException, InterruptedException, SAXException {
             //laws.toString();affiche les data du livre
            // laws.recup();
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
        System.out.println("Voici ce que vous pouvez faire sur CYBooks :");

        System.out.println(" 1 : Connexion client ou création de client");
        System.out.println(" 2 : Recherche de documents ");
        System.out.println(" 2.1 : Emprunter un livre");
        System.out.println(" 4 : Deconnexion du client ");
        System.out.println(" 5 : Deconnexion de la bibliothécaire (Fin de la session)");
        System.out.println(" 6 : Mettre à jour le nom du client");
        System.out.println(" 7 : Mettre à jour le prénom du client");
        System.out.println(" 8 : Mettre à jour l'adresse du client");
        System.out.println(" 9 : Mettre à jour le numéro de téléphone du client");



    }

            //






}
