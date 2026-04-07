package org.example.service.falha;

import org.example.model.Falha;
import org.example.repository.EquipamentoRepositoryImpl;
import org.example.repository.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{
    private EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();
    private FalhaRepositoryImpl falhaRepository = new FalhaRepositoryImpl();
    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        try {
            equipamentoRepository.buscarEquipamentoPorId(falha.getEquipamentoId());
        }catch (RuntimeException e){
            throw new IllegalArgumentException("Equipamento não encontrado!");
        }

        if(falha.getCriticidade() == "CRITICA"){
            equipamentoRepository.atualizarStatusEquipamento(falha.getEquipamentoId(), "EM_MANUTENCAO");
        }
        return falhaRepository.registrarNovaFalha(falha);
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        return falhaRepository.buscarFalhasCriticasAbertas();
    }
}
