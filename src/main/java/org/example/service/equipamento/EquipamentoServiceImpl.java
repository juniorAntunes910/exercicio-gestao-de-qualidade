package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.EquipamentoRepositoryImpl;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService{
    private EquipamentoRepositoryImpl repository = new EquipamentoRepositoryImpl();
    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        return repository.criarEquipamento(equipamento);
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        return repository.buscarEquipamentoPorId(id);
    }
}
