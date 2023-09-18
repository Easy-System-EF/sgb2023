package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);
	void insertBackUp(Fornecedor obj);
	void update(Fornecedor obj);
	void deleteById(int codigo);
	Fornecedor findById(int codigo); 
 	List<Fornecedor> findAll();
 	List<Fornecedor> findPesquisa(String str);

}
