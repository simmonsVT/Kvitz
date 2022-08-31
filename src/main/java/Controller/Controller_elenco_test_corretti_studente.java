package Controller;

import Database.TestDB;
import Database.Test_svoltiDB;
import Database.UtenteDB;
import Model.Docente;
import Model.Test;
import Model.Test_svolto;
import Model.Test_svolto_status;
import PostgresDAO.TestDB_DAO;
import PostgresDAO.Test_svoltiDB_DAO;
import PostgresDAO.UtenteDB_DAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import utils.Utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static utils.Utils.*;


public class Controller_elenco_test_corretti_studente {




    @FXML
    TableView table_test;

    @FXML
    Button btnIndietro;

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


        if (arrayList_test_svolto == null)
            arrayList_test_svolto = new ArrayList<>();

        arrayList_test_svolto = test_svoltiDB_dao.get_lista_test_svolti_studente(connection, utente.getUsername());

        TableColumn<Test_svolto,String> colonna_idTest = new TableColumn<>("ID");
        colonna_idTest.setCellValueFactory(new PropertyValueFactory<>("id_test"));
        colonna_idTest.setPrefWidth(100);

        TableColumn<Test_svolto,String> colonna_nomeTest = new TableColumn<>("Nome test");
        colonna_nomeTest.setCellValueFactory(new PropertyValueFactory<>("nome_test"));
        colonna_nomeTest.setPrefWidth(300);



        TableColumn<Test_svolto,String> colonna_punteggio_finale = new TableColumn<>("Punteggio finale");
        colonna_punteggio_finale.setCellValueFactory(new PropertyValueFactory<>("punteggio_finale"));
        colonna_punteggio_finale.setPrefWidth(100);

        if (arrayList_test_svolto != null) {

            //Rende visibile la tabella solo se ci sono test già corretti
            table_test.setItems(FXCollections.observableArrayList(get_test_corretti(arrayList_test_svolto)));

            table_test.getColumns().addAll(colonna_idTest,colonna_nomeTest,colonna_punteggio_finale);
        }


    }






    //funzione che prende il nome del test scelto
    public String prendiNomeT(){


     Test test = (Test) table_test.getSelectionModel().getSelectedItem();


    String nome= test.getNome();
    String doc= test.getDocente().getUsername();

    System.out.println("Nome test ---> "+ nome);

    //return selected;
    return nome;
}


private ArrayList<Test_svolto> get_test_corretti(ArrayList<Test_svolto> lista_originale){
        ArrayList<Test_svolto> test_corretti = new ArrayList<Test_svolto>();
    for (Test_svolto t: lista_originale) {

        if (t.getStato_correzione() == Test_svolto_status.CORRETTO.getStatusId()){
            test_corretti.add(t);
        }

    }
    return test_corretti;
}








    @FXML
    private void menu() throws IOException {
        change_scene(this.getClass(), "menu_studente");
    }








    public void click_test(MouseEvent event) throws IOException {
        visualizza_test(event);
    }








    public void visualizza_test(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2){
            test_svolto = test_svoltiDB_dao.get_test(connection,prendi_id_test());
            Utils.change_scene(this.getClass(),"visualizza_test");
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

    //funzione che prende l'id del test scelto
    public int prendi_id_test() {


        Test_svolto test = (Test_svolto) table_test.getSelectionModel().getSelectedItem();


        int id_test = test.getId_test();
        System.out.println("ID test ---> " + id_test);

        return id_test;
    }
}
