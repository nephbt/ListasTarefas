package org.example.projetolistatarefas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.projetolistatarefas.Dao.ConnectionFactory;
import org.example.projetolistatarefas.Dao.TarefaDao;
import org.example.projetolistatarefas.Model.Tarefa;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddTarefaController {
    @FXML private TextField tituloTxt;
    @FXML private TextArea descTxt;
    @FXML private ComboBox<String> statusCbox;
    @FXML private DatePicker dataPick;
    @FXML private Button salvarButton;
    @FXML private Button voltarButton;

    private TarefaDao tarefaDao;
    private Tarefa tarefaEditando = null;

    @FXML
    public void initialize(){
        Connection connection = ConnectionFactory.getConnection();
        tarefaDao = new TarefaDao(connection);
        statusCbox.getItems().addAll("A fazer", "Em andamento", "Concluída");

        salvarButton.setOnAction(actionEvent -> {
            try {
                salvarTarefa();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        voltarButton.setOnAction(actionEvent -> {
            try {
                voltarLista();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void salvarTarefa() throws SQLException, IOException {
        String titulo = tituloTxt.getText();
        String descricao = descTxt.getText();
        LocalDate localDate = dataPick.getValue();

        java.sql.Date dataEntrega = null;
        if (localDate != null) {
            dataEntrega = java.sql.Date.valueOf(localDate);
        }
        String status = statusCbox.getValue();

        if(titulo.isEmpty() || descricao.isEmpty() || dataEntrega == null || status.isEmpty()){
            campoVazio();
        } else {
            if(tarefaEditando == null) {
                // Modo adicionar nova tarefa
                Tarefa tarefa = new Tarefa(titulo, descricao, dataEntrega, status);
                tarefaDao.adicionarTarefa(tarefa);

                tituloTxt.clear();
                descTxt.clear();
                dataPick.setValue(null);
                statusCbox.setValue(null);

                tarefaAdicionada();
            } else {
                // Modo editar
                tarefaEditando.setTitulo(titulo);
                tarefaEditando.setDescricao(descricao);
                tarefaEditando.setDataEntrega(dataEntrega);
                tarefaEditando.setStatus(status);

                tarefaDao.atualizarTarefa(tarefaEditando);
                tarefaAtualizada();
                voltarLista();
            }
        }
    }

    public void setTarefaParaEditar(Tarefa tarefa) {
        this.tarefaEditando = tarefa;

        tituloTxt.setText(tarefa.getTitulo());
        descTxt.setText(tarefa.getDescricao());
        dataPick.setValue(tarefa.getDataEntrega().toLocalDate());
        statusCbox.setValue(tarefa.getStatus());
    }

    private void voltarLista() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetolistatarefas/lista-tarefas.fxml"));
        Parent root= loader.load();

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage atualStage = (Stage) voltarButton.getScene().getWindow();
        atualStage.close();
    }

    private void campoVazio(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText("Preencha todos os campos");
        alert.showAndWait();
    }

    private void tarefaAdicionada(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Parabéns");
        alert.setHeaderText(null);
        alert.setContentText("Cadastro realizado com sucesso!");
        alert.showAndWait();
    }

    private void tarefaAtualizada() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tarefa Atualizada");
        alert.setHeaderText(null);
        alert.setContentText("Tarefa atualizada com sucesso!");
        alert.showAndWait();
    }
}
