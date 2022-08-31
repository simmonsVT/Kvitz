package Model;

public class Studente extends Utente {

    public Studente(){
        super();
    }

    public Studente(String nome,String cognome,String username,String password){
        super(nome,cognome,username,password);
    }

    public Studente(String user_name){
        super(user_name);
    }


}
