package gui.sgbmodel.dao;

import db.DB;
import gui.sgbmodel.dao.impl.AdiantamentoDaoJDBC;
import gui.sgbmodel.dao.impl.AnosDaoJDBC;
import gui.sgbmodel.dao.impl.CargoDaoJDBC;
import gui.sgbmodel.dao.impl.CartelaCommitDaoJDBC;
import gui.sgbmodel.dao.impl.CartelaDaoJDBC;
import gui.sgbmodel.dao.impl.CartelaPaganteDaoJDBC;
import gui.sgbmodel.dao.impl.CartelaVirtualDaoJDBC;
import gui.sgbmodel.dao.impl.ClienteDaoJDBC;
import gui.sgbmodel.dao.impl.EmpresaDaoJDBC;
import gui.sgbmodel.dao.impl.EntradaDaoJDBC;
import gui.sgbmodel.dao.impl.FechamentoAnoDaoJDBC;
import gui.sgbmodel.dao.impl.FechamentoMesDaoJDBC;
import gui.sgbmodel.dao.impl.FolhaMesDaoJDBC;
import gui.sgbmodel.dao.impl.FuncionarioDaoJDBC;
import gui.sgbmodel.dao.impl.GrupoDaoJDBC;
import gui.sgbmodel.dao.impl.LoginDaoJDBC;
import gui.sgbmodel.dao.impl.MesesDaoJDBC;
import gui.sgbmodel.dao.impl.ProdutoDaoJDBC;
import gui.sgbmodel.dao.impl.SituacaoDaoJDBC;

public class DaoFactory {

/*
 * � obrigat�rio passar uma conex�o como argumento (db.getCon...)
 */
 	public static GrupoDao createGrupoDao() {
		return new GrupoDaoJDBC(DB.getConnection());
	}
	
  	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
	
  	public static CargoDao createCargoDao() {
		return new CargoDaoJDBC(DB.getConnection());
	}
	
  	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
  	public static SituacaoDao createSituacaoDao() {
		return new SituacaoDaoJDBC(DB.getConnection());
	}
	
  	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
  	public static CartelaPaganteDao createCartelaPaganteDao() {
		return new CartelaPaganteDaoJDBC(DB.getConnection());
	}
	
  	public static CartelaVirtualDao createCartelaVirtualDao() {
		return new CartelaVirtualDaoJDBC(DB.getConnection());
	}
	
  	public static CartelaCommitDao createCartelaCommitDao() {
		return new CartelaCommitDaoJDBC(DB.getConnection());
	}

  	public static CartelaDao createCartelaDao() {
		return new CartelaDaoJDBC(DB.getConnection());
	}

  	public static EntradaDao createEntradaDao() {
		return new EntradaDaoJDBC(DB.getConnection());
	}
  	
  	public static AdiantamentoDao createAdiantamentoDao() {
		return new AdiantamentoDaoJDBC(DB.getConnection());
	}
  	
  	public static MesesDao createMesesDao() {
		return new MesesDaoJDBC(DB.getConnection());
	}
  	
  	public static FolhaMesDao createFolhaMesDao() {
		return new FolhaMesDaoJDBC(DB.getConnection());
	}
  	
  	public static AnosDao createAnosDao() {
		return new AnosDaoJDBC(DB.getConnection());
	}
  	
  	public static FechamentoAnoDao createFechamentoAnoDao() {
		return new FechamentoAnoDaoJDBC(DB.getConnection());
	}
  	
  	public static FechamentoMesDao createFechamentoMesDao() {
		return new FechamentoMesDaoJDBC(DB.getConnection());
	}
  	
  	public static LoginDao createLoginDao() {
		return new LoginDaoJDBC(DB.getConnection());
	}

  	public static EmpresaDao createEmpresaDao() {
		return new EmpresaDaoJDBC(DB.getConnection());
	}

}
