package Controller;

import Database.ConnessioneDatabase;
import Database.UtenteDB;
import Model.Docente;
import Model.Studente;
import Model.Utente;
import PostgresDAO.UtenteDB_DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.Utils;

import java.io.IOException;

public class Controller_login {


    @FXML
    PasswordField txt_utente_pass;

    @FXML
    TextField txt_utente_user;

    @FXML
    Button btn_login,
            btn_indietro;

    @FXML
    Label lbl_registrazione;


    @FXML
    public void login() throws IOException {

        Utils.utente.setUsername(txt_utente_user.getText());
        Utils.utente.setPassword(txt_utente_pass.getText());
        //todo da sistemare in DAO
        UtenteDB_DAO utenteDAO = new UtenteDB();
        Utente app  = utenteDAO.get_utente(ConnessioneDatabase.connect_db(), Utils.utente);
        if (Utils.utente instanceof Studente){

            if (app != null){
                Utils.utente = new Studente(app.getNome(),app.getCognome(),app.getUsername(),app.getPassword());
            }
            else{
                new Alert(Alert.AlertType.WARNING,"Utente non trovato!").show();
                return;
            }
            Utils.change_scene(this.getClass(),"menu_studente");

        }
        else if (Utils.utente instanceof Docente){
            if (app != null){
                Utils.utente =  new Docente(app.getNome(),app.getCognome(),app.getUsername(),app.getPassword());
            }
            else{
                new Alert(Alert.AlertType.WARNING,"Utente non trovato!").show();
                return;
            }
            //home page docente
            Utils.change_scene(this.getClass(),"menu_docente");
        }


    }

    @FXML
    public void registazione() throws IOException {
        Utils.change_scene(this.getClass(),"registrazione");
    }


    @FXML
    private void indietro() throws IOException {

        Utils.change_scene(this.getClass(),"first_page");

    }


}
