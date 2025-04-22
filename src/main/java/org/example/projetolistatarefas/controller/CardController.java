package org.example.projetolistatarefas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.projetolistatarefas.Dao.ConnectionFactory;
import org.example.projetolistatarefas.Dao.TarefaDao;
import org.example.projetolistatarefas.Model.Tarefa;
import javafx.scene.control.Label;

import java.sql.Connection;


public class CardController {
    @FXML private Label tituloLb;
    @FXML private Label dataLb;
    @FXML private Label descLb;
    @FXML private Label statusLb;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;

    private TarefaDao tarefaDao;

    @FXML
    public void initialize(){
        Connection connection = ConnectionFactory.getConnection();
        tarefaDao = new TarefaDao(connection);
    }

    public void setTarefa(Tarefa tarefa){
        tituloLb.setText(tarefa.getTitulo());
        dataLb.setText(tarefa.getDataEntrega().toString());
        descLb.setText(tarefa.getDescricao());
        statusLb.setText(tarefa.getStatus());
    }
}
