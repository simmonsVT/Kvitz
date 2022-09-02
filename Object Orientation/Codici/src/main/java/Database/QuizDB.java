package Database;

import Database.ConnessioneDatabase;
import Model.*;
import PostgresDAO.QuizDB_DAO;
import utils.TipoQuiz;

import java.sql.*;
import java.util.ArrayList;

import static utils.Utils.test;
import static Database.ConnessioneDatabase.*;
public class QuizDB implements QuizDB_DAO {

//prende i quiz associati al nome del test scelto
public ArrayList<Quiz> prendiQuiz(Connection c, String nTest){

    PreparedStatement select;
    ArrayList<Quiz> lista_quiz;
    try {
        select = c.prepareStatement(
                "SELECT * FROM "+ quiz_aperto_table +" WHERE nome_test = '"+nTest +"'");

        ResultSet rs = select.executeQuery();

        lista_quiz = new ArrayList<>();
        Quiz q;
        while (rs.next()) {

            q = new Quiz();

            q.setId_quiz(rs.getInt("id"));
            q.setDomanda(rs.getString("domanda"));
            q.setTipo(TipoQuiz.RISPOSTA_APERTA.get_tipo());
            q.setVoto_max(rs.getInt("voto_max"));
            q.setVoto_minimo(rs.getInt("voto_min"));
            q.setMaxChar(rs.getInt("max_char_risp"));
            q.setId_risp_corretta(-1);

            lista_quiz.add(q);
        }
        select = c.prepareStatement(
                    "SELECT * FROM " + quiz_multiplo_table + " WHERE nome_test = '" + nTest +"'");


        rs = select.executeQuery();
        Quiz.Risposta r;
        while(rs.next()) {

            q = new Quiz();

            q.setId_quiz(rs.getInt("id"));
            q.setDomanda(rs.getString("domanda"));
            q.setId_risp_corretta(rs.getInt("id_risposta_corretta"));
            q.setVoto_minimo(rs.getFloat("voto_risp_errata"));
            q.setVoto_max(rs.getFloat("voto_risp_esatta"));
            q.setTipo(TipoQuiz.RISPOSTA_MULTIPLA.get_tipo());
            for (int i = 1; i <= 4; i++) {
                r = new Quiz.Risposta();
                r.setDescrizione(rs.getString("risposta_" + i));
                q.getRisposte_arr().add(r);
            }
            lista_quiz.add(q);

        }
        return lista_quiz;

    }
    catch (SQLException e) {

        e.printStackTrace();
        return null;


    }


}

//prendi il quiz dal suo id
public Quiz prendi_info_quiz(Connection c,int id_quiz,int tipo_quiz){
        try {
            if (!c.isClosed()){
                Quiz q = null;
                String table = tipo_quiz == TipoQuiz.RISPOSTA_MULTIPLA.get_tipo() ? quiz_multiplo_table : quiz_aperto_table;
                PreparedStatement select = c.prepareStatement("SELECT * FROM "+table+" WHERE id = "+id_quiz);
                ResultSet rs = select.executeQuery();
                while (rs.next()){
                    q = new Quiz();
                    q.setDomanda(rs.getString("domanda"));
                    q.setTipo(tipo_quiz == TipoQuiz.RISPOSTA_MULTIPLA.get_tipo() ? 0 : 1);
                    if (q.getTipo() == TipoQuiz.RISPOSTA_APERTA.get_tipo()){
                        q.setVoto_minimo(rs.getFloat("voto_min"));
                        q.setVoto_max(rs.getFloat("voto_max"));
                        q.setMaxChar(rs.getInt("max_char_risp"));
                    }
                    else{
                        q.setId_risp_corretta(rs.getInt("id_risposta_corretta"));
                        q.setVoto_max(rs.getFloat("voto_risp_esatta"));
                        q.setVoto_minimo(rs.getFloat("voto_risp_errata"));
                        q.add_risposta(rs.getString("risposta_1"));
                        q.add_risposta(rs.getString("risposta_2"));
                        q.add_risposta(rs.getString("risposta_3"));
                        q.add_risposta(rs.getString("risposta_4"));
                    }
                    q.setId_quiz(rs.getInt("id"));
                }
                return q;
            }
            else
                return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

}
