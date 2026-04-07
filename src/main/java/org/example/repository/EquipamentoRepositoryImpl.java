package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Equipamento;

import java.sql.*;

public class EquipamentoRepositoryImpl implements EquipamentoRepository {
    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        String command = """
                INSERT INTO Equipamento
                (nome,
                numeroDeSerie,
                areaSetor,
                statusOperacional)
                VALUES
                (?,
                ?,
                ?,
                ?)
                """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, equipamento.getNome());
            stmt.setString(2, equipamento.getNumeroDeSerie());
            stmt.setString(3, equipamento.getAreaSetor());
            if(equipamento.getStatusOperacional() == null){
                equipamento.setStatusOperacional("OPERACIONAL");
                System.out.println("Status Alterado!");
            }
            stmt.setString(4, equipamento.getStatusOperacional());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
            equipamento.setId(rs.getLong(1));
        }
            }
        return equipamento;
    }

    @Override
    public Equipamento buscarEquipamentoPorId(long id) throws SQLException {
        String command = """
             SELECT 
             nome, 
             numeroDeSerie,
             areaSetor,
             statusOperacional
             FROM Equipamento
             WHERE id = ?
    """;
        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                var equipamento = new Equipamento(
                        id,
                  rs.getString("nome"),
                  rs.getString("numeroDeSerie"),
                  rs.getString("areaSetor"),
                  rs.getString("statusOperacional")
                );
                return equipamento;
            }else{
                throw new RuntimeException("Equipamento não encontrado!");
            }
        }
    }

    @Override
    public void atualizarStatusEquipamento(long id, String status) throws SQLException {
        String command = """
            UPDATE Equipamento 
            SET statusOperacional = ?
            WHERE id = ?
    """;
        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)
        ){
            stmt.setString(1, status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }
}


/*Create Base

     CREATE TABLE IF NOT EXISTS Equipamento (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(255) NOT NULL,
                numeroDeSerie VARCHAR(100) NOT NULL UNIQUE,
                areaSetor VARCHAR(100) NOT NULL,
                statusOperacional VARCHAR(50) NOT NULL,

                -- Garante que o status só possa ter valores pré-definidos
                CONSTRAINT chk_status_equipamento CHECK (statusOperacional IN ('OPERACIONAL', 'EM_MANUTENCAO', 'INATIVO'))
                    );
            """;


 */