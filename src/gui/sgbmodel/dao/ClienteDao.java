package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);
	void insertBackUp(Cliente obj);
	void update(Cliente obj);
	void deleteById(Integer codigo);
	Cliente findById(Integer codigo); 
 	List<Cliente> findAll();
 	List<Cliente> findPesquisa(String str);

}
