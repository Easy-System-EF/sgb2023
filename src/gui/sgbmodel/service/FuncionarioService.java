package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.FuncionarioDao;
import gui.sgbmodel.entities.Funcionario;
 
public class FuncionarioService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FuncionarioDao dao = DaoFactory.createFuncionarioDao();

//    criar no fornecedorlist , uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Funcionario> findAll(int aa, int mm) {
   		return dao.findAll(aa, mm);
	} 
	
	public List<Funcionario> findPesquisa(String str, int aa, int mm) {
   		return dao.findPesquisa(str, aa, mm);
	} 
	
	public List<Funcionario> findByAtivo(String situacao, int aa, int mm) {
   		return dao.findByAtivo(situacao, aa, mm);
	} 
	
	public Funcionario findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getCodigoFun() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void insertBackUp(Funcionario obj) {
		dao.insertBackUp(obj);
	}

// removendo
	public void remove(Funcionario obj) {
		dao.deleteById(obj.getCodigoFun());
	}

	public void zeraAll() {
   		dao.zeraAll();
	} 
	
	public Double sumSalary(int aa, int mm) {
   		return dao.sumSalary(aa, mm);
	} 
}
