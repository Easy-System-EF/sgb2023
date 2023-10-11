package gui.copia;

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
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Cliente;
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
import gui.sgbmodel.service.ClienteService;
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
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
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

 	@FXML
 	private TableColumn<Copia, String> tableColumnUnidBackUp;

 	private ObservableList<Copia> obsList;

	int count = 0;
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
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	int grw = 0;
 	
 	private CopiaService service;
 	private Copia entity;
 	
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
 			count = 0; 			
 			grw = 0;
 			adiantamento();
 			cargo();
 			cliente();
 			cartela();
 			cartelaPagante();
 			cartelaVirtual();
 			entrada();
 			funcionario();
 			grupo();
 			login();
 			produto();
 			situacao();
 			compromisso();
 			fornecedor();
 			parcela();
 			tipo();
 			limpaBackUp();
 			gravaBackUp();	
 	 		labelFile.setText("Kbou!!!");
 	 		labelFile.viewOrderProperty();
 	 		labelCount.setText(String.valueOf(count));
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
		String arqAdi = "";
		crip = "";

		List<Adiantamento> listA = adiService.findAll();
		try {FileWriter fwA = new FileWriter(path);
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
				String data = sdfAno.format(a.getDataAdi());
				arqAdi = (" ADI " + a.getNumeroAdi() + " , " + data + " , " + a.getValeAdi() + " , " + a.getMesAdi()
				 + " , " + a.getAnoAdi() + " , " + a.getValorCartelaAdi() + " , " + a.getCartelaAdi() + " , " + 
						a.getComissaoAdi() + " , " + a.getTipoAdi() + " , " + a.getSalarioAdi() + " , " + 
						a.getCodigoFun() + " , " + a.getNomeFun() + " , " + a.getMesFun() + " , " + a.getAnoFun() + " , " + 
						a.getCargoFun() + " , " + a.getSituacaoFun() + " , " + a.getSalarioFun()  + " , " +  
						a.getCargo().getCodigoCargo() + " , " + a.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arqAdi);
				fwA.write(crip);
			}
			fwA.close();
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
		CargoService cargoService = new CargoService();
		String arqCargo = null;
		crip = "";
		
		List<Cargo> listCargo = cargoService.findAll();
		try {FileWriter fwC = new FileWriter(path);
			for(Cargo c: listCargo) {
				count += 1;
				arqCargo = (" CARGO " + c.getCodigoCargo() + " , " + c.getNomeCargo() + " , " + c.getSalarioCargo() + " , " + 
						c.getComissaoCargo() + " ; ");
				crip = Cryptograf.criptografa(arqCargo);
				fwC.write(crip);
				fwC.flush();
			}
			fwC.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void cliente() {
		file = "Cliente";
		path = unid + meioSgb + file + ext;
		ClienteService cliService = new ClienteService();
		String arqCliente = null;
		crip = "";
		
		List<Cliente> listCliente = cliService.findAll();
		try {FileWriter fwCl = new FileWriter(path);
			for(Cliente cl: listCliente) {
				count += 1;
				arqCliente = (" CLIENTE " + cl.getCodigoCli() + " , " + cl.getNomeCli() + " , " + cl.getDddCli() + " , " + 
						cl.getTelefoneCli() + " , " + cl.getConvenioCli() + " ; ");

				crip = Cryptograf.criptografa(arqCliente);
				fwCl.write(crip);
				fwCl.flush();
			}
			fwCl.close();
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
		String arqCart = "";
		crip = "";
		List<Cartela> listCt = cartelaService.findAll();
		try {FileWriter fwCt = new FileWriter(path);
			for(Cartela c: listCt) {
				count += 1;
				String dataCar = sdfAno.format(c.getDataCar());
				arqCart = (" CARTELA " + c.getNumeroCar() + " , " + dataCar + " , " + c.getLocalCar() + " , " + c.getDescontoCar()
				 	+ " , " + c.getTotalCar() + " , " + c.getSituacaoCar() + " , " + c.getNumeroPaganteCar() + " , " + 
						c.getValorPaganteCar() + " , " + c.getMesCar() + " , " + c.getAnoCar() + " , " + c.getObsCar()
						 + " , " + c.getServicoCar() + " , " + c.getValorServicoCar() + " , " + c.getSubTotalCar() + " , " + 
						c.getNomeSituacaoCar() + " , " + c.getMesPagCar() + " , " + c.getAnoPagCar());
				crip = Cryptograf.criptografa(arqCart);
				fwCt.write(crip);
			}
			fwCt.close();
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
		String arqCarP = "";
		crip = "";
		List<CartelaPagante> listCp = cartelaPagService.findAll();
		try {FileWriter fwCp = new FileWriter(path);
			for(CartelaPagante cp: listCp) {
				count += 1;
				String dataCarPag = sdfAno.format(cp.getDataCartelaPag());
				arqCarP = (" CARTELAPAGANTE " + cp.getNumeroCartelaPag() + " , " + cp.getPaganteCartelaPag() + " , " + dataCarPag + " , " +
						cp.getLocalCartelaPag() + " , " + cp.getValorCartelaPag() + " , " + cp.getFormaCartelaPag() + " , " + 
						cp.getSituacaoCartelaPag() + " , " + cp.getCartelaIdOrigemPag() + " , " + cp.getMesCartelaPag() + " , " + 
						cp.getAnoCartelaPag() + " , " + cp.getMesPagamentoPag() + " , " + cp.getAnoPagamentoPag());
				crip = Cryptograf.criptografa(arqCarP);
				fwCp.write(crip);
			}
			fwCp.close();
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
		String arqVir = null;
		crip = "";
		List<CartelaVirtual> listV = virService.findAll();
		try {FileWriter fwCv = new FileWriter(path);
			for(CartelaVirtual v: listV) {
				count += 1;
				
				arqVir = (" CARTELAVIRTUAL " + v.getNumeroVir() + " , " + v.getLocalVir() + " , " + v.getSituacaoVir()  + " , " + 
						v.getFuncionario().getNomeFun() + " , " + v.getProduto().getNomeProd() + " , " + 
						v.getQuantidadeProdVir() + " , " + v.getPrecoProdVir() + " , " + v.getVendaProdVir() + " , " + 
						v.getTotalProdVir() + " , " + v.getOrigemIdCarVir() + " , " + v.getFuncionario().getCodigoFun() 
						+ " , " + v.getProduto().getCodigoProd());
				crip = Cryptograf.criptografa(arqVir);
				fwCv.write(crip);
			}
			fwCv.close();
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
		String arqEnt = null;
		crip = "";
		List<Entrada> listE = entService.findAll();
		try {FileWriter fwE = new FileWriter(path);
			for(Entrada e : listE) {
				count += 1;
				String dataEnt = sdfAno.format(e.getDataEnt());
				arqEnt = (" ENTRADA " + e.getNumeroEnt() + " , " + e.getNnfEnt() + " , " + dataEnt + " , " + 
						e.getForn().getRazaoSocial() + " , " + e.getProd().getNomeProd() + " , " + e.getQuantidadeProdEnt() 
						+ " , " + e.getValorProdEnt() + " , " + 	e.getForn().getCodigo() + " , " + e.getProd().getCodigoProd());

				crip = Cryptograf.criptografa(arqEnt);
				fwE.write(crip);
			}
			fwE.close();
		}
		catch(	IOException e2) {
			e2.getMessage();	 			
		}
		finally {					
		}
	}
	
	public void funcionario() {
		LocalDate dataHj = LocalDate.now();
		int ano = DataStaticSGB.anoDaData(dataHj);
		int mes = DataStaticSGB.mesDaData(dataHj);
		file = "Funcionario";
		path = unid + meioSgb + file + ext;
		FuncionarioService funService = new FuncionarioService();
		String arqFun = null;
		crip = "";
		List<Funcionario> listF = funService.findAll(ano, mes);
		try {FileWriter fwF = new FileWriter(path);
			for(Funcionario f : listF) {
				count += 1;
				arqFun = (" FUNCIONARIO " + f.getCodigoFun() + " , " + f.getNomeFun() + " , " + f.getEnderecoFun() 
					+ " , " + f.getBairroFun() + " , " + f.getCidadeFun() + " , " + f.getUfFun() + " , " + f.getCepFun()
					+ " , " + f.getDddFun() + " , " + f.getTelefoneFun() + " , " + f.getCpfFun() + " , " + f.getPixFun() 
					+ " , " + f.getComissaoFun() + " , " + f.getAdiantamentoFun() + " , " + f.getMesFun() + " , " + f.getAnoFun() 
				 	 + " , " + f.getCargoFun() + " , " + f.getSituacaoFun() + " , " + f.getSalarioFun() + " , " +  
				 	 f.getCargo().getCodigoCargo() + " , " + f.getSituacao().getNumeroSit());
				crip = Cryptograf.criptografa(arqFun);
				fwF.write(crip);
			}
			fwF.close();
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
		String arqGru = null;
		crip = "";
		List<Grupo> listGrupo = gruService.findAll();
		try {FileWriter fwG = new FileWriter(path);
			for(Grupo g : listGrupo) {
				count += 1;
				arqGru = (" GRUPO " + g.getCodigoGru() + " , " + g.getNomeGru());
				crip = Cryptograf.criptografa(arqGru);
				fwG.write(crip);
			}
			fwG.close();
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
		String arqLog = null;
		crip = "";
		List<Login> listL = logService.findAll();
		try {FileWriter fwL = new FileWriter(path);
			for(Login l : listL) {
				count += 1;
				String dataLog = sdfAno.format(l.getDataLog());
				String dataVen = sdfAno.format(l.getVencimentoLog());
				String dataMax = sdfAno.format(l.getMaximaLog());
				String dataAce = sdfAno.format(l.getAcessoLog());

				arqLog = (" LOGIN " + l.getNumeroLog() + " , " + l.getSenhaLog() + " , " + l.getNomeLog() + " , " + l.getNivelLog()
				 + " , " + l.getAlertaLog() + " , " + dataLog + " , " + dataVen + " , " + 
						dataMax + " , " + dataAce + " , " + l.getEmpresaLog());
				crip = Cryptograf.criptografa(arqLog);
				fwL.write(crip);
			}
			fwL.close();
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
		String arqPro = null;
		crip = "";
		List<Produto> listP = prodService.findAll();
		try {FileWriter fwP = new FileWriter(path);
			for(Produto p : listP) {
				count += 1;
				String dataCad = sdfAno.format(p.getDataCadastroProd());
				arqPro = (" PRODUTO " + p.getCodigoProd() + " , " + p.getGrupoProd() + " , " + p.getNomeProd() + " , " + p.getSaldoProd()
				 	+ " , " + p.getEstMinProd() + " , " + p.getPrecoProd() + " , " + p.getVendaProd() + " , " + 
						p.getCmmProd() + " , " + p.getSaidaCmmProd() + " , " + dataCad + " , " + 
						p.getGrupo().getCodigoGru() + " , " + p.getPercentualProd() + " , " + p.getLetraProd());
				crip = Cryptograf.criptografa(arqPro);
				fwP.write(crip);
			}
			fwP.close();
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
		String arqSit = null;
		crip = "";
		FileWriter fwS = null;
		List<Situacao> listS = sitService.findAll();
		try {fwS = new FileWriter(path);
			for(Situacao s : listS) {
				count += 1;
				arqSit = (" SIT " + s.getNumeroSit() + " , " + s.getNomeSit() + " ; ");
				crip = Cryptograf.criptografa(arqSit);
				fwS.write(crip);
				fwS.flush();
			}
			fwS.close();
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
		String arqCom = null;
		crip = "";
		List<Compromisso> listCm = comService.findAll();
		try {FileWriter fwCm = new FileWriter(path);
			for(Compromisso cm : listCm) {
				count += 1;
				String dataCom = sdfAno.format(cm.getDataCom());
				String dataVen = sdfAno.format(cm.getDataVencimentoCom());
				arqCom = (" COMPROMISSO " + cm.getIdCom() + " , " + cm.getFornecedor().getCodigo() + " , " + cm.getFornecedor().getRazaoSocial()
						 + " , " + cm.getNnfCom() + " , " + dataCom + " , " + dataVen + " , " + 
						cm.getValorCom() + " , " + cm.getParcelaCom() + " , " + cm.getPrazoCom() + " , " + 
						 cm.getFornecedor().getCodigo() + " , " + cm.getTipoFornecedor().getCodigoTipo() + " , " + 
						cm.getParPeriodo().getIdPeriodo());				
				crip = Cryptograf.criptografa(arqCom);
				fwCm.write(crip);
			}
			fwCm.close();
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
		String arqFor = null;
		crip = "";
		List<Fornecedor> listFo = forService.findAll();
		try {FileWriter fwFo = new FileWriter(path);
			for(Fornecedor fo : listFo) {
				count += 1;
				arqFor = (" FORNECEDOR " + fo.getCodigo() + " , " + fo.getRazaoSocial() + " , " + fo.getRua() + " , " + 
						fo.getNumero() + " , " + fo.getComplemento() + " , " + fo.getBairro() + " , " + fo.getCidade()
						 + " , " + fo.getUf() + " , " + fo.getCep() + " , " + fo.getDdd01() + " , " + fo.getTelefone01()
						 + " , " + fo.getDdd02() + " , " + fo.getTelefone02() + " , " + fo.getContato() + " , " + 
						 fo.getDddContato() + " , " + fo.getTelefoneContato() + " , " + fo.getEmail() + " , " + fo.getPix()
						 + " , " + fo.getObservacao() + " , " + fo.getPrazo() + " , " + fo.getParcela());
				crip = Cryptograf.criptografa(arqFor);
				fwFo.write(crip);
			}
			fwFo.close();
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
		String arqPar = "";
		crip = "";
		List<Parcela> listPa = parService.findAll();
		try {FileWriter fwPa = new FileWriter(path);
			for(Parcela pa : listPa) {
				count += 1;
				String dataVen = sdfAno.format(pa.getDataVencimentoPar());
				String dataPag = sdfAno.format(pa.getDataPagamentoPar());
				arqPar = (" PARCELA " + pa.getIdPar() + " , " + pa.getCodigoFornecedorPar() + " , " + pa.getNomeFornecedorPar() + " , " + 
						pa.getNnfPar() + " , " + pa.getNumeroPar() + " , " + dataVen + " , " + 
						pa.getValorPar() + " , " + pa.getDescontoPar() + " , " + pa.getJurosPar() + " , " + pa.getTotalPar()
						 + " , " + pa.getPagoPar() + " , " + dataPag + " , " + pa.getFornecedor().getCodigo()
						 + " , " + pa.getTipoFornecedor().getCodigoTipo() + " , " + pa.getPeriodo().getIdPeriodo());
				crip = Cryptograf.criptografa(arqPar);
				fwPa.write(crip);
			}
			fwPa.close();
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
		String arqTipo = "";
		crip = "";
		List<TipoConsumo> listT = tipoService.findAll();
		try {FileWriter fwT = new FileWriter(path);
			for(TipoConsumo t : listT) {
				count += 1;
				arqTipo = (" TIPO " + t.getCodigoTipo() + " , " + t.getNomeTipo());
				crip = Cryptograf.criptografa(arqTipo);
				fwT.write(crip);
			}
			fwT.close();
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
		tableColumnUnidBackUp.setCellValueFactory(new PropertyValueFactory<>("UnidadeBackUp"));
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
			int countM = 0;
			int countY = 0;

			if(ano1 == anoB && mes1 == mesB && dia1 == diaB && b.getUnidadeBackUp().equals(unid)) {
				service.remove(b.getIdBackUp());
			} else {
				if (anoB < ano1) {
					countY += 1;
					if (countY > 1) {
						service.remove(b.getIdBackUp());
					}	
				} else {
					if (mesB < mes1) {
						countM += 1;
						if (countM > 1) {
							service.remove(b.getIdBackUp());
						}	
					}	
				}	
			}			
		}
	}
	
 	public void gravaBackUp() {
 		grw += 1;
		String dti = sdf.format(dataI); 
		entity.setIdBackUp(null);
		entity.setDataIBackUp(dti);
		entity.setUserBackUp(user);
		dataF = new Date(System.currentTimeMillis());
		String dtf = sdf.format(dataF);
		entity.setDataFBackUp(dtf);
		entity.setUnidadeBackUp(unid);
		service.saveOrUpdate(entity);
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

