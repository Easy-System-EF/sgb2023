package gui.copia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.entities.Situacao;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.LoginService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgbmodel.service.SituacaoService;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import gui.util.Cryptograf;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RestauraSgbController implements Initializable, DataChangeListener {

	@FXML
	private VBox vBoxRestaura;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Restaura> tableViewRestaura;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnFile;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnStatus;

 	@FXML
 	private TableColumn<Restaura, Integer> tableColumnCount;

 	private ObservableList<Restaura> obsList;

	Integer count = 0;
	Integer countAk = 0;
	String arq = "";
	String crip = "";
	String status = "";
	Date dataI = new Date(System.currentTimeMillis());
	Date dataF = new Date();
    String classe = "BackUp SGO ";
	static String meioSgb = ":\\Arqs\\Backup\\SGO\\";
	public String meioSgcp= ":\\Arqs\\Backup\\SGOCP\\";
	public static String ext = ".Bck";
	public static String file = "";
	public static String unid = null;
	public static String path = null;
 	public String user = "usuário";
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	Calendar cal = Calendar.getInstance();
 	
 	public RestauraService service;
 	private Restaura entity;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setRestauraService(RestauraService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Restaura entity) {
 		this.entity = entity;
 	}

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) throws ParseException {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/RestauraForm.fxml", parentStage);
// 		 service.zeraAll();
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() throws ParseException {
 		if (unid != null) {
 			count = 0;
//			Alerts.showAlert("Atenção", "isoo pode demorar um pouco", null, AlertType.WARNING);
//// 			anCartela();
 			login();
//// 			meses();
//// 			empresa();
 			grupo();
 			cargo();
 			situacao();
 			tipoConsumo();
 			fornecedor();
 			produto();
 			funcionario();
 			adiantamento();
 			Cartela();
 			cartelaVirtual();
			cartelaPagante();
			periodo();
			entrada();
			compromisso();
			parcela();
			labelFile.setText("Kbou!!!");
			labelFile.viewOrderProperty();
			labelCount.viewOrderProperty();
 		}	
 	}

	public void situacao() {
		status = "Ok";
		file = "Situacao";
		path = unid + meioSgb + file + ext;
		Situacao sit = new Situacao();
		SituacaoService sitService = new SituacaoService();		
		countAk = 0;
		try { BufferedReader brSit = new BufferedReader(new FileReader(path));
			String lineSit = brSit.readLine();
			while (lineSit != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineSit);
				String[] campoSit = crip.split(" , ");
				
				sit.setNumeroSit(Integer.parseInt(campoSit[0]));
				sit.setNomeSit(campoSit[1]);
				
//				sitService.insertBackUp(sit);
System.out.println(sit);				
				lineSit = brSit.readLine();
			}
			brSit.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void adiantamento() throws ParseException {
		status = "Ok";
		file = "Adiantamento";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		Funcionario fun = new Funcionario();
		FuncionarioService funService = new FuncionarioService();
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();
		Adiantamento adi = new Adiantamento();
		AdiantamentoService adiService = new AdiantamentoService();
		String arqAdi = "";
		try {BufferedReader brAdi = new BufferedReader(new FileReader(path));
			String lineAdi = brAdi.readLine();
			while (lineAdi != null) { 
				countAk +=1;
				arqAdi = Cryptograf.desCriptografa(lineAdi);
				String[] campoAdi = arqAdi.split(" , ");
				adi.setNumeroAdi(Integer.parseInt(campoAdi[0]));
				Date dt = sdfAno.parse(campoAdi[1]);
				cal.setTime(dt);
				adi.setDataAdi(cal.getTime());
				adi.setValeAdi(Double.parseDouble(campoAdi[2]));
				adi.setMesAdi(Integer.parseInt(campoAdi[3]));
				adi.setAnoAdi(Integer.parseInt(campoAdi[4]));
				adi.setValorCartelaAdi(Double.parseDouble(campoAdi[5]));
				adi.setCartelaAdi(Integer.parseInt(campoAdi[6]));
				adi.setComissaoAdi(Double.parseDouble(campoAdi[8]));
				adi.setTipoAdi(campoAdi[9]);
				adi.setSalarioAdi(Double.parseDouble(campoAdi[10]));
				adi.setCodigoFun(Integer.parseInt(campoAdi[11]));
				adi.setNomeFun(campoAdi[12]);
				adi.setMesFun(Integer.parseInt(campoAdi[13]));
				adi.setAnoFun(Integer.parseInt(campoAdi[14]));
				adi.setCargoFun(campoAdi[15]);
				adi.setSituacaoFun(campoAdi[16]);
				adi.setSalarioFun(Double.parseDouble(campoAdi[17]));
				fun = funService.findById(Integer.parseInt(campoAdi[11]));
				fun.setCargo(carService.findById(Integer.parseInt(campoAdi[18])));
				fun.setSituacao(sitService.findById(Integer.parseInt(campoAdi[19])));
				adi.setCargo(carService.findById(Integer.parseInt(campoAdi[18])));
				adi.setSituacao(sitService.findById(Integer.parseInt(campoAdi[19])));
				adiService.insertBackUp(adi);
				lineAdi = brAdi.readLine();
			}
			brAdi.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {	
			gravaRestaura();
		}
	}
	
	public void cargo() {
		status = "Ok";
		countAk = 0;
		file = "Cargo";
		path = unid + meioSgb + file + ext;
		Cargo car = new Cargo();
		CargoService carService = new CargoService();
		
		try {BufferedReader brCar = new BufferedReader(new FileReader(path));
		String lineCar = brCar.readLine();
		while (lineCar != null) { 
			countAk += 1;
			crip = Cryptograf.desCriptografa(lineCar);
			String[] campoCar = crip.split(" , ");
			car.setCodigoCargo(Integer.parseInt(campoCar[0]));
			car.setNomeCargo(campoCar[1]);
			car.setSalarioCargo(Double.parseDouble(campoCar[2]));
			car.setComissaoCargo(Double.parseDouble(campoCar[3]));
			carService.insertBackUp(car);
			lineCar = brCar.readLine();
		}
		brCar.close();
	}
	catch(	IOException e2) {
		status = "Er";
		throw new DbException(e2.getMessage());	 			
	}
	finally {					
		gravaRestaura();
	}
}
	
	public void entrada() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Entrada";
		path = unid + meioSgb + file + ext;
		ProdutoService prodService = new ProdutoService();
		FornecedorService forService = new FornecedorService();
		Entrada ent = new Entrada();
		EntradaService entService = new EntradaService();
		try {BufferedReader brEnt = new BufferedReader(new FileReader(path));
			String lineEnt = brEnt.readLine();
			while (lineEnt != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineEnt);
				String[] campoEnt = crip.split(" , ");
				ent.setNumeroEnt(Integer.parseInt(campoEnt[0]));
				ent.setNnfEnt(Integer.parseInt(campoEnt[1]));
				Date dtEnt = sdfAno.parse(campoEnt[2]);
				cal.setTime(dtEnt);
				ent.setDataEnt(cal.getTime());
				ent.setNomeFornEnt(campoEnt[3]);
				ent.setNomeProdEnt(campoEnt[4]);
				ent.setQuantidadeProdEnt(Double.parseDouble(campoEnt[5]));
				ent.setValorProdEnt(Double.parseDouble(campoEnt[6]));
				ent.setForn(forService.findById(Integer.parseInt(campoEnt[7])));
				ent.setProd(prodService.findById(Integer.parseInt(campoEnt[8])));

				entService.insertBackUp(ent);
				lineEnt = brEnt.readLine();
			}
			brEnt.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void funcionario() {
		status = "ok";
		countAk = 0;
		file = "Funcionario";
		path = unid + meioSgb + file + ext;
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();
		Funcionario fun = new Funcionario();
		FuncionarioService funService = new FuncionarioService();
		
		try {BufferedReader brFun = new BufferedReader(new FileReader(path));
			String lineFun = brFun.readLine();
			while (lineFun != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineFun);
				String[] campoFun = crip.split(" , ");
				fun.setCodigoFun(Integer.parseInt(campoFun[0]));
				fun.setNomeFun(campoFun[1]);
				fun.setEnderecoFun(campoFun[2]);
				fun.setBairroFun(campoFun[3]);
				fun.setCidadeFun(campoFun[4]);
				fun.setUfFun(campoFun[5]);
				fun.setCepFun(campoFun[6]);
				fun.setDddFun(Integer.parseInt(campoFun[7]));
				fun.setTelefoneFun(Integer.parseInt(campoFun[8]));
				fun.setCpfFun(campoFun[9]);
				fun.setPixFun(campoFun[10]);
				fun.setComissaoFun(Double.parseDouble(campoFun[11]));
				fun.setAdiantamentoFun(Double.parseDouble(campoFun[12]));
				fun.setMesFun(Integer.parseInt(campoFun[13]));
				fun.setAnoFun(Integer.parseInt(campoFun[14]));
				fun.setCargoFun(campoFun[15]);
				fun.setSituacaoFun(campoFun[16]);
				fun.setSalarioFun(Double.parseDouble(campoFun[17]));
				fun.setCargo(carService.findById(Integer.parseInt(campoFun[18])));
				fun.setSituacao(sitService.findById(Integer.parseInt(campoFun[19])));
				funService.insertBackUp(fun);
				lineFun = brFun.readLine();
			}
			brFun.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());
		}
		finally {			
			gravaRestaura();
		}
	}
	
	public void grupo() {
		status = "Ok";
		countAk = 0;
		file = "Grupo";
		path = unid + meioSgb + file + ext;
		Grupo gru = new Grupo();
		GrupoService gruService = new GrupoService();
		
		try { BufferedReader brGru = new BufferedReader(new FileReader(path));
			  String lineGru = brGru.readLine();
			  while (lineGru != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineGru);
				String[] campoGru = crip.split(" , ");
				gru.setCodigoGru(Integer.parseInt(campoGru[0]));
				gru.setNomeGru(campoGru[1]);
				gruService.insertBackUp(gru);
					lineGru = brGru.readLine();
			  }
				brGru.close();
			}
			catch(	IOException e2) {
				status = "Er";
				throw new DbException(e2.getMessage());	 			
			}
			finally {					
				gravaRestaura();
			}
		}
		
	@SuppressWarnings("null")
	public void login() throws ParseException {
		status = "Ok";
		file = "Login";
		path = unid + meioSgb + file + ext;
		Login log = new Login();
		LoginService logService = new LoginService();
		countAk = 0;
		try {BufferedReader brLog = new BufferedReader(new FileReader(path));
			String lineLog = brLog.readLine();
			while (lineLog != null) {
				countAk += 1;
				crip = Cryptograf.desCriptografa(lineLog);
				String[] campoLog = crip.split(" , ");
				log.setNumeroLog(Integer.parseInt(campoLog[0]));
				log = logService.findById(log.getNumeroLog());
				if (log == null) {
					log.setNumeroLog(Integer.parseInt(campoLog[0]));
					log.setSenhaLog(campoLog[1]);
					log.setNomeLog(campoLog[2]);
					log.setNivelLog(Integer.parseInt(campoLog[3]));
					log.setAlertaLog(Integer.parseInt(campoLog[4]));
					Date dtLog = sdfAno.parse(campoLog[5]);
					Date dtVen = sdfAno.parse(campoLog[6]);
					Date dtMax = sdfAno.parse(campoLog[7]);
					Date dtAce = sdfAno.parse(campoLog[8]);
					cal.setTime(dtLog);
					log.setDataLog(cal.getTime());
					cal.setTime(dtVen);
					log.setDataVencimentoLog(cal.getTime());
					cal.setTime(dtMax);
					log.setDataMaximaLog(cal.getTime());
					cal.setTime(dtAce);
					log.setAcessoLog(cal.getTime());
					log.setEmpresaLog(Integer.parseInt(campoLog[9]));
					if (log.getNumeroLog() == 1 || log.getNumeroLog() == 2) {
						logService.insertOrUpdate(log);
					} else { 
						logService.insertBackUp(log);
					}	
				}	
				lineLog = brLog.readLine();
			}
			brLog.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void produto() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Produto";
		path = unid + meioSgb + file + ext;
		GrupoService gruService = new GrupoService();
		Produto mat = new Produto();
		ProdutoService matService = new ProdutoService();
		
		try { BufferedReader brProd = new BufferedReader(new FileReader(path));
		  String lineProd = brProd.readLine();
		  while (lineProd != null) {
			countAk += 1;
			String crip = Cryptograf.desCriptografa(lineProd);
			String[] campoProd = crip.split(" , ");

			mat.setCodigoProd(Integer.parseInt(campoProd[0]));
			mat.setGrupoProd(Integer.parseInt(campoProd[1]));
			mat.setNomeProd(campoProd[2]);
			mat.setEstMinProd(Double.parseDouble(campoProd[3]));
			mat.setSaldoProd(Double.parseDouble(campoProd[4]));
			mat.setSaidaCmmProd(Double.parseDouble(campoProd[5]));
			mat.setCmmProd(Double.parseDouble(campoProd[6]));
			mat.setPrecoProd(Double.parseDouble(campoProd[7]));
			mat.setVendaProd(Double.parseDouble(campoProd[8]));
			mat.setPercentualProd(Double.parseDouble(campoProd[11]));
			mat.setLetraProd(campoProd[12].charAt(0));
			Date dtProd = sdfAno.parse(campoProd[13]);
			cal.setTime(dtProd);
			mat.setDataCadastroProd(cal.getTime());
			mat.setGrupo(gruService.findById(Integer.parseInt(campoProd[14])));
			
			matService.insertBackUp(mat);
			lineProd = brProd.readLine();
		  }
			brProd.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void cartelaVirtual() {
		status = "Ok";
		file = "CartelaVirtual";
		path = unid + meioSgb + file + ext;
		ProdutoService prodService = new ProdutoService();
		FuncionarioService funService = new FuncionarioService();
		CartelaVirtual vir = new CartelaVirtual();
		CartelaVirtualService virService = new CartelaVirtualService();
		countAk = 0;
		try { BufferedReader brVir = new BufferedReader(new FileReader(path));
			String lineVir = brVir.readLine();
			while (lineVir != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineVir);
				String[] campoVir = crip.split(" , ");
				vir.setNumeroVir(Integer.parseInt(campoVir[0]));
				vir.setLocalVir(campoVir[1]);
				vir.setSituacaoVir(campoVir[2]);
				vir.setNomeFunVir(campoVir[3]);
				vir.setNomeProdVir(campoVir[4]);
				vir.setQuantidadeProdVir(Double.parseDouble(campoVir[5]));
				vir.setPrecoProdVir(Double.parseDouble(campoVir[6]));
				vir.setVendaProdVir(Double.parseDouble(campoVir[7]));
				vir.setTotalProdVir(Double.parseDouble(campoVir[8]));
				vir.setOrigemIdCarVir(Integer.parseInt(campoVir[9]));
				vir.setFuncionario(funService.findById(Integer.parseInt(campoVir[10])));
				vir.setProduto(prodService.findById(Integer.parseInt(campoVir[11])));
				virService.insertBackUp(vir);
				lineVir = brVir.readLine();
			}
			brVir.close();
		}
		catch(RuntimeException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
		}
		catch(IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	
	public void Cartela() throws ParseException {
		status = "Ok";
		file = "CartelaServico";
		path = unid + meioSgb + file + ext;
		CartelaService carService = new CartelaService();
		Cartela car = new Cartela();
		countAk = 0;
		try { BufferedReader brCar = new BufferedReader(new FileReader(path));
			String lineCar = brCar.readLine();
			while (lineCar != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineCar);
				String[] campoCar = crip.split(" , ");

				car.setNumeroCar(Integer.parseInt(campoCar[0]));
				Date dtCar = sdfAno.parse(campoCar[1]);
				cal.setTime(dtCar);
				car.setDataCar(cal.getTime());
				car.setLocalCar(campoCar[2]);
				car.setDescontoCar(Double.parseDouble(campoCar[3]));
				car.setTotalCar(Double.parseDouble(campoCar[4]));
				car.setSituacaoCar(campoCar[5]);
				car.setNumeroPaganteCar(Integer.parseInt(campoCar[6]));
				car.setValorPaganteCar(Double.parseDouble(campoCar[7]));
				car.setMesCar(Integer.parseInt(campoCar[8]));
				car.setAnoCar(Integer.parseInt(campoCar[9]));
				car.setObsCar(campoCar[10]);
				car.setServicoCar(campoCar[11]);
				car.setValorServicoCar(Double.parseDouble(campoCar[12]));
				car.setSubTotalCar(Double.parseDouble(campoCar[13]));
				car.setNomeSituacaoCar(campoCar[14]);
				car.setMesPagCar(Integer.parseInt(campoCar[15]));
				car.setAnoPagCar(Integer.parseInt(campoCar[16]));				

				carService.insertBackUp(car);
				lineCar = brCar.readLine();
			}
			brCar.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void cartelaPagante() throws ParseException {
		status = "Ok";
		file = "CartelaServico";
		path = unid + meioSgb + file + ext;
		CartelaPaganteService carPService = new CartelaPaganteService();
		CartelaPagante carP = new CartelaPagante();
		countAk = 0;
		try { BufferedReader brCarP = new BufferedReader(new FileReader(path));
			String lineCarP = brCarP.readLine();
			while (lineCarP != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineCarP);
				String[] campoCar = crip.split(" , ");

				carP.setNumeroCartelaPag(Integer.parseInt(campoCar[0]));
				carP.setPaganteCartelaPag(Integer.parseInt(campoCar[1]));
				Date dtCarP = sdfAno.parse(campoCar[2]);
				cal.setTime(dtCarP);
				carP.setDataCartelaPag(cal.getTime());
				carP.setLocalCartelaPag(campoCar[3]);
				carP.setValorCartelaPag(Double.parseDouble(campoCar[4]));
				carP.setFormaCartelaPag(campoCar[5]);
				carP.setSituacaoCartelaPag(campoCar[6]);
				carP.setCartelaIdOrigemPag(Integer.parseInt(campoCar[7]));
				carP.setMesCartelaPag(Integer.parseInt(campoCar[8]));
				carP.setAnoCartelaPag(Integer.parseInt(campoCar[9]));
				carP.setMesPagamentoPag(Integer.parseInt(campoCar[10]));
				carP.setAnoPagamentoPag(Integer.parseInt(campoCar[11]));

				carPService.insertBackUp(carP);
				lineCarP = brCarP.readLine();
			}
			brCarP.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}

	public void compromisso() throws ParseException {
		status = "Ok";
		file = "Compromisso";
		path = unid + meioSgb + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
		Compromisso comp = new Compromisso();
		CompromissoService comService = new CompromissoService();
		countAk = 0;
		try { BufferedReader brCom = new BufferedReader(new FileReader(path));
			String lineCom = brCom.readLine();
			while (lineCom != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineCom);
				String[] campoCom = crip.split(" , ");
				
				comp.setIdCom(Integer.parseInt(campoCom[0]));
				comp.setCodigoFornecedorCom(Integer.parseInt(campoCom[1]));
				comp.setNomeFornecedorCom(campoCom[2]);
				comp.setNnfCom(Integer.parseInt(campoCom[3]));
				Date dtCom = sdfAno.parse(campoCom[4]);
				cal.setTime(dtCom);
				comp.setDataCom(cal.getTime());
				Date dtVen = sdfAno.parse(campoCom[5]);
				cal.setTime(dtVen);
				comp.setDataVencimentoCom(cal.getTime());
				comp.setValorCom(Double.parseDouble(campoCom[6]));
				comp.setParcelaCom(Integer.parseInt(campoCom[7]));
				comp.setPrazoCom(Integer.parseInt(campoCom[8]));
				comp.setFornecedor(forService.findById(Integer.parseInt(campoCom[9])));
				comp.setTipoFornecedor(tipoService.findById(Integer.parseInt(campoCom[10])));
				comp.setParPeriodo(perService.findById(Integer.parseInt(campoCom[10])));
				
				comService.insertBackUp(comp);
				lineCom = brCom.readLine();
			}
			brCom.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
	
	public void fornecedor() {
		status = "Ok";
		file = "Fornecedor";
		path = unid + meioSgb + file + ext;
		Fornecedor forn = new Fornecedor();
		FornecedorService forService = new FornecedorService();
		countAk = 0;
		try { BufferedReader brFor = new BufferedReader(new FileReader(path));
			String lineFor = brFor.readLine();
			while (lineFor != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineFor);
				String[] campoFor = crip.split(" , ");

				forn.setCodigo(Integer.parseInt(campoFor[0]));
				forn.setRazaoSocial(campoFor[1]);
				forn.setRua(campoFor[2]);
				forn.setNumero(Integer.parseInt(campoFor[3]));
				forn.setComplemento(campoFor[4]);
				forn.setBairro(campoFor[5]);
				forn.setCidade(campoFor[6]);
				forn.setUf(campoFor[7]);
				forn.setCep(campoFor[8]);
				forn.setDdd01(Integer.parseInt(campoFor[9]));
				forn.setTelefone01(Integer.parseInt(campoFor[10]));
				forn.setDdd02(Integer.parseInt(campoFor[11]));
				forn.setTelefone02(Integer.parseInt(campoFor[12]));
				forn.setContato(campoFor[13]);
				forn.setDddContato(Integer.parseInt(campoFor[14]));
				forn.setTelefoneContato(Integer.parseInt(campoFor[15]));
				forn.setEmail(campoFor[16]);
				forn.setPix(campoFor[17]);
				forn.setObservacao(campoFor[18]);
				forn.setPrazo(Integer.parseInt(campoFor[19]));
				forn.setParcela(Integer.parseInt(campoFor[20]));
				
				forService.insertBackUp(forn);
				lineFor = brFor.readLine();
			}
			brFor.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	public void parcela() throws ParseException {
		status = "Ok";
		file = "Parcela";
		path = unid + meioSgb + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
		Parcela par = new Parcela();
		ParcelaService parService = new ParcelaService();
		countAk = 0;
		try { BufferedReader brPar = new BufferedReader(new FileReader(path));
			String linePar = brPar.readLine();
			while (linePar != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(linePar);
				String[] campoPar = crip.split(" , ");

				par.setIdPar(Integer.parseInt(campoPar[0]));
				par.setCodigoFornecedorPar(Integer.parseInt(campoPar[1]));
				par.setNomeFornecedorPar(campoPar[2]);
				par.setNnfPar(Integer.parseInt(campoPar[3]));
				par.setNumeroPar(Integer.parseInt(campoPar[4]));
				Date dtVen = sdfAno.parse(campoPar[5]);
				cal.setTime(dtVen);
				par.setDataVencimentoPar(cal.getTime());
				par.setValorPar(Double.parseDouble(campoPar[6]));
				par.setDescontoPar(Double.parseDouble(campoPar[7]));
				par.setJurosPar(Double.parseDouble(campoPar[8]));
				par.setTotalPar(Double.parseDouble(campoPar[9]));
				par.setPagoPar(Double.parseDouble(campoPar[10]));
				Date dtPag = sdfAno.parse(campoPar[11]);
				cal.setTime(dtPag);
				par.setDataPagamentoPar(cal.getTime());
				par.setFornecedor(forService.findById(Integer.parseInt(campoPar[12])));
				par.setTipoFornecedor(tipoService.findById(Integer.parseInt(campoPar[13])));
				par.setPeriodo(perService.findById(Integer.parseInt(campoPar[14])));
				
				parService.insertBackUp(par);
				linePar = brPar.readLine();
			}
			brPar.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	
	public void periodo() throws ParseException {
		status = "Ok";
		file = "ParPeriodo";
		path = unid + meioSgb + file + ext;
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodo per = new ParPeriodo();
		ParPeriodoService perService = new ParPeriodoService();
		
		countAk = 0;
		try { BufferedReader brPer = new BufferedReader(new FileReader(path));
			String linePer = brPer.readLine();
			while (linePer != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(linePer);
				String[] campoPer = crip.split(" , ");
				per.setIdPeriodo(Integer.parseInt(campoPer[0]));
				Date dti = sdfAno.parse(campoPer[1]);
				cal.setTime(dti);
				per.setDtiPeriodo(cal.getTime());
				Date dtf = sdfAno.parse(campoPer[2]);
				cal.setTime(dtf);
				per.setDtfPeriodo(cal.getTime());
				per.setFornecedor(forService.findById(Integer.parseInt(campoPer[3])));
				per.setTipoConsumo(tipoService.findById(Integer.parseInt(campoPer[4])));
				perService.update(per);
				linePer = brPer.readLine();
			}
			brPer.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
			
	public void tipoConsumo() {
		status = "Ok";
		file = "TipoConsumidor";
		path = unid + meioSgb + file + ext;
		TipoConsumo tipo = new TipoConsumo();
		TipoConsumoService tipoService = new TipoConsumoService();
		countAk = 0;
		try { BufferedReader brTp = new BufferedReader(new FileReader(path));
			String lineTp = brTp.readLine();
			while (lineTp != null) {
				countAk += 1;
				String crip = Cryptograf.desCriptografa(lineTp);
				String[] campoTp = crip.split(" , ");

				tipo.setCodigoTipo(Integer.parseInt(campoTp[0]));
				tipo.setNomeTipo(campoTp[1]);

				tipoService.insertBackUp(tipo);
				lineTp = brTp.readLine();
			}
			brTp.close();
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {					
			gravaRestaura();
		}
	}
		
	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("FileUp"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("StatusUp"));
		tableColumnCount.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("CountUp")));
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewRestaura.prefHeightProperty().bind(stage.heightProperty());
 	}

 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
		List<Restaura> listR = service.findAll();
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
  		obsList = FXCollections.observableArrayList(listR);
  		tableViewRestaura.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		initializeNodes();
	}
	
 	public void gravaRestaura() {
		entity.setIdRestauraUp(null);
		entity.setFileUp(file);
		entity.setStatusUp(status);
		entity.setCountUp(countAk);
		service.saveOrUpdate(entity);
		count += 1;
		labelCount.setText(String.valueOf(count));
		labelCount.viewOrderProperty();
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "BackUp SGO ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			RestauraSgbFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unid para Restaurar                 ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 
}
