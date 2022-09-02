package PostgresDAO;

import Model.Studente;

import java.sql.Connection;
import java.util.ArrayList;

public interface StudenteDB_DAO {

    public ArrayList<Studente> elenco_studenti(Connection connection);

    Studente get_studente(Connection c, String username);
}
