package PostgresDAO;
import Model.Docente;

import java.sql.Connection;
import java.util.ArrayList;

public interface DocenteDBDAO {

    ArrayList<Docente> elenco_docenti(Connection connection);
    Docente get_docente(Connection c,String username);

}
