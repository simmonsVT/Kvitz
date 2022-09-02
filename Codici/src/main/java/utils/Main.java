package utils;

import Model.Docente;
import Model.Studente;
import Model.Utente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

import static utils.Utils.*;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        Utils.setStage(primaryStage);
        primaryStage.setTitle(nome_software);
        change_scene(getClass(),"first_page");
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../Gui/first_page.fxml")));
//        primaryStage.setTitle("Progetto2022");
//        scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.setFullScreen(true);

//        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
//        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
