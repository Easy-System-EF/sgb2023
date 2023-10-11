package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import db.DbException;
import gui.sgb.ConvenioImprimeController;
import gui.sgbmodel.dao.CartelaCommitDao;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.util.DataStaticSGB;

public class CartelaCommitDaoJDBC implements CartelaCommitDao {

	private Connection conn;
	
	public CartelaCommitDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	private CartelaService carService = new CartelaService(); 
	private CartelaVirtualService virService = new CartelaVirtualService(); 
	private CartelaPaganteService pagService = new CartelaPaganteService(); 
	private FuncionarioService funService = new FuncionarioService();
	private AdiantamentoService adiService = new AdiantamentoService();
	private GrupoService gruService = new GrupoService();
	LocalDate dt = DataStaticSGB.criaLocalAtual();
	
	public void gravaCartelaCommit(Cartela objCar, String letra, String sit, String baixa, Integer numEmp, String forma) {
		String classe = "Cartela Commit ";
		try {
			conn.setAutoCommit(false);

			if (baixa == "Sim") {
				String nomeCliente = objCar.getClienteCar();
				Double totConvenio = carService.sumCliente(nomeCliente);
				List<Cartela> listCar = carService.findClienteAberto(nomeCliente);
				for (Cartela c : listCar) {
					objCar = c;
					objCar.setSituacaoCar(letra);
					objCar.setNomeSituacaoCar(sit);
					objCar.setMesPagCar(DataStaticSGB.mesDaData(dt));
					objCar.setAnoPagCar(DataStaticSGB.anoDaData(dt));
					carService.saveOrUpdate(objCar);
				}	
				situacaoVir(objCar, letra, sit);
				pagante(listCar, letra, forma);
				imprimeReciboConvenio(nomeCliente, numEmp, totConvenio);
			} else {
				objCar.setSituacaoCar(letra);
				objCar.setNomeSituacaoCar(sit);
				carService.saveOrUpdate(objCar);
				situacaoVir(objCar, letra, sit);
			}
			
//int i = 2;
//if (i > 0) {
//	throw new DbException("Erro fake ");
//}						
			conn.commit();			
		} catch (SQLException e) {
			try {	
				conn.rollback();
				throw new DbException("bug rollback CAUSA: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Erro!!! rollback " + classe + e1.getMessage());
			}
		}
		finally {
		}		
	}

	private void situacaoVir(Cartela objCar, String letra, String sit) {
		@SuppressWarnings("unused")
		int nada = 0;
		Grupo grupo = null;
		List<CartelaVirtual> listV = virService.findCartela(objCar.getNumeroCar());
		try {
			for (CartelaVirtual cv : listV) {
				if (cv.getOrigemIdCarVir() == objCar.getNumeroCar()) {
					cv.setSituacaoVir(letra);
					grupo = gruService.findById(cv.getProduto().getGrupoProd());
					if (grupo.getNomeGru().equals("Serviço") ||
						grupo.getNomeGru().equals("Servico" ) ||
							cv.getNomeFunVir().equals("Consumo Próprio") || 
								cv.getNomeFunVir().equals("Consumo Proprio")) {
						nada = 1;
					} else {	
						if (sit.equals("Pago")) {
							double vlr = cv.getVendaProdVir() * cv.getQuantidadeProdVir();
							comissao(cv.getFuncionario().getCodigoFun(), objCar.getDataCar(), vlr, 
									objCar.getNumeroCar());
						}	
					}	
					virService.saveOrUpdate(cv);
				}
			}	
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback virtual, CAUSA: " + e1.getMessage());
		}
	}
	
	@SuppressWarnings("static-access")
	private void comissao(int codFun, Date data, double vlr, int cod) {
		Funcionario fun;
		Adiantamento adi = new Adiantamento();
		try {
			fun = funService.findById(codFun);
			adi.setCodigoFun(codFun);
			adi.setNomeFun(fun.getNomeFun());
			adi.setCargo(fun.getCargo());
			adi.setSituacao(fun.getSituacao());
			adi.setMesFun(fun.getMesFun());
			adi.setAnoFun(fun.getAnoFun());
			adi.setCargoFun(fun.getCargo().getNomeCargo());
			adi.setSituacaoFun(fun.getSituacao().getNomeSit());
			adi.setSalarioFun(fun.getCargo().getSalarioCargo());
			adi.setComissaoFun(0.00);
			adi.setNumeroAdi(null);
			adi.setDataAdi(data);
	/*
	 * aqui, qdo saida (venda) oda atributo vale recebe o vlr do percentual p/ calculo d comissao
	 */
			adi.setValeAdi(0.00);
			adi.perComissao = fun.getCargo().getComissaoCargo();
			adi.setMesAdi(DataStaticSGB.mesDaData(dt));
			adi.setAnoAdi(DataStaticSGB.anoDaData(dt));
			adi.setValorCartelaAdi(vlr);
			adi.setCartelaAdi(cod);
			adi.setTipoAdi("C");
			adi.setSalarioAdi(fun.getCargo().getSalarioCargo());
			adi.setComissaoAdi(0.00);
			adi.calculaComissao();
			adiService.saveOrUpdate(adi);
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback comissão, CAUSA: " + e1.getMessage());
		}
	}
	
	private void pagante(List<Cartela> listCar, String letra, String forma) {
		CartelaPagante objPag = new CartelaPagante();
		try {
			for (Cartela c: listCar) {
				double parcela = c.getTotalCar() /  c.getNumeroPaganteCar();
				pagService.remove(c.getNumeroCar());
				int numPag = 0;
				while (numPag < c.getNumeroPaganteCar()) {
					numPag += 1;
					if (c.getNumeroPaganteCar() > 1 ) {
						if (c.getNumeroPaganteCar() == numPag) {
							double resto = c.getTotalCar() - (parcela * (numPag - 1));
							parcela = resto;
						}	
					}
					objPag = new CartelaPagante();
					objPag.setNumeroCartelaPag(null);
					objPag.setPaganteCartelaPag(numPag);
					objPag.setDataCartelaPag(new Date());
					objPag.setLocalCartelaPag(c.getLocalCar());
					objPag.setValorCartelaPag(parcela);
					objPag.setFormaCartelaPag(forma);
					objPag.setSituacaoCartelaPag(letra);
					objPag.setCartelaIdOrigemPag(c.getNumeroCar());
					objPag.setMesCartelaPag(c.getMesCar());
					objPag.setAnoCartelaPag(c.getAnoCar());
					objPag.setMesPagamentoPag(c.getMesPagCar());
					objPag.setAnoPagamentoPag(c.getAnoPagCar());
					pagService.insert(objPag);
				}	
			}	
		} catch (DbException e1) {
			throw new DbException("Erro!!! rollback pagante, CAUSA: " + e1.getMessage());
		}
	}
	
	private void imprimeReciboConvenio(String nome, Integer numEmp, Double total) {
		ConvenioImprimeController convenioImp = new ConvenioImprimeController();
		ConvenioImprimeController.numEmp = numEmp;
		convenioImp.imprimeConvenio(nome, total);
	}
}
