package org.example.projetolistatarefas.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.projetolistatarefas.Dao.ConnectionFactory;
import org.example.projetolistatarefas.Dao.TarefaDao;
import org.example.projetolistatarefas.Model.Tarefa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ListaTarefasController {
    @FXML private VBox tarefasBox;
    @FXML private Label dataTela;
    @FXML private Label horaTela;
    @FXML private Button addButton;

    LocalDate dataHoje = LocalDate.now();


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private TarefaDao tarefaDao;

    @FXML
    public void initialize() throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        tarefaDao = new TarefaDao(connection);

        carregarTarefas();
        atualizarHora();
        dataTela.setText(String.valueOf(dataHoje));

        addButton.setOnAction(actionEvent -> {
            try {
                telaAddTarefa();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void carregarTarefas() throws SQLException {
        List<Tarefa> tarefas = tarefaDao.listarTarefas();
        tarefasBox.getChildren().clear();

        for (Tarefa tarefa : tarefas){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetolistatarefas/card-tarefas.fxml"));
                AnchorPane card = loader.load();

                CardController controller = loader.getController();
                controller.setTarefa(tarefa);

                tarefasBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizarHora(){
        Thread thread = new Thread(() -> {
           while (true) {
               LocalTime agora = LocalTime.now();
               String horaFormatada = agora.format(formatter);

               Platform.runLater(() -> horaTela.setText(horaFormatada));

               try{
                   Thread.sleep(1000);
               } catch (InterruptedException e){
                   e.printStackTrace();
               }
           }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void telaAddTarefa() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetolistatarefas/add-tarefa.fxml"));
        Parent root= loader.load();

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage atualStage = (Stage) addButton.getScene().getWindow();
        atualStage.close();
    }
}
