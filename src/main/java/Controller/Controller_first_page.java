package Controller;

import Database.ConnessioneDatabase;
import Model.Docente;
import Model.Studente;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import utils.Utils;
import java.io.IOException;

public class Controller_first_page {


    @FXML
    Button btn_docente,
           btn_studente;


    @FXML
    private void initialize(){

        Utils.utente = null;
        if (Utils.connection == null){
            Utils.connection = ConnessioneDatabase.connect_db();
        }
    }


    @FXML
    public void continue_click(MouseEvent mouseEvent) throws IOException {


        Button btn_cliccato = (Button) mouseEvent.getSource();
        System.out.println("PAGINA INIZIALE ---> cambio pagina!");

        switch (btn_cliccato.getId()){
            case "btn_docente":
                Utils.utente = new Docente();
                break;
            case "btn_studente":
                Utils.utente = new Studente();
                break;
        }
        Utils.change_scene(this.getClass(),"login");
    }



}
