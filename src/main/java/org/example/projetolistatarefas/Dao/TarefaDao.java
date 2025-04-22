package org.example.projetolistatarefas.Dao;

import org.example.projetolistatarefas.Model.Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDao {
    private Connection connection;

    public TarefaDao(Connection connection) {
        this.connection = connection;
    }

    // Adicionar Tarefa
    public void adicionarTarefa(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, descricao, data_entrega, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, tarefa.getDataEntrega());
            stmt.setString(4, tarefa.getStatus());

            stmt.executeUpdate();
        }
    }

    // Listar Tarefas
    public List<Tarefa> listarTarefas() throws SQLException {
        List<Tarefa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarefas";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Tarefa(
                    rs.getLong("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getDate("data_entrega"),
                    rs.getString("status")

                ));
            }
        }
        return lista;
    }

    // Atualizar Tarefa
    public void atualizarTarefa(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, data_entrega = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, tarefa.getDataEntrega());
            stmt.setString(4, tarefa.getStatus());
            stmt.setLong(5, tarefa.getId());

            stmt.executeUpdate();
        }
    }

    // Remover Tarefa
    public void removerTarefa(int id) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}