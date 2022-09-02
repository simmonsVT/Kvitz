package PostgresDAO;

import Model.Quiz_svolto;
import utils.TipoQuiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.*;

public interface Quiz_svoltoDB_DAO {

    public void salva_quiz(Connection c, ArrayList<Quiz_svolto> lista_quiz,int indice_test);
    ArrayList<Quiz_svolto> prendiQuiz(Connection c, int id_test);

}
