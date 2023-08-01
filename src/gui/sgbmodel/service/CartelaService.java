package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.CartelaDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Cartela;
 
public class CartelaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private CartelaDao dao = DaoFactory.createCartelaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cartela> findAll() {
   		return dao.findAll();
	} 
	
	public List<Cartela> findSituacao(String sit) {
   		return dao.findSituacao(sit);
	} 
	
	public Cartela findById(Integer cod) {
		return dao.findById(cod);
	} 

	public List<Cartela> findByMesAno(Integer mm, Integer aa, String str) {
		return dao.findByMesAno(mm, aa, str);
	} 

	public List<Cartela> findByMesAnoFecha(Integer mm, Integer aa, Integer mmp, Integer aap) {
		return dao.findByMesAnoFecha(mm, aa, mmp, aap);
	} 

	public List<Cartela> findSituacaoAberto(Integer mm, Integer aa, String strA, String strC) {
		return dao.findSituacaoAberto(mm, aa, strA, strC);
	} 

// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Cartela obj) {
		if (obj.getNumeroCar() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(int cod) {
		dao.deleteById(cod);
	}

	public void zeraAll() {
   		dao.zeraAll();
	} 
	
}
