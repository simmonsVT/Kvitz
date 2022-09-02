package Controller;

import Database.UtenteDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import Model.*;
import utils.Utils;

import java.io.IOException;


public class Controller_registrazione {

    @FXML
    Button btnRegistrati,
            btnIndietro;

@FXML
TextField tfNome;
@FXML
TextField tfCognome;
@FXML
TextField tfUsername;
@FXML
TextField tfPassword;






    public void registrazione(MouseEvent actionEvent) throws IOException {

        if (Utils.utente != null && ( Utils.utente instanceof Studente || Utils.utente instanceof Docente)){

            if (check_campi()){

                Utils.utente.setNome(tfNome.getText());
                Utils.utente.setCognome(tfCognome.getText());
                Utils.utente.setUsername(tfUsername.getText());
                Utils.utente.setPassword(tfPassword.getText());

                int ris = new UtenteDB().registra_utente(Utils.connection,Utils.utente);
                if(ris != -1){
                  if (ris != 0) {
                      new Alert(Alert.AlertType.INFORMATION, "Utente registrato correttamente!").show();
                      if (Utils.utente instanceof Studente)
                          Utils.change_scene(this.getClass(), "menu_studente");
                      else
                          Utils.change_scene(this.getClass(), "menu_docente");

                  }
                  else
                      new Alert(Alert.AlertType.WARNING,"L'utente inserito già è registrato!").show();
                }
                else
                    new Alert(Alert.AlertType.ERROR,"Errore registrazione!").show();


            }else
                new Alert(Alert.AlertType.WARNING,"Ci sono dei campi vuoti!\nAssicurati di aver inserito tutte le informazioni richieste").show();



        }//Se si prova ad usare l'oggetto utente con un istanza di una classe che non sia Docente o Studente
        else{
            new Alert(Alert.AlertType.ERROR,"C'è stato un errore imprevisto!").show();
        }


    }





    public void Reset(ActionEvent actionEvent) {
        tfNome.setText("");
        tfCognome.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
    }






    /**
     * Verifica che siano stati popolati tutti i campi del form di registrazione
     * @return
     */
    private boolean check_campi(){
         return (!tfUsername.getText().equals("") &&
                 !tfPassword.getText().equals("") &&
                 !tfNome.getText().equals("") &&
                 !tfCognome.getText().equals(""));
    }






    @FXML
    private void indietro() throws IOException {

        Utils.change_scene(this.getClass(),"first_page");

    }













    @FXML
    public void continue_click(MouseEvent mouseEvent) {

    }
}
