package utils;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Utils {

    /** Oggetti GUI **/
    private static Scene scene;
    private static Parent root;
    private static Stage stage;
    public static int display_size[] = new int[10];
    public static String nome_software = "Kvíz";
    /**********************************************/

    /**Oggetti dell'engine *************************/
    public static Utente utente; // Sfrutto il polimorfismo per stabilire se chi sta utilizzando il software è un docente o studente
    public static Test test; //Oggetto utilizzato per manipolare i test
    public static Test_svolto test_svolto; // Utilizzato per la correzione dei test da parte del docente
    public static Connection connection; //Oggetto connessione per eseguire le query sul DB
    public static boolean correzione_quiz; //Utilizzato per aprire le activity di correzione in modalita correzione(true) oppure visualizzazione(false)
    /***************************************************/


    public static void setStage(Stage stage) {
        Utils.stage = stage;
    }

    /**
     * Lancia la scena passata come parametro, alla risoluzione del monitor. In caso di multi monitor verrà lanciata alla risoluzione più bassa
     * @param c
     * @param scene_name
     * @throws IOException
     */
    @FXML
    public static void change_scene(Class c ,String scene_name)throws IOException {

        switch (scene_name){
            case "first_page":
                root = FXMLLoader.load(c.getResource("../Gui/first_page.fxml"));
                break;
            case "login":
                root = FXMLLoader.load(c.getResource("../Gui/login.fxml"));
                break;
            case "registrazione":
                root = FXMLLoader.load(c.getResource("../Gui/registrazione.fxml"));
                break;
            case "menu_studente":
                root = FXMLLoader.load(c.getResource("../Gui/menu_studente.fxml"));
                break;
            case "menu_docente":
                root = FXMLLoader.load(c.getResource("../Gui/menu_docente.fxml"));
                break;
            case "inserisci_domanda":
                root = FXMLLoader.load(c.getResource("../Gui/inserisci_domanda.fxml"));
                break;
            case "elenco_test":
                root = FXMLLoader.load(c.getResource("../Gui/elenco_test.fxml"));
                break;
            case "svolgi_test":
                root = FXMLLoader.load(c.getResource("../Gui/svolgi_test.fxml"));
                break;
            case "consulta_punteggi":
                root = FXMLLoader.load(c.getResource("../Gui/consulta_correzione_studente.fxml"));
                break;

            case "elenco_test_correzione":
                root = FXMLLoader.load(c.getResource("../Gui/elenco_test_correzione.fxml"));
                break;

            case "correggi_test":
                correzione_quiz = true;
                root = FXMLLoader.load(c.getResource("../Gui/visualizza_correggi_test.fxml"));
                break;

            case "visualizza_test":
                correzione_quiz = false;
                root = FXMLLoader.load(c.getResource("../Gui/visualizza_correggi_test.fxml"));
                break;
        }

//        if ( display_size == null || display_size.length == 0)
        setScreenSize();

        scene = new Scene(root,display_size[0],display_size[1]-60);
        stage.setScene(scene);
//        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.show();
    }

    /**
     * Imposta le variabili di dimensione display
     */
    @FXML
    private static void setScreenSize(){
        short screen_num  = (short) Screen.getScreens().size();
        int screen_size[][] = new int[10][10];
        int min_width = 9999999;
        int min_height = 99999999;
        if (screen_num >= 2){
            for (int i = 0;i < screen_num;i++){
//                screen_size[i][0] = (int) Math.round(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
                screen_size[i][0] = (int) Screen.getScreens().get(i).getBounds().getWidth();
                if (min_width > screen_size[i][0] )
                    display_size[0] = screen_size[i][0];
                screen_size[i][1] = (int) Screen.getScreens().get(i).getBounds().getHeight();
                if (min_height > screen_size[i][1] )
                    display_size[1] = screen_size[i][1];
            }

        }
        else {
            display_size[0] = (int) Screen.getScreens().get(0).getBounds().getWidth();
            display_size[1] = (int) Screen.getScreens().get(0).getBounds().getHeight();
        }


    }

}
