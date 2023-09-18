package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.CartelaVirtualDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.CartelaVirtual;
  
public class CartelaVirtualService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private CartelaVirtualDao dao = DaoFactory.createCartelaVirtualDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	
	public void saveOrUpdate(CartelaVirtual obj) {
		if (obj.getNumeroVir() == null) {
			dao.insert(obj);
		} else
			dao.update(obj);
	}
	
	public void insertBackUp(CartelaVirtual obj) {
		dao.insertBackUp(obj);
	}
	
	public List<CartelaVirtual> findSituacao(String local, String situacao) {
   		return dao.findSituacao(local, situacao);
	} 
	
	public List<CartelaVirtual> findCartela(Integer idCar) {
   		return dao.findCartela(idCar);
	} 
	
	public List<CartelaVirtual> findAll() {
   		return dao.findAll();
	} 
	
	public List<CartelaVirtual> findPesquisaFunc(String str) {
   		return dao.findPesquisaFunc(str);
	} 
	
	public List<CartelaVirtual> findPesquisaProd(String str) {
   		return dao.findPesquisaProd(str);
	} 
	
	public CartelaVirtual findByProduto(String str) {
   		return dao.findByProduto(str);
	} 
	
	public Double sumTotalCartela(int numCar) {
   		return dao.sumTotalCartela(numCar);
	} 
	
	public Double sumTotalCusto(int numCar) {
   		return dao.sumTotalCusto(numCar);
	} 
	
// removendo
	public void removeLinha(Integer codVir) {
		dao.deleteLinha(codVir);
	}

	public void removeCartela(Integer codCar) {
		dao.deleteCartela(codCar);
	}

	public void zeraAll() {
   		dao.zeraAll();
	} 
	
}
