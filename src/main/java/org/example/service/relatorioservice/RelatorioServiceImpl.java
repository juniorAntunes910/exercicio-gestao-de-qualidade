package org.example.service.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.repository.FalhaRepository;
import org.example.repository.FalhaRepositoryImpl;
import org.example.repository.RelatorioServiceRepository;
import org.example.repository.RelatorioServiceRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{
    private RelatorioServiceRepository relatorioServiceRepository = new RelatorioServiceRepositoryImpl();
    private FalhaRepository falhaRepository = new FalhaRepositoryImpl();
    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        return relatorioServiceRepository.relatoriosParada();
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        return relatorioServiceRepository.buscarEquipamentosSemFalhasPorPeriodo(dataInicio, datafim);
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        if(!falhaRepository.comprovarExistenciaFalha(falhaId)){
            throw new RuntimeException();
        }
        return relatorioServiceRepository.relatorioFalhaDetalhada(falhaId);
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        if(contagemMinimaFalhas <= 0){
            throw new RuntimeException();
        }
        return relatorioServiceRepository.gerarRelatorioManutencaoPreventiva(contagemMinimaFalhas);
    }
}
