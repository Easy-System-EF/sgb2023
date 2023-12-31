package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Adiantamento;

public interface AdiantamentoDao {

	void insert(Adiantamento obj);
	void insertBackUp(Adiantamento obj);
	void deleteById(Integer codigo);
	void zeraAll();
 	Adiantamento findMesIdFun(Integer cod, Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMesTipo(Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMes(Integer mes, Integer Ano);
 	List<Adiantamento> findPesquisaFun(String str);
 	List<Adiantamento> findByCartela(Integer idCar);
 	List<Adiantamento> findAll();
 	Double comSumTotal(int mm, int aa, int codFun);
 	Double comSumTotalGeral(int mm, int aa);
 	Double valeSumTotal(int mm, int aa, int codFun);
}
  