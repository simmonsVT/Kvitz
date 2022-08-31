package PostgresDAO;

import Model.Quiz;
import utils.TipoQuiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.quiz_aperto_table;
import static Database.ConnessioneDatabase.quiz_multiplo_table;

public interface QuizDB_DAO {
    ArrayList<Quiz> prendiQuiz(Connection c, String nTest);
    Quiz prendi_info_quiz(Connection c,int id_quiz,int tipo_quiz);

}
