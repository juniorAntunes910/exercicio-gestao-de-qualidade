package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.repository.AcaoCorretivaRepositoryImpl;
import org.example.repository.EquipamentoRepositoryImpl;
import org.example.repository.FalhaRepositoryImpl;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{
    private AcaoCorretivaRepositoryImpl acaoCorretivaRepository = new AcaoCorretivaRepositoryImpl();
    private FalhaRepositoryImpl falhaRepository = new FalhaRepositoryImpl();
    private EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();
    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        falhaRepository.comprovarExistenciaFalha(acao.getFalhaId());
        equipamentoRepository.atualizarStatusEquipamento(falhaRepository.retornaIdEquipamento(acao.getFalhaId()), "OPERACIONAL");
        falhaRepository.atualizaFalha(acao.getFalhaId());
        return acaoCorretivaRepository.registrarConclusaoDeAcao(acao);
    }
}
