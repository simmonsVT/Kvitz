package PostgresDAO;

import Model.Test;
import Model.Utente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TestDB_DAO {
    int salva_test(Connection c, Test test, Utente utente);
    ArrayList<Test> get_lista_test(Connection c, String docente_user);
    Test get_test(Connection c,String nome_test);
    int get_studenti_test_completato(Connection c, String nome_test) throws SQLException;
}

