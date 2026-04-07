package org.example.repository;

import org.example.model.AcaoCorretiva;

import java.sql.SQLException;

public interface AcaoCorretivaRepository {

    AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acaoCorretiva) throws SQLException;


}
