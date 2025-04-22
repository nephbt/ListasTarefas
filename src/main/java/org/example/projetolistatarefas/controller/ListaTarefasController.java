package org.example.projetolistatarefas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import org.example.projetolistatarefas.Dao.ConnectionFactory;
import org.example.projetolistatarefas.Dao.TarefaDao;
import org.example.projetolistatarefas.Model.Tarefa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListaTarefasController {
    @FXML private VBox tarefasBox;
    @FXML private Label dataTela;
    @FXML private Label horaTela;

    private TarefaDao tarefaDao;

    @FXML
    public void initialize() throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        tarefaDao = new TarefaDao(connection);
        carregarTarefas();
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
}
