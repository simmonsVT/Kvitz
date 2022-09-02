package Database;

import Model.Docente;
import Model.Studente;
import Model.Utente;
import PostgresDAO.UtenteDB_DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Database.ConnessioneDatabase.*;

public class UtenteDB implements UtenteDB_DAO {



    /**
     * Registra un utente nella base di dati
     * @param c Oggetto connessione già connesso al db
     * @param u  Oggetto con i dati dell'utente da inserire
     * @return -1 Errore , 0 operazione avvenuta con successo
     */
    @Override
    public Integer registra_utente(Connection c,Utente u) {
        PreparedStatement insert;

        try {
            if (c!= null && !c.isClosed()) {
                if (u instanceof Studente){
                    insert = c.prepareStatement(
                            "INSERT INTO "+studenti_table+" VALUES ('" + u.getUsername() + "','" + u.getPassword() + "','" + u.getNome() + "','" + u.getCognome() + "')");
                }else if (u instanceof Docente){
                    insert = c.prepareStatement(
                            "INSERT INTO "+docenti_table+" VALUES ('" + u.getUsername() + "','" + u.getPassword() + "','" + u.getNome() + "','" + u.getCognome() + "')");
                }
                else
                    return -1;

                return insert.executeUpdate();
            }
        }
        catch (
                SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return -1;
    }

    /**
     * Ricerca un utente dalla base di dati
     * @param c Oggetto connessione già connesso al db
     * @param u Oggetto utente di cui ricavare le informazioni
     * @return Oggetto Utente se trovato, altrimenti null
     */
    @Override
    public Utente get_utente(Connection c,Utente u) {
        PreparedStatement insert;

        try {
            if (c != null && !c.isClosed() ){

                String query = "";
                if (u instanceof Studente)
                     query = "SELECT * from "+ studenti_table +" WHERE username LIKE '" + u.getUsername()+"'";
                else if (u instanceof Docente)
                     query = "SELECT * from "+ docenti_table +" WHERE username LIKE '" + u.getUsername()+"'";

                PreparedStatement query_obj = c.prepareStatement(query);

                ResultSet ris = query_obj.executeQuery();
                while (ris.next()) {

                    if (ris.getString("username").equals(u.getUsername())) {
                        Utente user = new Utente();
                        user.setNome(ris.getString("nome"));
                        user.setCognome(ris.getString("cognome"));
                        user.setUsername(ris.getString("username"));
                        user.setPassword(ris.getString("password"));
                        return user;
                    }
                }
                return null; //<-- utente non trovato
            }
        }
        catch (
                SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
