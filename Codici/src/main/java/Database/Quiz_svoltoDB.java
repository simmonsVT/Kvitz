package Database;

import Model.Quiz;
import Model.Quiz_svolto;
import Model.Studente;
import Model.Utente;
import PostgresDAO.Quiz_svoltoDB_DAO;
import PostgresDAO.StudenteDB_DAO;
import PostgresDAO.UtenteDB_DAO;
import utils.TipoQuiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.*;

public class Quiz_svoltoDB implements Quiz_svoltoDB_DAO {


    public void salva_quiz(Connection c, ArrayList<Quiz_svolto> lista_quiz,int indice_test) {

        try {
            if (!c.isClosed()){
                PreparedStatement statement;
                for (Quiz_svolto q : lista_quiz){
                    if (q.getTipo_quiz() == TipoQuiz.RISPOSTA_APERTA.get_tipo()) {

                        String sql = String.format("INSERT INTO %s (id_quiz,user_studente,risposta_inserita,id_test) VALUES ('%d','%s','%s','%d')",
                                quiz_svolti_aperti_table,q.getId_quiz(),q.getStudente().getUsername(),q.getRisposta_inserita().getDescrizione(),indice_test);
                        statement = c.prepareStatement(sql);

                        statement.executeUpdate();


                    }
                    else{

                        //Mi prendo i dati inseriti dal docente riguardanti il quiz in oggetto
                        statement = c.prepareStatement("SELECT * FROM "+quiz_multiplo_table+" WHERE id ='"+q.getId_quiz()+"'");
                        ResultSet rs = statement.executeQuery();

                        int esito_insert;
                        while (rs.next()){
                            int id_risp_esatta = rs.getInt("id_risposta_corretta");
                            float voto_risp_esatta = rs.getFloat("voto_risp_esatta");
                            float voto_risp_errata = rs.getFloat("voto_risp_errata");
                            float punteggio_assegnato = 0;
                            punteggio_assegnato += id_risp_esatta == Integer.parseInt(q.getRisposta_inserita().getDescrizione()) ? voto_risp_esatta : voto_risp_errata;

                            String sql = String.format("INSERT INTO %s (id_quiz,user_studente,risposta_inserita,punteggio_assegnato,id_test) VALUES ('%d','%s','%s','%s','%d')",
                                    quiz_svolti_multipli_table,q.getId_quiz(),q.getStudente().getUsername(),q.getRisposta_inserita().getDescrizione(),punteggio_assegnato,indice_test);

                            esito_insert = c.prepareStatement(sql).executeUpdate();

                        }


                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //prende i quiz associati all'id del test scelto
    public ArrayList<Quiz_svolto> prendiQuiz(Connection c, int id_test){

        PreparedStatement select;
        ArrayList<Quiz_svolto> lista_quiz;
        try {
            select = c.prepareStatement(
                    "SELECT * FROM "+ quiz_svolti_aperti_table +" WHERE id_test = '"+id_test +"'");

            ResultSet rs = select.executeQuery();

            lista_quiz = new ArrayList<>();
            Quiz_svolto q;

            StudenteDB_DAO studenteDB_dao = new StudenteDB();
            while (rs.next()) {

                q = new Quiz_svolto();

                q.setId_quiz(rs.getInt("id_quiz"));

                Studente s = (Studente) studenteDB_dao.get_studente(c,rs.getString("user_studente"));

                q.setStudente(s);
                q.setTipo_quiz(TipoQuiz.RISPOSTA_APERTA.get_tipo());
                Quiz.Risposta r = new Quiz.Risposta();
                r.setDescrizione(rs.getString("risposta_inserita"));
                q.setRisposta_inserita(r);
                q.setPunteggio_assegnato(rs.getFloat("punteggio_assegnato"));
                q.setId_test(rs.getInt("id_test"));
                q.setId_quiz_svolto(rs.getInt("id"));
                lista_quiz.add(q);
            }
            select = c.prepareStatement(
                    "SELECT * FROM " + quiz_svolti_multipli_table + " WHERE id_test = '" + id_test +"'");


            rs = select.executeQuery();
            Quiz.Risposta r = null;
            while(rs.next()) {

                q = new Quiz_svolto();

                q.setId_quiz(rs.getInt("id_quiz"));
                Studente s = (Studente) studenteDB_dao.get_studente(c, rs.getString("user_studente"));
                q.setStudente(s);
                q.setTipo_quiz(TipoQuiz.RISPOSTA_MULTIPLA.get_tipo());
                r = new Quiz.Risposta();
                r.setDescrizione(rs.getString("risposta_inserita"));
                q.setRisposta_inserita(r);
                q.setPunteggio_assegnato(rs.getFloat("punteggio_assegnato"));
                q.setId_test(rs.getInt("id_test"));
                q.setId_quiz_svolto(rs.getInt("id"));
                lista_quiz.add(q);

            }
            return lista_quiz;

        }
        catch (SQLException e) {

            e.printStackTrace();
            return null;


        }


    }




}
