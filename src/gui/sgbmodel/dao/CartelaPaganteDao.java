package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.CartelaPagante;

public interface CartelaPaganteDao {

	void insert(CartelaPagante obj);
	void insertBackUp(CartelaPagante obj);
	void deleteById(Integer numero);
 	List<CartelaPagante> findBySituacao(String local, String situacao);
 	List<CartelaPagante> findByCartela(Integer idCar);
 	List<CartelaPagante> findAll();
 	List<CartelaPagante> findByMesAnoAberto(int mm, int aa, String str);
 	List<CartelaPagante> findByMesAnoPago(int mm, int aa, String str);
 	CartelaPagante findById(Integer codigo);
}
