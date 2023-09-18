package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.Compromisso;

public interface CompromissoDao {

	void insert(Compromisso obj);
	void insertBackUp(Compromisso obj);
	void update(Compromisso obj);
 	void deleteById(int cod, int nnf);
  	List<Compromisso> findAll();
  	List<Compromisso> findPesquisa(String str);
 	List<Compromisso> findByFor(int cod);
 	Compromisso findById(int cod, int nnf);
	List<Compromisso> findByTipo(int tp);
	Double findByTotalFor(int cod);
}
