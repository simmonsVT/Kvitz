package PostgresDAO;

import Database.Quiz_svoltoDB;
import Model.Test_svolto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.test_table;

public interface Test_svoltiDB_DAO {

     void salva_test(Test_svolto test, Connection c);

     int get_test_id(Connection c, String nome_test, String user_studente);

     ArrayList<Test_svolto> get_lista_test_svolti_studente(Connection c,String user_studente);

     ArrayList<Test_svolto> get_lista_test(Connection c, String user_studente);

     ArrayList<Test_svolto> get_lista_test_docente(Connection c, String user_docente);

     public ArrayList<Test_svolto> get_lista_studenti_test_svolto(Connection c, String nome_test);

     Test_svolto get_test(Connection c, int id_test);

     boolean salva_correzione_test(Connection c, Test_svolto test);
}


