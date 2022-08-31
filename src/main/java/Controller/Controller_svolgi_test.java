package Controller;

import PostgresDAO.Test_svoltiDB_DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Model.*;
import Database.*;
import javafx.scene.layout.Pane;
import utils.TipoQuiz;
import utils.Utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import static utils.Utils.*;

public class Controller_svolgi_test {




    @FXML
    Label lbl_titolo,
          lbl_domanda,
          lbl_02;


@FXML
Button btnAvanti,
        btnEsci;



@FXML
    RadioButton opz1,
                opz2,
                opz3,
                opz4;

    @FXML
    Pane pane_risp_mult,
            pane_risp_ap;

    @FXML
    TextArea txtRispAperta;

    ToggleGroup btn_group_risp;

    //String nQuiz = "TestProva01";


    TestDB testDB = new TestDB();
    QuizDB quizBD = new QuizDB();

    Controller_elenco_test c;

    ArrayList<Quiz_svolto> quiz_completati;
    ArrayList<Integer> risposte_multiple_ordine;
    Quiz q;
    float punteggio;
    int indice_prossimo_quiz;

    @FXML
    private void initialize(){

        btnEsci.setVisible(false);
        btn_group_risp = new ToggleGroup();
        risposte_multiple_ordine = new ArrayList<>();

        opz1.setToggleGroup(btn_group_risp);
        opz2.setToggleGroup(btn_group_risp);
        opz3.setToggleGroup(btn_group_risp);
        opz4.setToggleGroup(btn_group_risp);

        punteggio = 0;
        quiz_completati = new ArrayList<>();
        test = testDB.get_test(Utils.connection, test.getNome());

        inizia_quiz();



    }

    private void inizia_quiz(){

        //Mostra i quiz in modo casuale
        Random random = new Random();
        indice_prossimo_quiz = random.nextInt(test.getLista_quiz().size());
        q = test.getLista_quiz().get(indice_prossimo_quiz);

        lbl_domanda.setText(q.getDomanda());

        if (q.getTipo() == TipoQuiz.RISPOSTA_MULTIPLA.get_tipo()) {

            ArrayList<Quiz.Risposta> risposte_app = (ArrayList<Quiz.Risposta>) q.getRisposte_arr().clone();

            ArrayList<RadioButton> radioButtons = new ArrayList<>();
            radioButtons.add(opz1);
            radioButtons.add(opz2);
            radioButtons.add(opz3);
            radioButtons.add(opz4);

            int radio_count = 0;
            //Mostra le risposte in modo casuale
            while (risposte_app.size() > 0){
                int index_risposta = random.nextInt(risposte_app.size()); // <-- indice risposta casuale da prelevare dalla lista
                Quiz.Risposta risposta_ap = risposte_app.get(index_risposta);
                risposte_multiple_ordine.add(q.getRisposte_arr().indexOf(risposta_ap));

                radioButtons.get(radio_count++).setText(risposta_ap.getDescrizione());
//                radioButtons.remove(radio_count++);

                risposte_app.remove(risposta_ap);
            }
            pane_risp_ap.setVisible(false);
            pane_risp_mult.setVisible(true);



        }
        else{
            setMaxCharRispApe(q.getMaxChar());
            pane_risp_mult.setVisible(false);
            pane_risp_ap.setVisible(true);
        }


    }

    private void setMaxCharRispApe(int maxChars){
        //Imposta il limite di caratteri inseribili
        if (maxChars != -1) {
            txtRispAperta.setTextFormatter(new TextFormatter<String>(change ->
                    change.getControlNewText().length() <= maxChars ? change : null));
        }
        //togli limite
        else
            txtRispAperta.setTextFormatter(new TextFormatter<String>(change -> change));
    }


    private void clear_all(){
        lbl_domanda.setText("");
        txtRispAperta.clear();
        opz1.setText("");
        opz2.setText("");
        opz3.setText("");
        opz4.setText("");
    }


    @FXML
    private void prossimo_quiz() throws IOException {

        if (quiz_completati == null){
            quiz_completati = new ArrayList<>();
        }

        Quiz_svolto quiz_svolto = new Quiz_svolto();
        Quiz.Risposta r = new Quiz.Risposta();
        if (q.getTipo() == TipoQuiz.RISPOSTA_MULTIPLA.get_tipo()) {
            RadioButton app_radio_btn = (RadioButton) btn_group_risp.getSelectedToggle();

            int index_risposta = Integer.parseInt(app_radio_btn.getId().substring(3)) - 1 ;
            r.setDescrizione(String.valueOf(risposte_multiple_ordine.get(index_risposta)));
        }
        else{
            r.setDescrizione(txtRispAperta.getText());
        }
        quiz_svolto.setRisposta_inserita(r);


        quiz_svolto.setId_quiz(q.getId_quiz());
        quiz_svolto.setTipo_quiz(q.getTipo());
        quiz_svolto.setStudente((Studente) Utils.utente);

        quiz_completati.add(quiz_svolto);

        test.getLista_quiz().remove(indice_prossimo_quiz);

        if (test.getLista_quiz().size() > 0) {
            clear_all();
            inizia_quiz();
        }
        else{

//            Quiz_svoltoDB quiz_svoltoDB = new Quiz_svoltoDB();
//
//            quiz_svoltoDB.salva_quiz(Utils.connection,quiz_completati);

            Test_svoltiDB_DAO test_svoltiDB_dao = new Test_svoltiDB();

            Test_svolto test_svolto = new Test_svolto(test.getNome(),(Studente) utente,quiz_completati,new Timestamp(System.currentTimeMillis()));
            test_svoltiDB_dao.salva_test(test_svolto,connection);

            new Alert(Alert.AlertType.INFORMATION,"Test completato con successo!\nSei stato reindirizzato alla tua homepage").show();
            Utils.change_scene(this.getClass(),"menu_studente");
        }

    }

    @FXML
    private void esci() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Vuoi veramente uscire?\nTutte le tue risposte verranno cancellate...");

        alert.show();
        Button btn_ok = (Button)  alert.getDialogPane().lookupButton(ButtonType.OK);
        btn_ok.setOnAction(event -> {
            try {
                change_scene(this.getClass(), "elenco_test");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }




}
