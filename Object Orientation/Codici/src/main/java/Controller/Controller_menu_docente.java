package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import jdk.jshell.execution.Util;
import utils.Utils;

import java.io.IOException;

public class Controller_menu_docente {

    @FXML
    Button btn_inseriscitest,
            btn_elencotest,
            btn_valutatest,
            btn_elencostudenti,
            btn_logout;

    @FXML
    Label lbl_nome;

    @FXML
    private void initialize(){

        lbl_nome.setText(Utils.utente.getNome()+" "+Utils.utente.getCognome());
        Utils.test = null;
    }

    @FXML
    public void logout() throws IOException {

        Utils.change_scene(this.getClass(),"first_page");

    }

    @FXML
    public void inserisci_test(MouseEvent event) throws IOException {
        Utils.change_scene(this.getClass(),"inserisci_domanda");
    }


    public void elenco_test(MouseEvent event) throws IOException {
        Utils.change_scene(this.getClass(),"elenco_test");
    }
}
