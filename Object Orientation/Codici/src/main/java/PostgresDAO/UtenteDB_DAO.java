package PostgresDAO;

import Model.Utente;

import java.sql.Connection;

public interface UtenteDB_DAO {
    public Integer registra_utente(Connection c,Utente u);
    public Utente get_utente(Connection c,Utente u);
}
