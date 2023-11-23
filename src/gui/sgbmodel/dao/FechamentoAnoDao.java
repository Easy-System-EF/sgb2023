package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.FechamentoAno;

public interface FechamentoAnoDao {

	void insert(FechamentoAno obj);
 	List<FechamentoAno> findAll();
	void zeraAll();
}
