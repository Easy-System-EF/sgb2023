package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.ClienteDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Cliente;
  
public class ClienteService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ClienteDao dao = DaoFactory.createClienteDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cliente> findAll() {
   		return dao.findAll();
	} 
	
	public Cliente findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
	public List<Cliente> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Cliente obj) {
		if (obj.getCodigoCli() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Cliente obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Cliente obj) {
		dao.deleteById(obj.getCodigoCli());
	}
}
