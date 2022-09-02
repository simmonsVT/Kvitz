package Database;

import Model.Docente;
import Model.Quiz;
import Model.Test;
import Model.Utente;
import PostgresDAO.DocenteDBDAO;
import PostgresDAO.TestDB_DAO;
import PostgresDAO.UtenteDB_DAO;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.*;


public class TestDB implements TestDB_DAO {





    public int salva_test(Connection c, Test test, Utente utente){

        PreparedStatement insert;

        try {
            if (test!= null && !c.isClosed()) {

                insert = c.prepareStatement(
                            "INSERT INTO "+test_table+" (nome,id_docente) VALUES ('" + test.getNome() + "','" + utente.getUsername() + "')");

                if (insert.executeUpdate() > 0){
                    int result;


                    for (Quiz quiz: test.getLista_quiz()) {

                        switch (quiz.getTipo()){

                            //Risposta multipla
                            case 0:

                                insert = c.prepareStatement(
                                        "INSERT INTO "+quiz_multiplo_table+" (domanda,nome_test,risposta_1,risposta_2,risposta_3,risposta_4,id_risposta_corretta,voto_risp_errata,voto_risp_esatta) VALUES" +
                                                " ('" + quiz.getDomanda() + "','" + test.getNome() + "','" +
                                                  quiz.get_risposta(0).getDescrizione() + "','" + quiz.get_risposta(1).getDescrizione()  +
                                                "','"+quiz.get_risposta(2).getDescrizione()  +  "','"+ quiz.get_risposta(3).getDescrizione()  +
                                                "','" + quiz.getId_risp_corretta() +
                                                "','"+ quiz.getVoto_minimo() + "','" + quiz.getVoto_max() +
                                                "')"
                                );

                                 result = insert.executeUpdate();

//                                return result > 0 ? result : -1;
                                break;

                            case 1:

                                insert = c.prepareStatement(
                                        "INSERT INTO "+quiz_aperto_table+" (nome_test,domanda,max_char_risp,voto_min,voto_max) VALUES" +
                                                " ('" + test.getNome() + "','" + quiz.getDomanda() + "','" +
                                                 quiz.getMaxChar() +  "','" +
                                                 quiz.getVoto_minimo() + "','" + quiz.getVoto_max() +"')"
                                );

                                result = insert.executeUpdate();

//                                return result > 0 ? result : -1;

                                break;
                        }
                    }
                    return 1;
                }
            }else
                return -1;
        }
        catch (
                SQLException e) {
            e.printStackTrace();
            return 0;
        }

        return -1;
    }




    /**
     * Ritorna lista di test creati da un docente SENZA quiz
     * @param c
     * @param docente_user
     * @return
     */



    public ArrayList<Test> get_lista_test(Connection c,String docente_user){

        PreparedStatement select,select_quiz;
        ArrayList arrayList = null;

        try {
            if (!c.isClosed()) {

                if (docente_user == null || docente_user.equals("")) {
                    select = c.prepareStatement(
                            "SELECT * FROM " + test_table);
                }
                else {
                    select = c.prepareStatement(
                            "SELECT nome,id_docente FROM " + test_table + " WHERE id_docente LIKE '" + docente_user + "'");
                }
                ResultSet result = select.executeQuery();
                 Test t = null;

                while (result.next()){
                    t = get_test(c,result.getString("nome"));

                    if (arrayList == null)
                        arrayList = new ArrayList();
                    arrayList.add(t);
                }
                return arrayList;
            }else
                return null;
        }
        catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }


    }


    /**
     * Ritorna il test associato al nome
     * @param c
     * @param nome
     * @return
     */
    public Test get_test(Connection c, String nome_test){

        QuizDB quizDB = new QuizDB();
        Test t = null;

        try{

            if (!c.isClosed()) {

                PreparedStatement select = c.prepareStatement(
                        "SELECT * FROM " + test_table + " WHERE nome = '" + nome_test+"'");

                ResultSet rs = select.executeQuery();

                DocenteDBDAO docenteDBDAO = new DocenteDB();
                while (rs.next()) {
                    if (t == null)
                        t = new Test();
                    t.setNome(rs.getString("nome"));
                    t.setDocente( docenteDBDAO.get_docente(c,rs.getString("id_docente")));
                    t.setLista_quiz(quizDB.prendiQuiz(c,nome_test));
                }

                return t;
            }
            else
                return null;


        }catch (Exception e){
            System.out.println(TestDB.class.getCanonicalName()+": "+e);
            return null;
        }

    }

    public int get_studenti_test_completato(Connection c, String nome_test) throws SQLException {

//        if (!c.isClosed()){
//            if (nome_test != null && !nome_test.equals("")){
//
//            }
//            else
//                return -1;
//        }
        return -1;

        //todo da finire
    }
















//    public Test get_test(Connection c,String nome){
//
//        PreparedStatement select,select_quiz;
//
//        try {
//            if (!c.isClosed()) {
//
//                select = c.prepareStatement(
//                            "SELECT * FROM " + test_table + " WHERE nome LIKE '" + nome + "'");
//
//                ResultSet result = select.executeQuery();
//                 Test t = null;
//                while (result.next()){
//                    t = new Test();
//                    t.setNome(result.getString("nome"));
//                    t.setId_docente(result.getString("id_docente"));
//
//                    select_quiz = c.prepareStatement(
//                            "SELECT * FROM " + quiz_aperto_table + " WHERE nome_test LIKE '" + t.getNome() + "'");
//
//                    ResultSet resultSetQuiz = select_quiz.executeQuery();
//
//                    while (resultSetQuiz.next()){
//                        Quiz q = new Quiz();
//                        q.setDomanda(resultSetQuiz.getString("domanda"));
//                        q.setTipo(1);
//                        q.setVoto_max(resultSetQuiz.getInt("voto_max"));
//                        q.setVoto_minimo(resultSetQuiz.getInt("voto_min"));
//                        q.setMaxChar(resultSetQuiz.getInt("max_char_risp"));
//                        if (t.getLista_quiz() == null)
//                            t.setLista_quiz( new ArrayList());
//                        t.getLista_quiz().add(q);
//                    }
//
//                    select_quiz = c.prepareStatement(
//                            "SELECT * FROM " + quiz_multiplo_table + " WHERE nome_test LIKE '" + t.getNome() + "'");
//
//                    resultSetQuiz = select_quiz.executeQuery();
//
//                    while (resultSetQuiz.next()){
//                        Quiz q = new Quiz();
//                        q.setDomanda(resultSetQuiz.getString("domanda"));
//                        q.setTipo(0);
//                        q.setVoto_max(resultSetQuiz.getInt("voto_risp_esatta"));
//                        q.setVoto_minimo(resultSetQuiz.getInt("voto_risp_errata"));
//                        q.setId_risp_corretta(resultSetQuiz.getInt("id_risposta_corretta"));
//
//                        Quiz.Risposta r = new Quiz.Risposta();
//                        for (int i = 1; i <= 4; i++){
//                            r.setDescrizione(resultSetQuiz.getString("risposta_"+i));
//                            q.getRisposte_arr().add(r);
//                        }
//                        if (t.getLista_quiz() == null)
//                            t.setLista_quiz( new ArrayList());
//                        t.getLista_quiz().add(q);
//                    }
//
//
//
//                    return t;
//                }
//
//                //todo da completare con i quiz associati al test!
//            }else
//                return null;
//        }
//        catch (
//                SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }











//Query che ricerca il nome del quiz cliccato


//public Test cercaTest(Connection c,String nTest) {
//    //String str;
//    Test t;
//    PreparedStatement select;
//
//    try {
//        select = c.prepareStatement(
//                "SELECT nome, id_docente FROM" + test_table + "WHERE nome =" + nTest);
//
//        ResultSet rs = select.executeQuery();
//
//        t = new Test();
//
//        //str = rs.getString("nome");
//        t.setNome(rs.getString("nome"));
//        t.setId_docente(rs.getString("id_docente"));
//
//
//        //QuizDB.prendiQuiz(c, nTest);
//
//        return t;
//
//    } catch (SQLException e) {
//
//        e.printStackTrace();
//        return null;
//
//    }
//}








}
