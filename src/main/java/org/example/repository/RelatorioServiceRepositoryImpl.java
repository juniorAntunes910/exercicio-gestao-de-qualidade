package org.example.repository;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.AcaoCorretiva;
import org.example.model.Equipamento;
import org.example.model.Falha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceRepositoryImpl implements RelatorioServiceRepository{
    @Override
    public List<RelatorioParadaDTO> relatoriosParada() throws SQLException {
        String command = """
                SELECT 
                e.id,
                e.nome,
                f.tempoParadaHoras
                FROM Equipamento e
                JOIN Falha f ON e.id = f.equipamentoId
                """;
        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(command)){
            ResultSet rs = stmt.executeQuery();
            List<RelatorioParadaDTO> listRelatorioParadaDto = new ArrayList<>();
            while (rs.next()){
                var relatorio = new RelatorioParadaDTO(
                        rs.getLong(1),
                        rs.getString("nome"),
                        rs.getDouble("tempoParadaHoras")
                        );
                listRelatorioParadaDto.add(relatorio);
            }
            return listRelatorioParadaDto;
        }
    }

    @Override
    public Optional<FalhaDetalhadaDTO> relatorioFalhaDetalhada(long falhaId) throws SQLException {
        String command = """
                SELECT 
                f.id AS falha_id, f.equipamentoId, f.dataHoraOcorrencia, f.descricao,f.criticidade, f.status, f.tempoParadaHoras,
                e.id AS equip_id, e.nome, e.numeroDeSerie, e.areaSetor, e.statusOperacional
                FROM Falha f
                JOIN Equipamento e ON f.equipamentoId = e.id
                WHERE f.id = ?
                       
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)){
            stmt.setLong(1, falhaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                var falha = new Falha(
                        rs.getLong("falha_id"),
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                );

                var equipamento = new Equipamento(
                        rs.getLong("equip_id"),
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                );

                command = """
                        SELECT descricaoAcao FROM AcaoCorretiva WHERE falhaId = ?
                        """;

                try (PreparedStatement stmt2 = conn.prepareStatement(command)) {
                    stmt2.setLong(1, falhaId);
                    List<String> acao = new ArrayList<>();
                    ResultSet rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        acao.add(rs2.getString("descricaoAcao"));
                    }

                    return Optional.of(new FalhaDetalhadaDTO(falha, equipamento, acao));

                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        String command = """
                SELECT 
                e.id,
                e.nome,
                COUNT(f.id) AS total_falhas
                FROM equipamento e
                LEFT JOIN Falha f ON e.id = f.equipamentoId
                GROUP BY e.id, e.nome
                HAVING COUNT(f.id) >= ?;
                """;
        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(command)){
            stmt.setInt(1, contagemMinimaFalhas);
            ResultSet rs = stmt.executeQuery();
            List<EquipamentoContagemFalhasDTO> listEquip = new ArrayList<>();
            while (rs.next()){
                var EquipamentoCont = new EquipamentoContagemFalhasDTO(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getInt("total_falhas")
                );
                listEquip.add(EquipamentoCont);
            }
            return listEquip;
        }
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        String command = """
                SELECT 
                nome, 
                numeroDeSerie,
                areaSetor,
                statusOperacional,
                COUNT(f.id) AS total_falhas
                FROM equipamento e
                LEFT JOIN Falha f ON e.id = f.equipamentoId
                WHERE dataHoraOcorrencia BETWEEN ? AND ?
                GROUP BY e.id, e.nome
                HAVING COUNT(f.id) = 0
                """;
        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(command)){
            //            stmt.setObject(2, falha.getDataHoraOcorrencia());
            stmt.setObject(1, dataInicio);
            stmt.setObject(2, datafim);
            ResultSet rs = stmt.executeQuery();
            List<Equipamento> listEquip = new ArrayList<>();
            while (rs.next()){
                var EquipamentoCont = new Equipamento(
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                );
                listEquip.add(EquipamentoCont);
            }
            return listEquip;
        }
    }


}
