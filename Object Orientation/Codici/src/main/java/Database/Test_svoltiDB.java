package Database;

import Model.*;
import PostgresDAO.Quiz_svoltoDB_DAO;
import PostgresDAO.StudenteDB_DAO;
import PostgresDAO.Test_svoltiDB_DAO;
import PostgresDAO.UtenteDB_DAO;
import utils.TipoQuiz;

import java.sql.*;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.*;

public class Test_svoltiDB implements Test_svoltiDB_DAO {

    public void salva_test(Test_svolto test, Connection c){
        try {
            if (!c.isClosed()){

                PreparedStatement insert = c.prepareStatement(
                        "INSERT INTO " + test_svolti_table + " (user_studente,nome_test,data_consegna,stato_correzione) " +
                                "VALUES ('" + test.getStudente().getUsername() + "','" + test.getNome_test() + "','" + test.getDataConsegna()+ "','" + Test_svolto_status.IN_ATTESA_CORREZIONE.getStatusId()+"')");

                if (insert.executeUpdate() > 0) {

                    int indice_test = get_test_id(c,test.getNome_test(),test.getStudente().getUsername());

                    if (indice_test > 0) {

                        Quiz_svoltoDB_DAO quiz_svoltoDB_dao = new Quiz_svoltoDB();
                        quiz_svoltoDB_dao.salva_quiz(c, test.getLista_quiz_svolti(),indice_test);
                    }
                    else
                        cancella_test(c, test.getNome_test(), test.getStudente().getUsername());
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int get_test_id(Connection c,String nome_test,String user_studente){
        try {
            if (!c.isClosed()){

                PreparedStatement select = c.prepareStatement(
                        "SELECT * FROM " + test_svolti_table + " WHERE user_studente LIKE '%"+user_studente+"%' AND nome_test LIKE '%"+nome_test+"%'");

                ResultSet rs = select.executeQuery();
                while(rs.next()){
                   return rs.getInt("id_test");
                }
            }
            else
                return -1;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return -1;
    }

    @Override
    public ArrayList<Test_svolto> get_lista_test_svolti_studente(Connection c, String user_studente) {
        ArrayList<Test_svolto> lista_test_svolti = null;
        try {
            if (!c.isClosed()){

                PreparedStatement select = c.prepareStatement(
                        "SELECT * FROM " + test_svolti_table + " WHERE user_studente LIKE '"+user_studente+"'");

                ResultSet rs = select.executeQuery();
                Test_svolto t = null;
                Quiz_svoltoDB_DAO quiz_svoltoDB_dao = new Quiz_svoltoDB();
                StudenteDB_DAO studenteDB_dao = new StudenteDB();
                Studente s = null;
                while(rs.next()){

                    t = new Test_svolto();
                    t.setNome_test(rs.getString("nome_test"));
                    t.setDataConsegna(rs.getTimestamp("data_consegna"));
                    t.setStato_correzione(rs.getInt("stato_correzione"));
                    t.setId_test(rs.getInt("id_test"));
                    t.setLista_quiz_svolti(quiz_svoltoDB_dao.prendiQuiz(c,t.getId_test()));

                    s = studenteDB_dao.get_studente(c,rs.getString("user_studente"));
                    t.setStudente(s);
                    if (lista_test_svolti == null){
                        lista_test_svolti = new ArrayList<>();
                    }

                    t.setPunteggio_finale(get_punteggio_totale_test(t.getLista_quiz_svolti()));
                    lista_test_svolti.add(t);

                }
                return lista_test_svolti;
            }
            else
                return null;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public int cancella_test(Connection c,String nome_test,String user_studente){
        try {
            if (!c.isClosed()){

                PreparedStatement delete = c.prepareStatement(
                        "DELETE FROM" + test_svolti_table + " WHERE user_studente LIKE '%"+user_studente+"%' AND nome_test LIKE '%"+nome_test+"%'");

                int risultato = delete.executeUpdate();
                return risultato;
            }
            else
                return -1;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Test_svolto>get_lista_test(Connection c,String user_studente){

        try {
            if (!c.isClosed()){
                ArrayList<Test_svolto> lista_test = new ArrayList<>();
                PreparedStatement select;
//                select = c.prepareStatement(
//                            "SELECT * FROM " + test_table + " LEFT JOIN test_svolti ON test.nome = test_svolti.nome_test" +
//                                    " WHERE test_svolti.user_studente LIKE '%" + user_studente + "%'");

                select = c.prepareStatement("SELECT * FROM test_svolti WHERE user_studente LIKE '"+user_studente+"'");
                ResultSet result = select.executeQuery();
                Test_svolto t = null;
                Quiz_svoltoDB_DAO quiz_svoltoDB_dao = new Quiz_svoltoDB();
                StudenteDB_DAO studenteDB_dao = new StudenteDB();
                while (result.next()){
                   t = new Test_svolto(result.getString("nome_test"), (Studente) studenteDB_dao.get_studente(c,result.getString("user_studente")),null,result.getTimestamp("data_consegna"));
                   t.setId_test(result.getInt("id_test"));
                   t.setLista_quiz_svolti(quiz_svoltoDB_dao.prendiQuiz(c,t.getId_test()));
                   t.setPunteggio_finale(get_punteggio_totale_test(t.getLista_quiz_svolti()));

                   lista_test.add(t);
                }
                return lista_test;
            }
            else
                return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }

    public Test_svolto get_test(Connection c,int id_test){

        try{

            if (!c.isClosed()){

                Test_svolto test = new Test_svolto();

                StudenteDB_DAO studenteDB_DAO = new StudenteDB();

                PreparedStatement select = c.prepareStatement("SELECT * FROM "+test_svolti_table+" WHERE id_test = "+id_test);

                ResultSet rs = select.executeQuery();

                while(rs.next()){
                    test.setNome_test(rs.getString("nome_test"));
                    test.setId_test(rs.getInt("id_test"));
                    test.setDataConsegna(rs.getTimestamp("data_consegna"));
                    test.setStudente(studenteDB_DAO.get_studente(c,rs.getString("user_studente")));
                    test.setStato_correzione(rs.getInt("stato_correzione"));
                    test.setLista_quiz_svolti(new Quiz_svoltoDB().prendiQuiz(c,id_test));
                    test.setPunteggio_finale(get_punteggio_totale_test(test.getLista_quiz_svolti()));

                }
                return test;

            }else return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Restituisce la lista dei test completati creati dall'user del docente passato come parametro
     */
    public ArrayList<Test_svolto> get_lista_test_docente(Connection c,String user_docente){

        ArrayList<Test_svolto> lista_test = null;
        try {
            if (!c.isClosed()){
                PreparedStatement select = null;
                select = c.prepareStatement("SELECT * FROM "+test_svolti_table+" WHERE nome_test IN (" +
                        "SELECT nome FROM "+test_table+" WHERE id_docente LIKE '%"+user_docente+"%')");
                ResultSet result = select.executeQuery();
                Test_svolto t = null;
                Quiz_svoltoDB_DAO quiz_svoltoDB_dao = new Quiz_svoltoDB();
                StudenteDB_DAO studenteDB_dao = new StudenteDB();
                while (result.next()){
                    if (lista_test == null)
                        lista_test = new ArrayList<>();

                    t = new Test_svolto(result.getString("nome_test"),(Studente) studenteDB_dao.get_studente(c,result.getString("user_studente")),null,result.getTimestamp("data_consegna"));
                    t.setId_test(result.getInt("id_test"));
                    t.setLista_quiz_svolti(quiz_svoltoDB_dao.prendiQuiz(c,t.getId_test()));
                    t.setStato_correzione(result.getInt("stato_correzione"));
                    t.setPunteggio_finale(get_punteggio_totale_test(t.getLista_quiz_svolti()));

                    lista_test.add(t);
                }
                return lista_test;

            }
            else
                return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    /**
     * Restituisce la lista delle consegne degli studenti associate al test passato come parametro
     * @param c
     * @param nome_test
     * @return
     */
    public ArrayList<Test_svolto> get_lista_studenti_test_svolto(Connection c,String nome_test){

        ArrayList<Test_svolto> lista_test = null;
        try {
            if (!c.isClosed()){
                
                PreparedStatement select = c.prepareStatement("SELECT * FROM "+test_svolti_table+" WHERE nome_test LIKE '%"+nome_test+"%'");
                ResultSet result = select.executeQuery();
                Test_svolto t = null;
                Quiz_svoltoDB_DAO quiz_svoltoDB_dao = new Quiz_svoltoDB();
                StudenteDB_DAO studenteDB_dao = new StudenteDB();
                while (result.next()){
                    if (lista_test == null)
                        lista_test = new ArrayList<>();

                    t = new Test_svolto(result.getString("nome_test"),(Studente) studenteDB_dao.get_studente(c,result.getString("user_studente")),null,result.getTimestamp("data_consegna"));
                    t.setId_test(result.getInt("id_test"));
                    t.setStato_correzione(result.getInt("stato_correzione"));
                    t.setLista_quiz_svolti(quiz_svoltoDB_dao.prendiQuiz(c,t.getId_test()));
                    t.setPunteggio_finale(get_punteggio_totale_test(t.getLista_quiz_svolti()));

                    lista_test.add(t);
                }
                return lista_test;
            }
            else return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    /**
     * Aggiorna lo stato del test in CORRETTO e aggiorna i voti assegnati dal docente ai quiz (SOLO per quiz APERTI)
     * @param c
     * @param test
     * @return
     */
    public boolean salva_correzione_test(Connection c,Test_svolto test){
        try {
            if (!c.isClosed()){
                PreparedStatement update = null;
                for (Quiz_svolto quiz:test.getLista_quiz_svolti()) {

                    if (quiz.getTipo_quiz() == TipoQuiz.RISPOSTA_APERTA.get_tipo()){
                        update = c.prepareStatement("UPDATE " + quiz_svolti_aperti_table +
                                " SET punteggio_assegnato = " + quiz.getPunteggio_assegnato() +
                                " WHERE id = "+ quiz.getId_quiz_svolto());
                        if (update.executeUpdate() < 0)
                            return false;
                    }
                }
                update = c.prepareStatement("UPDATE " + test_svolti_table +
                        " SET stato_correzione = " + Test_svolto_status.CORRETTO.getStatusId() +
                        " WHERE id_test = "+ test.getId_test());

                return update.executeUpdate() > 0;

            }else
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private float get_punteggio_totale_test(ArrayList<Quiz_svolto> lista_quiz_svolti){
        float punteggio_totale = 0;
        for (Quiz_svolto q: lista_quiz_svolti) {
            punteggio_totale += q.getPunteggio_assegnato();
        }
        return punteggio_totale;
    }






}
