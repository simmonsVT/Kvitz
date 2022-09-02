package Controller;

import Database.TestDB;
import Model.*;
import PostgresDAO.TestDB_DAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static utils.Utils.*;

public class Controller_inserisci_domanda {


    @FXML
    Button btn_indietro,
            btn_reset,
           btn_termina_ins,
            btn_aggiungi_dom;

    @FXML
    TextArea txt_domanda;

    @FXML
    Label lbl_num_domanda;

    @FXML
    TextField txt_risp_1,
              txt_risp_2,
              txt_risp_3,
              txt_risp_4,
                txt_voto_min,
                txt_voto_max,
                txt_num_car;

    @FXML
    RadioButton rbtn_risp_mult,
                rbtn_risp_ape,
                rbtn_risp_corr_1,
                rbtn_risp_corr_2,
                rbtn_risp_corr_3,
                rbtn_risp_corr_4;


    @FXML
    Pane pnl_risposta_aperta,
            pnl_risposta_multipla;


    ToggleGroup btn_group_risp_mode,btn_group_risp_corr;


    Quiz quiz;




    // 0 - risposta multipla
    // 1 - risposta aperta
    int tipo_risposta = 0;
    boolean termina_ins = false;
    TestDB_DAO testDB_dao;




    @FXML
    private void initialize(){

        btn_group_risp_mode = new ToggleGroup();
        btn_group_risp_corr = new ToggleGroup();
        rbtn_risp_ape.setToggleGroup(btn_group_risp_mode);
        rbtn_risp_mult.setToggleGroup(btn_group_risp_mode);

        rbtn_risp_corr_1.setToggleGroup(btn_group_risp_corr);
        rbtn_risp_corr_2.setToggleGroup(btn_group_risp_corr);
        rbtn_risp_corr_3.setToggleGroup(btn_group_risp_corr);
        rbtn_risp_corr_4.setToggleGroup(btn_group_risp_corr);


        if (test == null){
            test = new Test();
            test.setLista_quiz(new ArrayList<>());
        }

        testDB_dao = new TestDB();
        quiz = new Quiz();

        aggiorna_label_num_quiz();
    }



    @FXML
    public boolean inserisci_quiz(MouseEvent event) throws IOException {

        if (test == null)
            test = new Test();

        if (check_domanda()) {
            quiz.setDomanda(txt_domanda.getText());

            try {
                quiz.setTipo(tipo_risposta);
//            risp.setDescrizione(txt);
                quiz.setVoto_minimo(Float.parseFloat(txt_voto_min.getText().replace(",", ".")));
                quiz.setVoto_max(Float.parseFloat(txt_voto_max.getText().replace(",", ".")));

                //Scambia i voti se il voto minimo è maggiore del massimo
                if (quiz.getVoto_minimo() > quiz.getVoto_max()){
                    float app = quiz.getVoto_max();
                    quiz.setVoto_max(quiz.getVoto_minimo());
                    quiz.setVoto_minimo(app);
                }

            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Controlla che i valori numerici inseriti siano corretti!").show();
                return false;
            }
            if (tipo_risposta == 0) {
                if (check_risposte()) {
                    RadioButton app_radio_btn = (RadioButton) btn_group_risp_corr.getSelectedToggle();
                    quiz.setId_risp_corretta(Integer.parseInt(app_radio_btn.getId().substring(15)) - 1);
                    quiz.add_risposta(txt_risp_1.getText());
                    quiz.add_risposta(txt_risp_2.getText());
                    quiz.add_risposta(txt_risp_3.getText());
                    quiz.add_risposta(txt_risp_4.getText());


                } else {
                    new Alert(Alert.AlertType.WARNING, "Bisogna inserire almeno 2 risposte!").show();
                    return false;
                }
            } else {
                try {
                    if (check_risp_ap())
                        quiz.setMaxChar(Integer.parseInt(txt_num_car.getText()));
                    else {
                        new Alert(Alert.AlertType.WARNING, "Il numero massimo di caratteri deve essere un intero!").show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Controlla che i valori numerici inseriti siano corretti!").show();
                    return false;
                }
            }
            test.getLista_quiz().add(quiz);
            if (!termina_ins) {
                clear_all();
                aggiorna_label_num_quiz();
            }
            quiz = new Quiz();
            return true;
        }

        new Alert(Alert.AlertType.WARNING,"La domanda non può essere vuota!").show();
        return false;
    }



    public void termina_inserimento_quiz(MouseEvent e) throws IOException {

        TextInputDialog textInputDialog = new TextInputDialog();

        textInputDialog.setHeaderText("Inserisci il nome del test");
        textInputDialog.setContentText("Nome:");

        final String[] nome_test = {""};

        Button btn_ok = (Button)  textInputDialog.getDialogPane().lookupButton(ButtonType.OK);
        btn_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nome_test[0] = textInputDialog.getEditor().getText();

                if ( !nome_test[0].equals("") && testDB_dao.get_test(connection,nome_test[0]) == null ){
                    termina_ins = true;

                    try {
                        if (inserisci_quiz(e)){
                            test.setNome(nome_test[0]);
                            TestDB testDB = new TestDB();
                            int res = testDB.salva_test(Utils.connection, test, Utils.utente);
                            test = null;

                            if (res > 0) {
                                new Alert(Alert.AlertType.INFORMATION,"Test salvato correttamente!").show();
                                Utils.change_scene(this.getClass(), "menu_docente");
                            }
                            else{
                                if (res == 0){
                                    new Alert(Alert.AlertType.ERROR,"Esiste già un test con il nome inserito!\nSi prega di riprovare a salvare con un nome diverso").show();
                                }
                                else{
                                    new Alert(Alert.AlertType.ERROR,"Impossibile salvare il test!").show();
                                    Utils.change_scene(this.getClass(), "menu_docente");
                                }
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else{
                    if (nome_test[0].equals(""))
                        new Alert(Alert.AlertType.WARNING,"Il nome non può essere una stringa vuota!").show();
                    else
                        new Alert(Alert.AlertType.WARNING,"Esiste già un test con lo stesso nome!").show();
                }
            }

        });

        textInputDialog.show();
    }



    @FXML
    private void change_pane(MouseEvent mouseEvent){
        RadioButton r = (RadioButton) mouseEvent.getSource();

        switch (r.getId()){
            case "rbtn_risp_mult":

                pnl_risposta_aperta.setVisible(false);

                tipo_risposta = 0;

                pnl_risposta_multipla.setVisible(true);

                System.out.println("Tipo risposta --> MULTIPLA");
                break;

            case "rbtn_risp_ape":

                pnl_risposta_aperta.setVisible(true);

                tipo_risposta = 1;

                pnl_risposta_multipla.setVisible(false);

                System.out.println("Tipo risposta --> APERTA");

                break;
        }


    }



    boolean check_risposte(){
        return (!txt_risp_1.getText().equals("") && !txt_risp_2.getText().equals(""));
    }




    boolean check_risp_ap(){
        try {

            int app  = Integer.parseInt(txt_num_car.getText().replace(",","."));
            return true;

        }catch (NumberFormatException e){
            return false;
        }
    }




    boolean check_domanda(){
        return !txt_domanda.getText().equals("");
    }



    private void aggiorna_label_num_quiz(){

        lbl_num_domanda.setText("Domanda n° "+ (test.getLista_quiz().size() + 1));
    }



    @FXML
    private void indietro() throws IOException {
        change_scene(this.getClass(),"menu_docente");
    }






    private void clear_all(){
        txt_domanda.clear();
        txt_risp_1.clear();
        txt_risp_2.clear();
        txt_risp_3.clear();
        txt_risp_4.clear();
        txt_num_car.clear();
        txt_voto_min.clear();
        txt_voto_max.clear();
        txt_num_car.clear();
    }
}
