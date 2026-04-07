package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepositoryImpl implements FalhaRepository{
    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        String command = """
                INSERT INTO Falha 
                (equipamentoId,
                dataHoraOcorrencia,
                descricao,
                criticidade,
                status,
                tempoParadaHoras)
                VALUES
                (?,
                ?,
                ?,
                ?,
                ?,
                ?);
                """;
        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, falha.getEquipamentoId());
            stmt.setObject(2, falha.getDataHoraOcorrencia());
            stmt.setString(3, falha.getDescricao());
            stmt.setString(4, falha.getCriticidade());
            if ("CRITICA".equalsIgnoreCase(falha.getCriticidade())) {
                falha.setStatus("ABERTA");
            }
            stmt.setString(5, falha.getStatus());
            stmt.setBigDecimal(6, falha.getTempoParadaHoras());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                falha.setId(rs.getLong(1));
            }
            }
        return falha;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        String command = """
                SELECT 
                id,
                equipamentoId,
                dataHoraOcorrencia,
                descricao,
                criticidade,
                status,
                tempoParadaHoras
                FROM 
                Falha
                WHERE criticidade = 'CRITICA' AND status = 'ABERTA'
                """;
        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)){
            List<Falha> listFalha = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                var falha = new Falha(
                        rs.getLong("id"),
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                    );
                listFalha.add(falha);
            }
            return listFalha;
        }
    }

    @Override
    public boolean comprovarExistenciaFalha(long id) throws SQLException {
        String command = """
                SELECT descricao
                FROM Falha
                WHERE id = ?
                """;
        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                if(rs.getString(1) != null){
                    return true;
                }
            }
        }
        throw new RuntimeException("Falha não encontrada!");
    }

    @Override
    public long retornaIdEquipamento(long id) throws SQLException {
        String command = """
                SELECT equipamentoId
                FROM Falha
                WHERE id = ?
                """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(command)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getLong(1);
            }
    }
        throw new IllegalArgumentException();
    }

    @Override
    public void atualizaFalha(long id) throws SQLException {
        String command = """
                UPDATE Falha
                SET status = 'RESOLVIDA'
                WHERE id = ?
                """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(command)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}

/*
           """
            CREATE TABLE IF NOT EXISTS Falha (
                id  SERIAL PRIMARY KEY,
                equipamentoId BIGINT NOT NULL,
                dataHoraOcorrencia DATETIME NOT NULL,
                descricao TEXT NOT NULL,
                criticidade VARCHAR(50) NOT NULL,
                status VARCHAR(50) NOT NULL,
                tempoParadaHoras DECIMAL(10,2) DEFAULT 0.00,

                CONSTRAINT chk_criticidade_falha CHECK (criticidade IN ('BAIXA','MEDIA','ALTA','CRITICA')),
                CONSTRAINT chk_status_falha CHECK (status IN ('ABERTA','EM_ANDAMENTO','RESOLVIDA'))
            );
            """;

 */