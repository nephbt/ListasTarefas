package org.example.projetolistatarefas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projetolistatarefas/lista-tarefas.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Lista de Tarefas");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
