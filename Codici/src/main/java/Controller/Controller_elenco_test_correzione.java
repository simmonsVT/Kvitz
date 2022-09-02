package Controller;

import Database.TestDB;
import Database.Test_svoltiDB;
import Database.UtenteDB;
import Model.Docente;
import Model.Studente;
import Model.Test;
import Model.Test_svolto;
import Model.Test_svolto_status;
import PostgresDAO.TestDB_DAO;
import PostgresDAO.Test_svoltiDB_DAO;
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


public class Controller_elenco_test_correzione {




    // 0 - nome docente
    // 1 - nome test
    ArrayList<TableRow<Object>> righe_tabella;

    @FXML
    TableView table;

    @FXML
    Label lbl_messaggio;
    @FXML
    Label lbl_nome_test;

    @FXML
    Button btn_indietro;

    ArrayList<Test_svolto> arrayList_test_svolto = null;
    TestDB_DAO testDB = null;
    Test_svoltiDB_DAO test_svoltiDB_dao = null;


    @FXML
    private void initialize() {

        if (testDB == null)
            testDB = new TestDB();

        if (test_svoltiDB_dao == null)
            test_svoltiDB_dao = new Test_svoltiDB();


        if (arrayList_test_svolto == null)
            arrayList_test_svolto = new ArrayList<>();

        if (Utils.utente instanceof Docente) {

            lbl_messaggio.setText("Fai doppio click sul test per iniziare la correzione");
            lbl_nome_test.setText("Test scelto: "+test_svolto.getNome_test());

            arrayList_test_svolto = test_svoltiDB_dao.get_lista_studenti_test_svolto(connection,test_svolto.getNome_test());

            TableColumn<Test,String> colonna_id_test = new TableColumn<>("ID");
            colonna_id_test.setCellValueFactory(new PropertyValueFactory<>("id_test"));
            colonna_id_test.setPrefWidth(50);


            TableColumn<Test,Studente> colonna_nome = new TableColumn<>("Nome studente");
            colonna_nome.setCellValueFactory(new PropertyValueFactory<>("studente"));

            colonna_nome.setCellFactory(column -> new TableCell<Test, Studente>() {
                @Override
                protected void updateItem(Studente studente, boolean empty) {
                    super.updateItem(studente, empty);
                    if (empty || studente == null)
                        setText("");
                    else {
                        setText(studente.getNome() + " "+studente.getCognome());
                    }
                }
            });
            colonna_nome.setPrefWidth(200);

            TableColumn<Test,String> colonna_dataConsegna = new TableColumn<>("Data consegna");
            colonna_dataConsegna.setCellValueFactory(new PropertyValueFactory<>("dataConsegna"));
            colonna_dataConsegna.setPrefWidth(200);
            TableColumn<Test,String> colonna_corretto = new TableColumn<>("Corretto (0 no/1 si)");
            colonna_corretto.setCellValueFactory(new PropertyValueFactory<>("stato_correzione"));
            colonna_corretto.setPrefWidth(200);

            if (arrayList_test_svolto != null) {
                table.setItems(FXCollections.observableArrayList(arrayList_test_svolto));
                table.getColumns().addAll(colonna_id_test,colonna_nome,colonna_dataConsegna,colonna_corretto);
            }

//            listv_lista_test.getItems().addAll(arrayList_test);
        }

    }






    //funzione che prende l'id del test scelto
    public int prendi_id_test(){


     Test_svolto test = (Test_svolto) table.getSelectionModel().getSelectedItem();


    int id_test= test.getId_test();
    System.out.println("ID test ---> "+ id_test);

    return id_test;
}










    @FXML
    private void indietro() throws IOException {
        change_scene(this.getClass(), "elenco_test");
    }








    public void click_test(MouseEvent event) throws IOException {
        if (utente instanceof Docente){
            correggi_test(event);

        }
    }








    @FXML
    public void correggi_test(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2){

            if (utente instanceof Docente) {

                Test_svoltiDB_DAO test_svoltiDB_dao = new Test_svoltiDB();
                //serve il nome del test
                test_svolto = test_svoltiDB_dao.get_test(connection,prendi_id_test());

                if (test_svolto.getStato_correzione() == Test_svolto_status.IN_ATTESA_CORREZIONE.getStatusId()){
                    //CORREGGI IL TEST
                    Utils.change_scene(this.getClass(),"correggi_test");

                }
                else{
                    // rivedi il test GIA CORRETTO
                    Utils.change_scene(this.getClass(),"visualizza_test");

                }
            }
        }
    }

}
