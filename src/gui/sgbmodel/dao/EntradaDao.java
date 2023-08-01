package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Entrada;

public interface EntradaDao {

	void insert(Entrada obj);
	void update(Entrada obj);
	void deleteById(int cod);
	void zeraAll();
	List<Entrada> findByNnf(int nnf); 
	Entrada findById(int cod); 
 	List<Entrada> findAll();
}
  