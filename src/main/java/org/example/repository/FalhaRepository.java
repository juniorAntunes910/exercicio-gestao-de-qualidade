package org.example.repository;

import org.example.model.Falha;

import java.sql.SQLException;
import java.util.List;

public interface FalhaRepository {

    Falha registrarNovaFalha(Falha falha) throws SQLException;

    List<Falha> buscarFalhasCriticasAbertas() throws SQLException;

    boolean comprovarExistenciaFalha(long id) throws  SQLException;

    long retornaIdEquipamento(long id) throws  SQLException;

    void atualizaFalha(long id) throws SQLException;
}
