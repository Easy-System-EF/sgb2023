package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.CartelaVirtual;

public interface CartelaVirtualDao {

	void insert(CartelaVirtual obj);
	void update(CartelaVirtual obj);
	void deleteLinha(Integer codVir);
	void deleteCartela(Integer codCar);
	void zeraAll();
 	List<CartelaVirtual> findCartela(Integer idCar);
 	List<CartelaVirtual> findAll();
 	List<CartelaVirtual> findSituacao(String local, String situacao);
 	List<CartelaVirtual> findPesquisaFunc(String str);
 	List<CartelaVirtual> findPesquisaProd(String str);
 	CartelaVirtual findByProduto(String str);
}
