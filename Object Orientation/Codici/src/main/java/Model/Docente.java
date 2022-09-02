package Model;

public class Docente extends Utente{

    public Docente(){
        super();
    }

    public Docente(String user_name){
        super(user_name);
    }

    public Docente(String nome,String cognome,String username,String password){
        super(nome,cognome,username,password);
    }

}
