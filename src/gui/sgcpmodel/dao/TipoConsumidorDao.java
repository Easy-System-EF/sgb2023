package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.TipoConsumo;

public interface TipoConsumidorDao {

	void insert(TipoConsumo obj);
	void insertBackUp(TipoConsumo obj);
	void update(TipoConsumo obj);
	void deleteById(int codigo);
	TipoConsumo findById(int codigo); 
	Integer findPesquisa(String nome); 
 	List<TipoConsumo> findAll();

}
