package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	void insertBackUp(Funcionario obj);
	void update(Funcionario obj);
	void deleteById(Integer codigo);
	void zeraAll();
	Funcionario findById(Integer codigo); 
 	List<Funcionario> findAll(int aa, int mm);
 	List<Funcionario> findPesquisa(String str, int aa, int mm);
 	List<Funcionario> findByAtivo(String situacao, int aa, int mm);
 	Double sumSalary(int aa, int mm);
}
  