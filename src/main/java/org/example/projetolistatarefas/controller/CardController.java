package org.example.projetolistatarefas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.projetolistatarefas.Dao.ConnectionFactory;
import org.example.projetolistatarefas.Dao.TarefaDao;
import org.example.projetolistatarefas.Model.Tarefa;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class CardController {
    @FXML private Label tituloLb;
    @FXML private Label dataLb;
    @FXML private Label descLb;
    @FXML private Label statusLb;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;

    private ButtonType buttonType;

    private TarefaDao tarefaDao;
    private Tarefa tarefa;

    @FXML
    public void initialize(){
        Connection connection = ConnectionFactory.getConnection();
        tarefaDao = new TarefaDao(connection);

        excluirButton.setOnAction(actionEvent -> {
            try {
                excluirTarefa();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        editarButton.setOnAction(actionEvent -> {
            try {
                telaEditarTarefa();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setTarefa(Tarefa tarefa){
        this.tarefa = tarefa; // Seta a instância de tarefa no card
        tituloLb.setText(tarefa.getTitulo());
        dataLb.setText(tarefa.getDataEntrega().toString());
        descLb.setText(tarefa.getDescricao());
        statusLb.setText(tarefa.getStatus());
    }

    private void excluirTarefa() throws SQLException {
        if (tarefa != null && confirmarExclusao()) {
            // Remove do banco de dados
            tarefaDao.removerTarefa(tarefa.getId());

            // Remove da interface
            // O botão está dentro do AnchorPane (card), que está dentro da VBox
            AnchorPane card = (AnchorPane) excluirButton.getParent(); // o card inteiro
            VBox tarefasBox = (VBox) card.getParent(); // VBox onde os cards estão

            tarefasBox.getChildren().remove(card); // Remove o card atual da tela
        }
    }

    private void telaEditarTarefa() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetolistatarefas/add-tarefa.fxml"));
        Parent root= loader.load();

        // Pega o controller da tela carregada
        AddTarefaController controller = loader.getController();
        controller.setTarefaParaEditar(tarefa); // Passa a tarefa a ser editada

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage atualStage = (Stage) editarButton.getScene().getWindow();
        atualStage.close();
    }


    private boolean confirmarExclusao(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exclir tarefa");
        alert.setHeaderText(null);
        alert.setContentText("Isso irá apagar a tarefa. Deseja excluir?");

        Optional<ButtonType> resultado = alert.showAndWait(); // Exibe o alerta e aguarda a decisão do usuário
        return resultado.isPresent() && resultado.get() == ButtonType.OK; // Vai pegar a decisão do usuário (excluir ou não)
    }
}
