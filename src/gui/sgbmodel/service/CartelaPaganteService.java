package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.CartelaPaganteDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.CartelaPagante;
  
public class CartelaPaganteService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private CartelaPaganteDao dao = DaoFactory.createCartelaPaganteDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	
	public void insert(CartelaPagante obj) {
		if (obj.getNumeroCartelaPag() == null) {
			dao.insert(obj);
		}
	}

	public void insertBackUp(CartelaPagante obj) {
		dao.insertBackUp(obj);
	}

	public List<CartelaPagante> findByCartela(Integer idCar) {
		return dao.findByCartela(idCar);
	}
	
	public List<CartelaPagante> findAll() {
		return dao.findAll();
	}
	
	public CartelaPagante findById(Integer codigo) {
		return dao.findById(codigo);
	}
	
	public List<CartelaPagante> findBySituacao(String local, String sit) {
		return dao.findBySituacao(local, sit);
	}

	public List<CartelaPagante> findByMesAnoAberto(int mm, int aa, String str) {
		return dao.findByMesAnoAberto(mm, aa, str);
	}

	public List<CartelaPagante> findByMesAnoPago(int mm, int aa, String str) {
		return dao.findByMesAnoPago(mm, aa, str);
	}

// removendo
	public void remove(Integer numero) {
		dao.deleteById(numero);
	}
}
