package org.example.repository;

import org.example.model.Equipamento;

import java.sql.SQLException;

public  interface EquipamentoRepository {

    Equipamento criarEquipamento(Equipamento equipamento) throws SQLException;

    Equipamento buscarEquipamentoPorId(long id) throws SQLException;

    void atualizarStatusEquipamento(long id, String status) throws SQLException;
}
