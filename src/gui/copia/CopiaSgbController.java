package gui.copia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.entities.Situacao;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.EmpresaService;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.LoginService;
import gui.sgbmodel.service.MesesService;
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
import gui.util.DataStaticSGB;
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

public class CopiaSgbController implements Initializable, DataChangeListener {
	
	@FXML
	private VBox vBoxBackUp;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Copia> tableViewBackUp;

 	@FXML
 	private TableColumn<Copia, Date> tableColumnDateIBackUp;

 	@FXML
 	private TableColumn<Copia, Date> tableColumnDateFBackUp;

 	@FXML
 	private TableColumn<Copia, String> tableColumnUserBackUp;

 	private ObservableList<Copia> obsList;

	int count = 0;
	String arq = "";
	String crip = "";
	Date dataI = new Date(System.currentTimeMillis());
	Date dataF = new Date();
    String classe = "BackUp SGB ";
	static String meioSgb = ":\\Arqs\\Backup\\SGB\\";
	public String meioSgcp= ":\\Arqs\\Backup\\SGBCP\\";
	public static String ext = ".Bck";
	public static String file = "";
	public static String unid = null;
	public static String path = null;
 	public String user = "usuário";			
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
 	
 	private CopiaService service;
 	private Copia entity;
 	private Copia entityDel;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setBackUpService(CopiaService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Copia entity) {
 		this.entity = entity;
 	}
 	
 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/CopiaForm.fxml", parentStage);
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() {
 		if (unid != null) { 
// 			Alerts.showAlert("Atenção", "isoo pode demorar um pouco", null, AlertType.WARNING);
 			backUp();
 			adiantamento();
 			anos();
 			cargo();
 			cartela();
 			cartelaPagante();
 			cartelaVirtual();
 			empresa();
 			entrada();
 			funcionario();
 			grupo();
 			login();
 			meses();
 			produto();
 			situacao();
 			compromisso();
 			fornecedor();
 			parcela();
 			periodo();
 			tipo();
 			limpaBackUp();
 			gravaBackUp();	
 	 		labelFile.setText("Kbou!!!");
 	 		labelFile.viewOrderProperty();
 	 		labelCount.setText(String.valueOf(count));
 		}	
 	}

	public void backUp() {
		file = "BackUp";
		path = unid + meioSgb + file + ext;
		CopiaService backService = new CopiaService();
		count = 0;
		arq = "";
		crip = "";
		List<Copia> listB = backService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Copia b: listB) {
				count += 1;
				arq = (b.getIdBackUp() + " | " + b.getDataIBackUp() + " | " + b.getUserBackUp() + " | " + b.getDataFBackUp());
				crip = Cryptograf.criptografa(arq);
//				System.out.println(crip);
//				String bc = Cryptograf.desCriptografa(crip);
//				System.out.println(crip);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void adiantamento() {
		file = "Adiantamento";
		path = unid + meioSgb + file + ext;
		AdiantamentoService adiService = new AdiantamentoService();
		CargoService carService = new CargoService();
		Cargo cargo = new Cargo();
		SituacaoService sitService = new SituacaoService();
		Situacao sit = new Situacao();
		List<Cargo> listC = carService.findAll();
		List<Situacao> listS = sitService.findAll();
		count = 0;
		arq = "";
		crip = "";
		List<Adiantamento> listA = adiService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Adiantamento a: listA) {
				count += 1;
				for (Cargo c : listC) {
					if (c.getNomeCargo().equals(a.getCargoFun())) {
						cargo = c;
						a.setCargo(cargo);
					}
				}
				for (Situacao s : listS) {
					if (s.getNomeSit().equals(a.getSituacaoFun())) {
						sit = s;
						a.setSituacao(sit);
					}
				}
				arq = (a.getNumeroAdi() + " | " + a.getDataAdi() + " | " + a.getValeAdi() + " | " + a.getMesAdi()
				 + " | " + a.getAnoAdi() + " | " + a.getValorCartelaAdi() + " | " + a.getCartelaAdi() + " | " + 
						a.getComissaoAdi() + " | " + a.getTipoAdi() + " | " + a.getSalarioAdi() + " | " + 
						a.getCodigoFun() + " | " + a.getNomeFun() + " | " + a.getMesFun() + " | " + a.getAnoFun() + " | " + 
						a.getCargoFun() + " | " + a.getSituacaoFun() + " | " + a.getCargo().getCodigoCargo() + " | " + 
						a.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arq);
//				System.out.println(crip);
//				String bc = Cryptograf.desCriptografa(crip);
//				System.out.println(crip);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void anos() {
		file = "Anos";
		path = unid + meioSgb + file + ext;
		AnosService anoService = new AnosService();
		count = 0;
		arq = "";
		crip = "";
		List<Anos> listA = anoService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Anos a: listA) {
				count += 1;
				arq = (a.getNumeroAnos() + " | " + a.getAnoAnos() + " | " + a.getAnoAnos());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cargo() {
		file = "Cargo";
		path = unid + meioSgb + file + ext;
		CargoService cartelaService = new CargoService();
		count = 0;
		arq = "";
		crip = "";
		List<Cargo> listC = cartelaService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cargo c: listC) {
				count += 1;
				arq = (c.getCodigoCargo() + " | " + c.getNomeCargo() + " | " + c.getSalarioCargo() + " | " + c.getComissaoCargo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cartela() {
		file = "Cartela";
		path = unid + meioSgb + file + ext;
		CartelaService cartelaService = new CartelaService();
		count = 0;
		arq = "";
		crip = "";
		List<Cartela> listC = cartelaService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Cartela c: listC) {
				count += 1;
				arq = (c.getNumeroCar() + " | " + c.getDataCar() + " | " + c.getLocalCar() + " | " + c.getDescontoCar()
				 	+ " | " + c.getTotalCar() + " | " + c.getSituacaoCar() + " | " + c.getNumeroPaganteCar() + " | " + 
						c.getValorPaganteCar() + " | " + c.getMesCar() + " | " + c.getAnoCar() + " | " + c.getObsCar()
						 + " | " + c.getServicoCar() + " | " + c.getValorServicoCar() + " | " + c.getSubTotalCar() + " | " + 
						c.getNomeSituacaoCar() + " | " + c.getMesPagCar() + " | " + c.getAnoPagCar());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cartelaPagante() {
		file = "CartelaPagante";
		path = unid + meioSgb + file + ext;
		CartelaPaganteService cartelaPagService = new CartelaPaganteService();
		count = 0;
		arq = "";
		crip = "";
		List<CartelaPagante> listC = cartelaPagService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(CartelaPagante c: listC) {
				count += 1;
				arq = (c.getNumeroCartelaPag() + " | " + c.getPaganteCartelaPag() + " | " + c.getDataCartelaPag() + " | " +
						c.getLocalCartelaPag() + " | " + c.getValorCartelaPag() + " | " + c.getFormaCartelaPag() + " | " + 
						c.getSituacaoCartelaPag() + " | " + c.getCartelaIdOrigemPag() + " | " + c.getMesCartelaPag() + " | " + 
						c.getAnoCartelaPag() + " | " + c.getMesPagamentoPag() + " | " + c.getAnoPagamentoPag());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cartelaVirtual() {
		file = "CartelaVirtual";
		path = unid + meioSgb + file + ext;
		CartelaVirtualService virService = new CartelaVirtualService();
		count = 0;
		arq = "";
		crip = "";
		List<CartelaVirtual> listV = virService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(CartelaVirtual v: listV) {
				count += 1;
				arq = (v.getNumeroVir() + " | " + v.getSituacaoVir()  + " | " + v.getFuncionario().getNomeFun() + " | " + 
						v.getProduto().getNomeProd() + " | " + v.getQuantidadeProdVir() + " | " + v.getPrecoProdVir() + " | " + 
						v.getVendaProdVir() + " | " + v.getTotalProdVir() + " | " + v.getOrigemIdCarVir() + " | " + 
						v.getFuncionario().getCodigoFun() + " | " + v.getProduto().getCodigoProd());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void empresa() {
		file = "Empresa";
		path = unid + meioSgb + file + ext;
		EmpresaService empService = new EmpresaService();
		count = 0;
		arq = "";
		crip = "";
		List<Empresa> listV = empService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Empresa e : listV) {
				count += 1;
				arq = (e.getNumeroEmp() + " | " + e.getNomeEmp() + " | " + e.getEnderecoEmp() + " | " + e.getTelefoneEmp()
				 	+ " | " + e.getEmailEmp() + " | " + e.getPixEmp());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void entrada() {
		file = "Entrada";
		path = unid + meioSgb + file + ext;
		EntradaService entService = new EntradaService();
		count = 0;
		arq = "";
		crip = "";
		List<Entrada> listE = entService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Entrada e : listE) {
				count += 1;
				arq = (e.getNumeroEnt() + " | " + e.getDataEnt() + " | " + e.getForn().getRazaoSocial() + " | " + 
						e.getProd().getNomeProd() + " | " + e.getQuantidadeProdEnt() + " | " + e.getValorProdEnt()
						 + " | " + 	e.getForn().getCodigo() + " | " + e.getProd().getCodigoProd());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void funcionario() {
		file = "Funcionario";
		path = unid + meioSgb + file + ext;
		FuncionarioService funService = new FuncionarioService();
		count = 0;
		arq = "";
		crip = "";
		List<Funcionario> listF = funService.findAll(2001, 01);
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Funcionario f : listF) {
				count += 1;
				arq = (f.getCodigoFun() + " | " + f.getNomeFun() + " | " + f.getEnderecoFun() + " | " + f.getBairroFun()
				 	+ " | " + f.getCidadeFun() + " | " + f.getUfFun() + " | " + f.getCepFun() + " | " + f.getDddFun()
				 	 + " | " + f.getTelefoneFun() + " | " + f.getCpfFun() + " | " + f.getPixFun() + " | "  + " | " +  
				 	 f.getComissaoFun() + " | " + f.getAdiantamentoFun() + " | " + f.getMesFun() + " | " + f.getAnoFun() 
				 	 + " | " + f.getCargoFun() + " | " + f.getSituacaoFun() + " | " + f.getSalarioFun() + " | " +  
				 	 f.getCargo().getCodigoCargo() + " | " + f.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void grupo() {
		file = "Grupo";
		path = unid + meioSgb + file + ext;
		GrupoService gruService = new GrupoService();
		count = 0;
		arq = "";
		crip = "";
		List<Grupo> listF = gruService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Grupo g : listF) {
				count += 1;
				arq = (g.getCodigoGru() + " | " + g.getNomeGru());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void login() {
		file = "Login";
		path = unid + meioSgb + file + ext;
		LoginService logService = new LoginService();
		count = 0;
		arq = "";
		crip = "";
		List<Login> listF = logService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Login l : listF) {
				count += 1;
				arq = (l.getNumeroLog() + " | " + l.getSenhaLog() + " | " + l.getNomeLog() + " | " + l.getNivelLog()
				 + " | " + l.getAlertaLog() + " | " + l.getDataLog() + " | " + l.getVencimentoLog() + " | " + 
						l.getMaximaLog() + " | " + l.getAcessoLog() + " | " + l.getEmpresaLog());
//				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(arq));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void meses() {
		file = "Meses";
		path = unid + meioSgb + file + ext;
		MesesService mesService = new MesesService();
		count = 0;
		arq = "";
		crip = "";
		List<Meses> listM = mesService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Meses m : listM) {
				count += 1;
				arq = (m.getNumeroMes() + " | " + m.getNomeMes());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void produto() {
		file = "Produto";
		path = unid + meioSgb + file + ext;
		ProdutoService prodService = new ProdutoService();
		count = 0;
		arq = "";
		crip = "";
		List<Produto> listP = prodService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Produto p : listP) {
				count += 1;
				arq = (p.getCodigoProd() + " | " + p.getGrupoProd() + " | " + p.getNomeProd() + " | " + p.getSaldoProd()
				 	+ " | " + p.getEstMinProd() + " | " + p.getPrecoProd() + " | " + p.getVendaProd() + " | " + 
						p.getCmmProd() + " | " + p.getSaidaCmmProd() + " | " + p.getDataCadastroProd() + " | " + 
						p.getGrupo().getCodigoGru() + " | " + p.getPercentualProd() + " | " + p.getLetraProd());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void situacao() {
		file = "Situacao";
		path = unid + meioSgb + file + ext;
		SituacaoService sitService = new SituacaoService();
		count = 0;
		arq = "";
		crip = "";
		List<Situacao> listS = sitService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Situacao s : listS) {
				count += 1;
				arq = (s.getNumeroSit() + " | " + s.getNomeSit());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void compromisso() {
		file = "Compromisso";
		path = unid + meioSgcp + file + ext;
		CompromissoService comService = new CompromissoService();
		count = 0;
		arq = "";
		crip = "";
		List<Compromisso> listC = comService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Compromisso c : listC) {
				count += 1;
				arq = (c.getIdCom() + " | " + c.getFornecedor().getCodigo() + " | " + c.getFornecedor().getRazaoSocial()
						 + " | " + c.getNnfCom() + " | " + c.getDataCom() + " | " + c.getDataVencimentoCom() + " | " + 
						c.getValorCom() + " | " + c.getParcelaCom() + " | " + c.getPrazoCom() + " | " + 
						 c.getFornecedor().getCodigo() + " | " + c.getTipoFornecedor().getCodigoTipo() + " | " + 
						c.getParPeriodo().getIdPeriodo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void fornecedor() {
		file = "Fornecedor";
		path = unid + meioSgcp + file + ext;
		FornecedorService forService = new FornecedorService();
		count = 0;
		arq = "";
		crip = "";
		List<Fornecedor> listF = forService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Fornecedor f : listF) {
				count += 1;
				arq = (f.getCodigo() + " | " + f.getRazaoSocial() + " | " + f.getRua() + " | " + 
						f.getNumero() + " | " + f.getComplemento() + " | " + f.getBairro() + " | " + f.getCidade()
						 + " | " + f.getUf() + " | " + f.getCep() + " | " + f.getDdd01() + " | " + f.getTelefone01()
						 + " | " + f.getDdd02() + " | " + f.getTelefone02() + " | " + f.getContato() + " | " + 
						 f.getDddContato() + " | " + f.getTelefoneContato() + " | " + f.getEmail() + " | " + f.getPix()
						 + " | " + f.getObservacao() + " | " + f.getPrazo() + " | " + f.getParcela());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void parcela() {
		file = "Parcela";
		path = unid + meioSgcp + file + ext;
		ParcelaService parService = new ParcelaService();
		count = 0;
		arq = "";
		crip = "";
		List<Parcela> listP = parService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(Parcela p : listP) {
				count += 1;
				arq = (p.getIdPar() + " | " + p.getCodigoFornecedorPar() + " | " + p.getNomeFornecedorPar() + " | " + 
						p.getNnfPar() + " | " + p.getNumeroPar() + " | " + p.getDataVencimentoPar() + " | " + 
						p.getValorPar() + " | " + p.getDescontoPar() + " | " + p.getJurosPar() + " | " + p.getTotalPar()
						 + " | " + p.getPagoPar() + " | " + p.getDataPagamentoPar() + " | " + p.getFornecedor().getCodigo()
						 + " | " + p.getTipoFornecedor().getCodigoTipo() + " | " + p.getPeriodo().getIdPeriodo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void periodo() {
		file = "ParPeriodo";
		path = unid + meioSgcp + file + ext;
		ParPeriodoService perService = new ParPeriodoService();
		count = 0;
		arq = "";
		crip = "";
		List<ParPeriodo> listP = perService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(ParPeriodo p : listP) {
				count += 1;
				arq = (p.getIdPeriodo() + " | " + p.getDtiPeriodo() + " | " + p.getDtfPeriodo() + " | " + 
						p.getFornecedor().getCodigo() + " | " + p.getTipoConsumo().getCodigoTipo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void tipo() {
		file = "TipoConsumidor";
		path = unid + meioSgcp + file + ext;
		TipoConsumoService tipoService = new TipoConsumoService();
		count = 0;
		arq = "";
		crip = "";
		List<TipoConsumo> listT = tipoService.findAll();
		try {BufferedWriter bwC = new BufferedWriter(new FileWriter(path));
			for(TipoConsumo t : listT) {
				count += 1;
				arq = (t.getCodigoTipo() + " | " + t.getNomeTipo());
				crip = Cryptograf.criptografa(arq);
				bwC.write(Cryptograf.criptografa(crip));
				bwC.newLine();
			}
			bwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnDateIBackUp.setCellValueFactory(new PropertyValueFactory<>("DataIBackUp"));
		tableColumnUserBackUp.setCellValueFactory(new PropertyValueFactory<>("UserBackUp"));
		tableColumnDateFBackUp.setCellValueFactory(new PropertyValueFactory<>("DataFBackUp"));
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewBackUp.prefHeightProperty().bind(stage.heightProperty());
 	}

 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
 		List<Copia> list = new ArrayList<>();
		list = service.findAll();
  		obsList = FXCollections.observableArrayList(list);
  		tableViewBackUp.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	public void limpaBackUp() {
		LocalDate dt1 = DataStaticSGB.dateParaLocal(dataI);
		int ano1 = DataStaticSGB.anoDaData(dt1);
		int mes1 = DataStaticSGB.mesDaData(dt1);
		int dia1 = DataStaticSGB.diaDaData(dt1);
		
		List<Copia> listLimpa = service.findAll();
		for (Copia b : listLimpa) {
			LocalDate dtB = DataStaticSGB.converteTimeFormataString(b.getDataIBackUp());
			int anoB = DataStaticSGB.anoDaData(dtB);
			int mesB = DataStaticSGB.mesDaData(dtB);
			int diaB = DataStaticSGB.diaDaData(dtB);

			if(ano1 == anoB && mes1 == mesB && dia1 == diaB) {
				service.remove(b.getIdBackUp());
			} else {
				if (anoB < ano1) {
					service.remove(b.getIdBackUp());
				} else {
					if (mesB < mes1) {
						service.remove(b.getIdBackUp());
					}	
				}	
			}			
		}
	}
	
 	public void gravaBackUp() {
		String dti = sdf.format(dataI); 
		entity.setIdBackUp(null);
		entity.setDataIBackUp(dti);
		entity.setUserBackUp(user);
		dataF = new Date(System.currentTimeMillis());
		String dtf = sdf.format(dataF);
		entity.setDataFBackUp(dtf);
		entityDel = entity;
		service.saveOrUpdate(entity);
		if (entity.getDataIBackUp() == entityDel.getDataIBackUp()) {
			service.remove(entity.getIdBackUp());
		}
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "BackUp SGB ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			CopiaSgbFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unidade para BackUp                 ");
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

