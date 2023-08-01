package gui.sgcpmodel.service;

import java.util.Date;
import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.ParcelaDao;
import gui.sgcpmodel.entites.Parcela;

public class ParcelaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ParcelaDao dao = DaoFactory.createParcelaDao();

//    criar no parcelalist uma dependencia na parc controlador para esse metodo, 
//	carregando e mostrando na view		
	public Double findSum(int ano, int mes, int tipo){
 		return dao.findSum(ano, mes, tipo);
	}
	
	public Double findSumAll(int ano, int mes){
 		return dao.findSumAll(ano, mes);
	}
	
	public Double findSumAberto(Date dti, Date dtf){
 		return dao.findSumAberto(dti, dtf);
	}
	
	public Double findSumPago(Date dti, Date dtf){
 		return dao.findSumPago(dti, dtf);
	}
	
	public List<Parcela> findAllAberto() {
   		return dao.findAllAberto();
	} 
	
	public List<Parcela> findAll() {
   		return dao.findAll();
	} 
	
	public List<Parcela> findAllPago() {
   		return dao.findAllPago();
	} 
	
	public List<Parcela> findPeriodoAberto() {
   		return dao.findPeriodoAberto();
	} 

	public List<Parcela> findPeriodoPago() {
   		return dao.findPeriodoPago();
	} 

	public List<Parcela> findByIdFornecedorAberto(int cod) {
   		return dao.findByIdFornecedorAberto(cod);
	} 

	public List<Parcela> findByIdFornecedorPago(int cod) {
   		return dao.findByIdFornecedorPago(cod);
	} 
	
	public List<Parcela> findByIdFornecedorNnf(int cod, int nnf) {
   		return dao.findByIdFornecedorNnf(cod, nnf);
	} 
	
	public List<Parcela> findByIdTipoAberto(int cod) {
   		return dao.findByIdTipoAberto(cod);
	} 

	public List<Parcela> findByIdTipoPago(int cod) {
   		return dao.findByIdTipoPago(cod);
	} 
	
	public Parcela findByIdForn(int cod) {
   		return dao.findByIdForn(cod);
	} 
	
 // * inserindo ou atualizando via dao
// * se o codigo n�o existe insere, se existe altera 
	public void saveUpdate(Parcela obj) {
			if (obj.getIdPar() == null) {
				dao.insert(obj);
			} else {
				dao.update(obj);
			}
	}	

// removendo
	public void removeNnf(int nnf, int codFor) {
			dao.deleteByNnf(nnf, codFor);
	}	
}