package Controller;

import Database.TestDB;
import Database.Test_svoltiDB;
import Database.UtenteDB;
import Model.Docente;
import Model.Studente;
import Model.Test;
import Model.Test_svolto;
import PostgresDAO.Test_svoltiDB_DAO;
import PostgresDAO.UtenteDB_DAO;
import PostgresDAO.TestDB_DAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static utils.Utils.*;


public class Controller_elenco_test {




    // 0 - nome docente
    // 1 - nome test
    ArrayList<TableRow<Object>> righe_tabella;

    @FXML
    TableView table;

    @FXML
    Label lbl_messaggio;

    @FXML
    Button btn_indietro;

    ArrayList<Test> arrayList_test = null;
    ArrayList<Test_svolto> arrayList_test_svolto = null;
    UtenteDB_DAO utenteDB = null;
    TestDB_DAO testDB = null;
    Test_svoltiDB_DAO test_svoltiDB_dao = null;


    @FXML
    private void initialize() {

        if (utenteDB == null)
            utenteDB = new UtenteDB();

        if (testDB == null)
            testDB = new TestDB();

        if (test_svolto == null)
            test_svolto = new Test_svolto();

        if (test_svoltiDB_dao == null)
            test_svoltiDB_dao = new Test_svoltiDB();

        if (arrayList_test == null)
            arrayList_test = new ArrayList<Test>();

        if (arrayList_test_svolto == null)
            arrayList_test_svolto = new ArrayList<>();

        if (Utils.utente instanceof Docente) {

            lbl_messaggio.setText("Fai doppio click sul test per interagire");

            arrayList_test = testDB.get_lista_test(Utils.connection,utente.getUsername());

            TableColumn<Test,Docente> colonna_nome = new TableColumn<>("Nome");
            colonna_nome.setCellValueFactory(new PropertyValueFactory<>("docente"));
            colonna_nome.setCellFactory(column -> new TableCell<Test, Docente>() {
                @Override
                protected void updateItem(Docente docente, boolean empty) {
                    super.updateItem(docente, empty);
                    if (empty || docente == null)
                        setText("");
                    else {
                            setText(docente.getNome() + " "+docente.getCognome());
                        }
                    }
                });
            colonna_nome.setPrefWidth(100);

            TableColumn<Test,String> colonna_nomeTest = new TableColumn<>("Nome test");
            colonna_nomeTest.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colonna_nomeTest.setPrefWidth(300);

            if (arrayList_test != null) {
                table.setItems(FXCollections.observableArrayList(arrayList_test));
                table.getColumns().addAll(colonna_nome,colonna_nomeTest);
            }

//            listv_lista_test.getItems().addAll(arrayList_test);
        }
        else if (Utils.utente instanceof Studente){

            lbl_messaggio.setText("Fai doppio click sul test per iniziarlo");

            arrayList_test = testDB.get_lista_test(Utils.connection,null);

            arrayList_test_svolto = test_svoltiDB_dao.get_lista_test(connection,utente.getUsername());

            TableColumn<Test,Docente> colonna_nome = new TableColumn<>("Nome docente");
            colonna_nome.setCellValueFactory(new PropertyValueFactory<>("docente"));
            colonna_nome.setPrefWidth(200);

            colonna_nome.setCellFactory(column -> new TableCell<Test, Docente>() {
                @Override
                protected void updateItem(Docente docente, boolean empty) {
                    super.updateItem(docente, empty);
                    if (empty || docente == null)
                        setText("");
                    else {
                        setText(docente.getNome() + " "+docente.getCognome());
                    }
                }
            });

            TableColumn<Test,String> colonna_nomeTest = new TableColumn<>("Nome test");
            colonna_nomeTest.setCellValueFactory(new PropertyValueFactory<>("nome"));


            colonna_nomeTest.setPrefWidth(300);



            if (arrayList_test != null && arrayList_test_svolto != null) {
                table.setItems(FXCollections.observableArrayList(arrayList_test));
                table.getColumns().addAll(colonna_nome,colonna_nomeTest);
            }
        }

    }






    //funzione che prende il nome del test scelto
    public String prendiNomeT(){


     Test test = (Test) table.getSelectionModel().getSelectedItem();


    String nome= test.getNome();
    String doc= test.getDocente().getUsername();

    System.out.println("Nome test ---> "+ nome);

    //return selected;
    return nome;
}










    @FXML
    private void vai_menu() throws IOException {
        if (utente instanceof Docente)
            change_scene(this.getClass(), "menu_docente");
        else
            change_scene(this.getClass(), "menu_studente");
    }








    public void click_test(MouseEvent event) throws IOException {
        inizia_test(event);
    }








    public void inizia_test(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2){

            if (utente instanceof Studente) {

                Test_svoltiDB_DAO test_svoltiDB_dao = new Test_svoltiDB();

                Test_svolto test_svolto = ricerca_test_svolto(prendiNomeT());
               if (test_svolto == null) {

                   Alert alert = new Alert(AlertType.CONFIRMATION);
                   alert.setTitle("Test: " + prendiNomeT());
                   alert.setContentText("Sei sicuro di voler procedere con il test?\nNon sarà possibile ripeterlo!");
                   //caso in cui l'utente clicca OK
                   Optional<ButtonType> result = alert.showAndWait();
                   if (result.get() == ButtonType.OK) {
                       if (Utils.test == null)
                           test = new Test();
                       test.setNome(prendiNomeT());
                       Utils.change_scene(this.getClass(), "svolgi_test");
                   }
               }
               else{
                   new Alert(AlertType.WARNING,"Hai già svolto questo test!").show();
               }
            }
            else{
                test_svolto.setNome_test(prendiNomeT());
                Utils.change_scene(this.getClass(),"elenco_test_correzione");
            }
        }
    }

    private Test_svolto ricerca_test_svolto(String nome_test){
        for (Test_svolto t: arrayList_test_svolto) {
            if (t.getNome_test().equals(nome_test)){
                return t;
            }
        }
        return null;
    }
}
