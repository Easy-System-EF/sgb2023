package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import db.DbException;
import gui.sgbmodel.dao.CartelaCommitDao;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.service.AdiantamentoService;
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
	private FuncionarioService funService = new FuncionarioService();
	private AdiantamentoService adiService = new AdiantamentoService();
	private GrupoService gruService = new GrupoService();
	
	int codFun = 0;
	double vlr = 0.0;

	@SuppressWarnings("unused")
	public void gravaCartelaCommit(Cartela objCar, String pc, String sit) {

	 	Calendar cal = Calendar.getInstance();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String classe = "Cartela Commit ";

		try {
			conn.setAutoCommit(false);

			objCar.setSituacaoCar(pc);
			objCar.setNomeSituacaoCar(sit);
			carService.saveOrUpdate(objCar);
			
			situacaoVir(objCar, pc, sit);
						
//			int i = 2;
//			if (i > 0) {
//				throw new DbException("Erro fake ");
//			}
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Erro!!! " + classe + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Erro!!! rollback " + classe + e1.getMessage());
			}
		}
		finally {
		}		
	}

	private void situacaoVir(Cartela objCar, String pc, String sit) {
		@SuppressWarnings("unused")
		int nada = 0;
		Grupo grupo = null;
		List<CartelaVirtual> listV = virService.findCartela(objCar.getNumeroCar());
		for (CartelaVirtual cv : listV) {
			if (cv.getOrigemIdCarVir() == objCar.getNumeroCar()) {
				cv.setSituacaoVir(pc);
				grupo = gruService.findById(cv.getProduto().getGrupoProd());
				if (grupo.getNomeGru().equals("Serviço") ||
						grupo.getNomeGru().equals("Servico" ) ||
							cv.getNomeFunVir().equals("Consumo Próprio") || 
								cv.getNomeFunVir().equals("Consumo Proprio")) {
					nada = 1;
				} else {	
					if (sit.equals("Pago")) {
						vlr = cv.getVendaProdVir() * cv.getQuantidadeProdVir();
						comissao(cv.getFuncionario().getCodigoFun(), objCar.getDataCar(), vlr, 
							objCar.getNumeroCar());
					}	
				}	
				virService.saveOrUpdate(cv);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void comissao(int codFun, Date data, double vlr, int cod) {
		Funcionario fun;
		Adiantamento adi = new Adiantamento();
		fun = funService.findById(codFun);
		adi.setCodigoFun(fun.getCodigoFun());
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
		LocalDate dt1 = DataStaticSGB.criaLocalAtual();

		adi.setValeAdi(0.00);
		adi.perComissao = fun.getCargo().getComissaoCargo();
		adi.setMesAdi(DataStaticSGB.mesDaData(dt1));
		adi.setAnoAdi(DataStaticSGB.anoDaData(dt1));
		adi.setValorCartelaAdi(vlr);
		adi.setCartelaAdi(cod);
		adi.setTipoAdi("C");
		adi.setSalarioAdi(fun.getCargo().getSalarioCargo());
		adi.setComissaoAdi(0.00);
		adi.calculaComissao();
		adiService.saveOrUpdate(adi);
	}
}

