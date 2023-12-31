package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Cartela;

public interface CartelaDao {

	void insert(Cartela obj);
	void insertBackUp(Cartela obj);
	void update(Cartela obj);
	void deleteById(Integer codigo);
	void zeraAll();
	Cartela findById(Integer codigo); 
	List<Cartela> findByAno(Integer aa); 
	List<Cartela> findByMesAno(Integer mm, Integer aa, String str); 
	List<Cartela> findByMesAnoFecha(Integer mm, Integer aa, Integer mmp, Integer aap); 
 	List<Cartela> findAll();
 	List<Cartela> findSituacao(String sit);
	List<Cartela> findSituacaoAberto(Integer mm, Integer aa); 
	List<Cartela> findClienteAberto(String str); 	
	List<Cartela> findClientePago(String str, Integer mm, Integer aa);
	Double sumCliente(String str);
}
  