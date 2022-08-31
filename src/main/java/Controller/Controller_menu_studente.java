package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.Utils;

import java.io.IOException;

public class Controller_menu_studente {

    @FXML
    Button btn_elenco_test,
            btn_consulta_punteggi,
            btn_logout;

    @FXML
    Button btn_svolgi_test;


    @FXML
    Label lbl_nome;

    @FXML
    private void initialize() {
        lbl_nome.setText(Utils.utente.getNome() + " " + Utils.utente.getCognome());
    }

    @FXML
    public void logout() throws IOException {

        Utils.change_scene(this.getClass(), "first_page");

    }


    @FXML
    public void svolgiTest() throws IOException {

        Utils.change_scene(this.getClass(), "elenco_test");

    }


    @FXML
    public void consultaPunteggi() throws IOException {

        Utils.change_scene(this.getClass(), "consulta_punteggi");

    }








}