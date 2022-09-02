package Controller;

import Database.QuizDB;
import Database.Test_svoltiDB;
import Database.UtenteDB;
import Model.*;
import PostgresDAO.QuizDB_DAO;
import PostgresDAO.Test_svoltiDB_DAO;
import PostgresDAO.UtenteDB_DAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import utils.TipoQuiz;
import utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static utils.Utils.*;

public class Controller_visualizza_correggi_test {




    @FXML
    Label lbl_titolo,
          lbl_domanda,
          lbl_02,
          lbl_studente,
          lbl_voto_min,
          lbl_voto_max,
          lbl_voto,
          lbl_data_consegna;

@FXML
Button tbnEsci,
        btnAvanti,
        btnIndietro;



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

    @FXML
    TextField txt_voto;
    ToggleGroup btn_group_risp;



    ArrayList<Quiz_svolto> quiz_corretti;
    Quiz_svolto q;
    Quiz quiz;
    float voto;
    int indice_prossimo_quiz;
    QuizDB_DAO quizDBDao;
    UtenteDB_DAO utenteDBDao;
    Test_svoltiDB_DAO testSvoltiDBDao;


    @FXML
    private void initialize(){
        testSvoltiDBDao = new Test_svoltiDB();
        btn_group_risp = new ToggleGroup();

        opz1.setToggleGroup(btn_group_risp);
        opz2.setToggleGroup(btn_group_risp);
        opz3.setToggleGroup(btn_group_risp);
        opz4.setToggleGroup(btn_group_risp);

        voto = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        lbl_data_consegna.setText("Consegnato il: "+dateFormat.format(test_svolto.getDataConsegna()));
        if (!correzione_quiz) {
            btnIndietro.setVisible(true);
            lbl_titolo.setText("Visualizzazione Test");
            //Mi ricavo il punteggio totale in caso di visualizzazione
            for (Quiz_svolto quiz_svolto : test_svolto.getLista_quiz_svolti()) {
                voto += quiz_svolto.getPunteggio_assegnato();
            }
            lbl_voto.setText("Voto finale: "+String.valueOf(voto));

        }
        else
            lbl_voto.setText("Voto: "+String.valueOf(voto));
        quiz_corretti = new ArrayList<>();

        indice_prossimo_quiz = 0;
        quizDBDao = new QuizDB();
        utenteDBDao = new UtenteDB();

        Utente u = test_svolto.getStudente();
        u = utenteDBDao.get_utente(connection,u);

        lbl_studente.setText("Studente: "+u.getNome() + " "+u.getCognome() + "["+u.getUsername()+"]");
        inizia_quiz();



    }

    private void inizia_quiz(){

        q = test_svolto.getLista_quiz_svolti().get(indice_prossimo_quiz);
        quiz = quizDBDao.prendi_info_quiz(connection,q.getId_quiz(),q.getTipo_quiz());
        lbl_domanda.setText(quiz.getDomanda());

        lbl_voto_min.setText("Voto minimo: "+String.valueOf(quiz.getVoto_minimo()));
        lbl_voto_max.setText("Voto massimo: "+String.valueOf(quiz.getVoto_max()));
        txt_voto.clear();
        if (q.getTipo_quiz() == TipoQuiz.RISPOSTA_MULTIPLA.get_tipo()) {

//            voto += q.getPunteggio_assegnato();
//                    lbl_voto.setText("Voto: "+voto);

            ArrayList<Quiz.Risposta> risposte_app = quiz.getRisposte_arr();

            pane_risp_ap.setVisible(false);
            pane_risp_mult.setVisible(true);

            txt_voto.setDisable(true);

            opz1.setText(risposte_app.get(0).getDescrizione());
            opz2.setText(risposte_app.get(1).getDescrizione());
            opz3.setText(risposte_app.get(2).getDescrizione());
            opz4.setText(risposte_app.get(3).getDescrizione());

            switch (q.getRisposta_inserita().getDescrizione()){
                case "0":
                    opz1.setSelected(true);
                break;
                case "1":
                    opz2.setSelected(true);

                    break;
                case "2":
                    opz3.setSelected(true);

                    break;
                case "3":
                    opz4.setSelected(true);
                    break;
            }

            if (q.getRisposta_inserita().getDescrizione().equals(String.valueOf(quiz.getId_risp_corretta()))) {
                lbl_02.setText("Risposta esatta!");
                lbl_02.setTextFill(Color.GREEN);
            }
            else {
                lbl_02.setText("Risposta errata!");
                lbl_02.setTextFill(Color.RED);
            }
            voto += q.getPunteggio_assegnato();
            txt_voto.setText(String.valueOf(q.getPunteggio_assegnato()));
        }
        else{
            txtRispAperta.setText(q.getRisposta_inserita().getDescrizione());
            pane_risp_mult.setVisible(false);
            pane_risp_ap.setVisible(true);
            txt_voto.setDisable(!correzione_quiz);
            if (!correzione_quiz)
                txt_voto.setText(String.valueOf(q.getPunteggio_assegnato()));
        }



    }




    private void clear_all(){
        lbl_domanda.setText("");
        txtRispAperta.clear();
        opz1.setText("");
        opz2.setText("");
        opz3.setText("");
        opz4.setText("");
        txt_voto.clear();
    }


    @FXML
    private void prossimo_quiz() throws IOException {
        if (correzione_quiz)
            prossimo_quiz_correzione();
        else
            prossimo_quiz_visualizzazione();
    }




    @FXML
    private void esci() throws IOException {

        if (correzione_quiz) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi veramente uscire?\nVerranno persi tutte le correzioni di questo test...");

            alert.show();
            Button btn_ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            btn_ok.setOnAction(event -> {
                try {
                    if (utente instanceof Docente)
                        change_scene(this.getClass(), "elenco_test");
                    else
                        change_scene(this.getClass(), "consulta_punteggi");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
        else
            if (utente instanceof Docente)
                change_scene(this.getClass(), "elenco_test");
            else
                change_scene(this.getClass(), "consulta_punteggi");
    }

    @FXML
    private void precedente_quiz() throws IOException {
        indice_prossimo_quiz = indice_prossimo_quiz > 0 ? --indice_prossimo_quiz : indice_prossimo_quiz;
        inizia_quiz();
    }

    private void prossimo_quiz_correzione() throws IOException {
        if (!txt_voto.getText().equals("")) {


            float voto_inserito = Float.parseFloat(txt_voto.getText().replace(",", "."));
            if (voto_inserito >= quiz.getVoto_minimo() && voto_inserito <= quiz.getVoto_max()) {

                // In caso di risposta aperta il punteggio viene assegnato dal docente, quindi, nella gui, va aggiornato dopo la correzione
                if (q.getTipo_quiz() == TipoQuiz.RISPOSTA_APERTA.get_tipo())
                    voto += voto_inserito;

                lbl_voto.setText("Voto: " + voto);

                Quiz_svolto quiz_svolto = new Quiz_svolto();
                quiz_svolto.setPunteggio_assegnato(Float.parseFloat(txt_voto.getText().replace(",", ".")));

                quiz_svolto.setId_quiz(q.getId_quiz());
                quiz_svolto.setId_quiz_svolto(q.getId_quiz_svolto());

                quiz_svolto.setStudente(q.getStudente());
                quiz_svolto.setTipo_quiz(q.getTipo_quiz());
                quiz_corretti.add(quiz_svolto);

                indice_prossimo_quiz++;

                //nel caso ci siano altri quiz da correggere
                if (quiz_corretti.size() < test_svolto.getLista_quiz_svolti().size()) {
                    clear_all();
                    inizia_quiz();
                }
                //correzione finita
                else {

                    System.out.println("Correzione terminata!\nVoto finale: "+voto);

                    Test_svolto test_svolto_corretto = new Test_svolto();

                    test_svolto_corretto.setId_test(test_svolto.getId_test());
                    test_svolto_corretto.setNome_test(test_svolto.getNome_test());
                    test_svolto_corretto.setStudente(test_svolto.getStudente());
                    test_svolto_corretto.setDataConsegna(test_svolto.getDataConsegna());
                    test_svolto_corretto.setLista_quiz_svolti(quiz_corretti);
                    test_svolto_corretto.setStato_correzione(Test_svolto_status.CORRETTO.getStatusId());
                    boolean check_correzione = testSvoltiDBDao.salva_correzione_test(connection, test_svolto_corretto);

                    if (check_correzione) {
                        new Alert(Alert.AlertType.INFORMATION, "Correzione salvata correttamente!\nVoto finale: "+voto).show();
                        Utils.change_scene(this.getClass(), "elenco_test");
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Impossibile salvare la correzione!").show();
                    }

                }
            }
            else
                new Alert(Alert.AlertType.WARNING, "Il voto inserito non rispetta il range stabilito in fase di creazione del quiz!").show();
        }
        else
            new Alert(Alert.AlertType.WARNING, "Inserire un voto per il quiz mostrato!").show();
    }

    private void prossimo_quiz_visualizzazione() {
        indice_prossimo_quiz++;

        //nel caso ci siano altri quiz da correggere
        if (indice_prossimo_quiz < test_svolto.getLista_quiz_svolti().size()) {
            clear_all();
            inizia_quiz();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"I quiz sono terminati\nVuoi tornare all'elenco dei test?");
            Button btn_ok = (Button)  alert.getDialogPane().lookupButton(ButtonType.OK);

            btn_ok.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (utente instanceof Docente)
                            change_scene(this.getClass(), "elenco_test");
                        else
                            change_scene(this.getClass(), "consulta_punteggi");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            alert.show();
        }
    }

}
