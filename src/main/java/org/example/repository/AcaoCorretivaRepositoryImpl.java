package org.example.repository;


import org.example.database.Conexao;
import org.example.model.AcaoCorretiva;

import java.sql.*;

public class AcaoCorretivaRepositoryImpl implements AcaoCorretivaRepository {


    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acaoCorretiva) throws SQLException {
        String command = """
                INSERT INTO AcaoCorretiva
                (falhaId,
                dataHoraInicio,
                dataHoraFim,
                responsavel,
                descricaoAcao)
                VALUES
                (?,
                ?,
                ?,
                ?,
                ?)
              
                """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, acaoCorretiva.getFalhaId());
            stmt.setObject(2, acaoCorretiva.getDataHoraInicio());
            stmt.setObject(3, acaoCorretiva.getDataHoraFim());
            stmt.setString(4, acaoCorretiva.getResponsavel());
            stmt.setString(5, acaoCorretiva.getDescricaoArea());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                acaoCorretiva.setId(rs.getLong(1));
            }
        }
        return acaoCorretiva;
    }
}



/*

        """
            CREATE TABLE IF NOT EXISTS AcaoCorretiva (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                falhaId BIGINT NOT NULL,
                dataHoraInicio DATETIME NOT NULL,
                dataHoraFim DATETIME NOT NULL,
                responsavel VARCHAR(255) NOT NULL,
                descricaoAcao TEXT NOT NULL,

                CONSTRAINT fk_acao_falha FOREIGN KEY (falhaId)
                REFERENCES Falha(id)
                ON DELETE RESTRICT
            );
            """;

 */
