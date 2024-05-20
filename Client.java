import java.util.Scanner;

public class Client extends User {

    public Client (String name,String firstname,String address,String mail,String phone){

       super(name,firstname,address,mail,phone);




        }
    public void recupdata()  {
        Scanner perso =  new Scanner(System.in);
        System.out.println(" votre prenom? ");
        String prenom  =perso.nextLine();
        setFirstName(prenom);

        perso =  new Scanner(System.in);
        System.out.println(" votre nom ? ");
        String name =perso.nextLine();
        setName(name);

        perso =  new Scanner(System.in);
        System.out.println(" votre address ? ");
        String address =perso.nextLine();
        setAddress(address);

        perso =  new Scanner(System.in);
        System.out.println(" votre mail ? ");
        String mail =perso.nextLine();
        setMail(mail);

        perso =  new Scanner(System.in);
        System.out.println(" votre phone ? ");
        String phone =perso.nextLine();
        setPhone(phone);
    }
    @Override
    public String toString(){
        System.out.println(getFirstName()+" "+getName()+" "+getaddress()+" "+getMail()+" "+getPhone()+" "+getConnected());
        return null;
    }

        //getters, setters,
/*
        public void borrowBook(){

        }

        public void subscription(){

        }

        public void remainingBorrowPeriod(){

        }*/


}






