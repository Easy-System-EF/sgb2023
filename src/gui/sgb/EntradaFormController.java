package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgcp.FornecedorFormController;
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
import gui.util.CalculaParcela;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class EntradaFormController implements Initializable {

	private Entrada entity;
	private Entrada entityAnterior;
	private Produto prod;
	private Produto prodAnt;
	private Compromisso entCom;
	private ParPeriodo entPer;
	private Fornecedor entFor;
	private TipoConsumo entTip;
 
	/*
	 * dependencia service com metodo set
	 */
	private EntradaService service;
	private FornecedorService fornService;
	private ProdutoService prodService;
	private CompromissoService comService;
	private TipoConsumoService tipoService;
	private ParPeriodoService perService;
	private ParcelaService parService;
 
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroEnt;

	@FXML
	private DatePicker dpDataEnt;

	@FXML
	private TextField textNnfEnt;

	@FXML
	private TextField textIniciaisForn;
	
	@FXML
	private ComboBox<Fornecedor> comboBoxFornEnt;

	@FXML
	private TextField textIniciaisProd;
	
	@FXML
	private ComboBox<Produto> comboBoxProdEnt;

	@FXML
	private TextField textQtdProdEnt;

	@FXML
	private TextField textVlrProdEnt;

	@FXML
	private Label labelTotVlrProdEnt;

	@FXML
	private Button btSaveEnt;

	@FXML
	private Button btCancelEnt;

	@FXML
	private Button btSaiEnt;

	@FXML
	private Button btPesqForn;
	
	@FXML
	private Button btPesqProd;
	
	@FXML
	private Label labelErrorDataEnt;

	@FXML
	private Label labelErrorNnfEnt;
	
	@FXML
	private Label labelErrorNomeProd;

	@FXML
	private Label labelErrorNomeForn;

	@FXML
	private Label labelErrorQtdProdEnt;

	@FXML
	private Label labelErrorVlrProdEnt;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Entrada Form";
 	String pesquisaForn = "";
 	String pesquisaProd = "";
 	String totM = "";
// auxiliares	
	double tot = 0.00;
	double totAnt = 0.00;
	int flagN = 0;
	int flagD = 0;
	int flagAvisoII = 0;
 	int prz = 0;
 	int tam = 999999;
 	int i = 0;
 	Integer codigoFor[] = new Integer[tam]; 
 	Integer codigoNnf[] = new Integer[tam];
	private ObservableList<Fornecedor> obsListForn;
	private ObservableList<Produto> obsListProd;

	public void setPesquisa(String nome) {
		this.pesquisaProd = nome;
	}
	
	public void setObjects(Entrada entity, Produto prod, Compromisso entCom, ParPeriodo entPer, Parcela entPar, 
			Fornecedor entfor, TipoConsumo entTip) {
		this.entity = entity;
		this.prod = prod;
		this.entCom = entCom;
		this.entPer = entPer;
		this.entFor = entfor;
		this.entTip = entTip;
	}
	
	public void setServices(EntradaService service, FornecedorService fornService, ProdutoService prodService, 
			CompromissoService comService, TipoConsumoService tipoService, ParPeriodoService perService,
			ParcelaService parService) {
		this.service = service;
		this.fornService = fornService;
		this.prodService = prodService;
		this.comService = comService;
		this.tipoService = tipoService;
		this.perService = perService;
		this.parService = parService;
	}

//  * o controlador tem uma lista de eventos q permite distribuiééo via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (lé em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> néo foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, é sé notificar (juntar) recebe lé
	 * no listController
	 */
	@FXML
	public void onBtSaveEntAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sai = 0;
			if (entity.getNumeroEnt() != null) {
				sai = 1;
			}
			entity = getFormData();
			acertaProduto();
			classe = "Entrada Form ";
			
 			if (flagN == 0) {
 				prodService.saveOrUpdate(prod);
 				if (!entity.getNomeFornEnt().equals("Próprio")) {
 					entity.setProd(prod);
 					service.saveOrUpdate(entity);
 					if (codigoFor[i] != entity.getForn().getCodigo()) {
 						if (codigoNnf[i] != entity.getNnfEnt()) {
 							i += 1;
 							codigoFor[i] = entity.getForn().getCodigo();
 							codigoNnf[i] = entity.getNnfEnt();
 							tam = i;
 						}	
					}	
				}	
			}
 			entity = new Entrada();
 			notifyDataChangeListerners();
 			updateFormData();
 			if (sai == 1) {
 				if (tam > 0 && tam < 999999) {
 					compromissoCreate();
 				}	
 				Utils.currentStage(event).close();
 			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		} 
	}

	private void acertaProduto() {
		classe = "Produto Ent Form ";
		if (flagN == 0) {
			if (entity.getValorProdEnt() >= prod.getVendaProd()) {
				Optional<ButtonType> result = Alerts.showConfirmation("Atenção!!!", "Custo!!! verifique o valor de venda");
				if (result.get() == ButtonType.CANCEL) {
					flagN = 1;
				}	else {
					prod.setPrecoProd(entity.getValorProdEnt());
				}
			} 	
			if (flagN == 0) {
				if (entityAnterior != null) {
					if (prod.getCodigoProd() == (entityAnterior.getProd().getCodigoProd())) {
						prod.saidaSaldo(entityAnterior.getQuantidadeProdEnt());
						prod.calculaCmm();
					} else {
						prodAnt = prodService.findById(entity.getProd().getCodigoProd());
						prodAnt.saidaSaldo(entityAnterior.getQuantidadeProdEnt());
						prodAnt.calculaCmm();
						prodService.saveOrUpdate(prodAnt);
					}
				}	
				prod.entraSaldo(entity.getQuantidadeProdEnt());
				if(prod.getSaldoProd() < 0.01) {
					Alerts.showAlert("Saldo", "saldo negativo e/ou nulo", prod.getNomeProd(), AlertType.ERROR);
					flagN = 1;
				} 
			}
		}
	}	

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se néo for atz tb verificamos se cpos
	 * obrigatérios estéo preenchidos, para informar erro(s) para cpos string néo
	 * precisa tryParse
	 */
	private Entrada getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
		Entrada obj = new Entrada();
		// instanciando uma exceééo, mas néo lanéado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroEnt(Utils.tryParseToInt(textNumeroEnt.getText()));
// tst name (trim elimina branco no principio ou final
// lanéa Erros - nome do cpo e msg de erro

		if (dpDataEnt.getValue() != null) {
			Instant instant = Instant.from(dpDataEnt.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEnt(Date.from(instant));
		}
		if (dpDataEnt.getValue() == null) {
			exception.addErros("data", "Data é obrigatória");
		}
		
		obj.setNnfEnt(Utils.tryParseToInt(textNnfEnt.getText()));
		if (textNnfEnt.getText() == null || textNnfEnt.getText().trim().contentEquals("")) {
			exception.addErros("nnf", "Nota fiscal é obrigatória");
		} else {
			entity.setNnfEnt(obj.getNnfEnt());
		}

		obj.setForn(comboBoxFornEnt.getValue());
		obj.setNomeFornEnt(comboBoxFornEnt.getValue().getRazaoSocial());
		if (obj.getNomeFornEnt() == null) {
			exception.addErros("forn", "Fornecedor é obrigatório");			
		}
		
		prod = (comboBoxProdEnt.getValue());
		obj.setProd(comboBoxProdEnt.getValue());
		obj.setNomeProdEnt(comboBoxProdEnt.getValue().getNomeProd());
		
		if (textVlrProdEnt.getText() == null || 
				textVlrProdEnt.getText() == "0.0" ||
				textVlrProdEnt.getText().trim().contentEquals("")) {
			exception.addErros("vlr", "Preço é obrigatório");
		} else {
			textVlrProdEnt.getText().replace(".", "");
			obj.setValorProdEnt(Utils.formatDecimalIn(textVlrProdEnt.getText()));
		}
		
		if (obj.getValorProdEnt() == 0.00) {
			exception.addErros("vlr", "Preço é obrigatório");			
		} else {
			prod.setPrecoProd(obj.getValorProdEnt());
		}	

		if (textQtdProdEnt.getText() == null || textQtdProdEnt.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} else {
			textQtdProdEnt.getText().replace(".", "");
			obj.setQuantidadeProdEnt(Utils.formatDecimalIn(textQtdProdEnt.getText())); 	 		
		}
		
		if (obj.getQuantidadeProdEnt() == 0.00 || obj.getQuantidadeProdEnt() == null) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} 

		tot = 0.0;
		tot = obj.getQuantidadeProdEnt() * obj.getValorProdEnt();
		if (tot != totAnt) {
			totM = Mascaras.formataValor(tot);
			totAnt = tot;
			exception.addErros("confirma", "confirma");
		}	
  				
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	@FXML
	public void onBtPesqFornAction(ActionEvent event) {
		classe = "Fornecedor Ent Form";
		pesquisaForn = "";
		try {
	  		pesquisaForn = textIniciaisForn.getText().toUpperCase().trim();
	  		if (pesquisaForn != "") {
	  			List<Fornecedor> listFor = fornService.findPesquisa(pesquisaForn);
				if (listFor.size() == 0) { 
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Fornecedor obj = new Fornecedor();
		 		 		 createDialogForn(obj, "/gui/sgcp/FornecedorForm.fxml", parentStage);
		 		  	}
					listFor = fornService.findPesquisa(pesquisaForn);
			 	}
				pesquisaForn = "";
	  			obsListForn = FXCollections.observableArrayList(listFor);
	  			comboBoxFornEnt.setItems(obsListForn);
	  			notifyDataChangeListerners();
	 			updateFormData();
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	@FXML
	public void onBtPesqProdAction(ActionEvent event) {
		classe = "Produto Ent Form";
		try {
	  		pesquisaProd = textIniciaisProd.getText().toUpperCase().trim();
			if (pesquisaProd != "") {			
				List<Produto> listProd0 = prodService.findPesquisa(pesquisaProd);
				listProd0 = prodService.findPesquisa(pesquisaProd);
				obsListProd = FXCollections.observableArrayList(listProd0);
			} else {	
				pesquisaProd = textIniciaisProd.getText().toUpperCase().trim();
				if (pesquisaProd != "") {
					List<Produto> listProd = prodService.findPesquisa(pesquisaProd);
					if (listProd.size() == 0) { 
						Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
						if (result.get() == ButtonType.OK) {
							Stage parentStage = Utils.currentStage(event);
							Produto obj = new Produto();
							createDialogProd(obj, "/gui/sgb/ProdutoForm.fxml", parentStage);
						}
						listProd = prodService.findPesquisa(pesquisaProd);
			  			obsListProd = FXCollections.observableArrayList(listProd);
					}
				}		
	  		}	
			pesquisaProd = "";
  			comboBoxProdEnt.setItems(obsListProd);
			pesquisaProd = "";
  			notifyDataChangeListerners();
 			updateFormData();
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelEntAction(ActionEvent event) {
		if (tam > 0 && tam < 999999) {
			compromissoCreate();
		}	
		if (flagAvisoII == 1) {
			Alerts.showAlert("Atenção", "Edite seu Contas a Pagar", "Para conferir: Tipo de Consumo, "
					+ " data de vencimento e parcela(s)", AlertType.WARNING);
			flagAvisoII += 1;
		}
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtSaiEntAction(ActionEvent event) {
		if (tam > 0 && tam < 999999) {
			compromissoCreate();
		}	
		if (flagAvisoII == 1) {
			Alerts.showAlert("Atenção", "Edite seu Contas a Pagar", "Para conferir: Tipo de Consumo, "
					+ " data de vencimento e parcela(s)", AlertType.WARNING);
			flagAvisoII += 1;
		}
		Utils.currentStage(event).close();
	}

	private void compromissoCreate() {
		classe = "Entrada Form";
		tam += 1;
		Integer[] codent = new Integer[tam];
		Double[] vlrent = new Double[tam];
		Integer[] codcom = new Integer[tam];

/*
 * cou conferir quais os regs para esse fornecedor e nf guardando o valor para comferir compromisso		
 */
		for (i = 1 ; i < tam ; i++) {
			if (codigoFor[i] != null) {
				List<Entrada> listEntVlr = service.findByNnf(codigoNnf[i]);
				double vlr = 0.00;
				for (Entrada e1 : listEntVlr) {
					if (e1.getNnfEnt().equals(codigoNnf[i])) {
						if (e1.getForn().getCodigo().equals(codigoFor[i])) {
							vlr += e1.getQuantidadeProdEnt() * e1.getValorProdEnt();
							vlrent[i] = vlr;
							codent[i] = e1.getNumeroEnt();
						}
					}	
				}
			}	
/*
 * vou conferir se ja existe o compromisso incluido ou alterado			
 */
			classe = "Compromisso Ent Form";
			for (i = 1 ; i < tam ; i++) {
				if (codigoFor[i] != null || codigoFor[i] > 0) {
					entCom = comService.findById(codigoFor[i], codigoNnf[i]);
					if (entCom != null) {
						if (entCom.getNnfCom().equals(codigoNnf[i]) && entCom.getCodigoFornecedorCom().equals(codigoFor[i])) {
 						    codcom[i] = 999999999;
							if (!entCom.getValorCom().equals(vlrent[i])) {								
/*
 * se o valor não for igual (houve alteração), vou conferir se não há parcela(s) paga(s)
 * se houver, ja setei o codcom com 999999999 (invalido) e mando aviso
 * se não houver, vou pegar o codigo do compromisso 								
 */
			   					codcom[i] = entCom.getIdCom();						
			   					flagD = conferePagamento(flagD, entCom);
								if (flagD > 0 ) {
									codcom[i] = 999999999;
									Alerts.showAlert("Atenção!!!   Verifique Contas a Pagar", "Forn.: " + 
										entCom.getFornecedor().getRazaoSocial() + " " + " - Nnf: " + 
										entCom.getNnfCom(), "Valor não confere!!! ", AlertType.ERROR);
				   				} 
							}
						} 
					}	
				}	
			}
		}	

/*
 * se o nnf for maior que zero, 
 * cria ou atualiza compromisso		
 */
		if (codigoNnf[1] != null) {
			for (int ii = 1 ; ii < tam ; ii ++) {
				if (codcom[ii] == null || codcom[ii] < 999999999) {
					classe = "Tipo Consumo Ent Form";
					List<TipoConsumo> listTipo = tipoService.findAll();
					for (TipoConsumo tc : listTipo) {
						if (tc.getNomeTipo().equals("Empresa")) {
							entTip = tipoService.findById(tc.getCodigoTipo());
						}
					}	
					classe = "Periodo Ent Form";
					entPer = perService.findById(1);
					classe = "Fornecedor Ent Form";
					entFor = fornService.findById(codigoFor[ii]);
					classe = "Entrada Form";
					Entrada ent = new Entrada();
					ent = service.findById(codent[ii]);
					classe = "Compromisso Ent Form";
					comService.remove(codigoFor[ii], codigoNnf[ii]);
					parService.removeNnf(codigoNnf[ii], codigoFor[ii]);
					entCom = new Compromisso(codcom[ii], codigoFor[ii], entFor.getRazaoSocial(), codigoNnf[ii], 
							ent.getDataEnt(), ent.getDataEnt(), vlrent[ii], entFor.getParcela(), entFor.getPrazo(), 	
							entFor, entTip, entPer);
					comService.saveOrUpdate(entCom);
					CalculaParcela.parcelaCreate(entCom);
					flagAvisoII = 1;
				}
			}	
		}
	}
		
	private int conferePagamento(int flagD, Compromisso obj) {
	flagD = 0;
		List<Parcela> list = parService.findByIdFornecedorNnf(obj.getCodigoFornecedorCom(), obj.getNnfCom());
		List<Parcela> st = list.stream()
			.filter(p -> p.getNnfPar() == (obj.getNnfCom()))
			.filter(p -> p.getFornecedor().getCodigo() == (obj.getCodigoFornecedorCom()))  
			.filter(p -> p.getPagoPar() > 0)
			.collect(Collectors.toList());	
		if (st.size() > 0) {
			Alerts.showAlert("Erro!!! ", "Parcela paga ", "Existe(m) parcela(s) paga(s) para o Compromisso!!!", AlertType.ERROR);
			flagD = 1;
		}	
		return flagD;
	}		

	/*
	 * o contrainsts (confere)	 impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroEnt);
		Utils.formatDatePicker(dpDataEnt, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textNnfEnt);
 		Constraints.setTextFieldMaxLength(textNnfEnt, 6);
 		Constraints.setTextFieldMaxLength(textIniciaisForn, 7);
 		Constraints.setTextFieldMaxLength(textIniciaisProd, 7);
		initializeComboBoxFornEnt();
		initializeComboBoxProdEnt();
	}

	private void initializeComboBoxFornEnt() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
			}
		};

		comboBoxFornEnt.setCellFactory(factory);
		comboBoxFornEnt.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxProdEnt() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeProd() + 
						(String.format(" - R$%.2f", item.getVendaProd())));
			}
		};

		comboBoxProdEnt.setCellFactory(factory);
		comboBoxProdEnt.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		
		labelUser.setText(user);
		labelUser.viewOrderProperty();
//  string value of p/ casting int p/ string 		
		textNumeroEnt.setText(String.valueOf(entity.getNumeroEnt()));
		if (entity.getNumeroEnt() != null) {
			entityAnterior = entity;
		}

		if (entity.getDataEnt() == null) {
			entity.setDataEnt(new Date());
		}
		
		if (entity.getDataEnt() != null) {
			dpDataEnt.setValue(LocalDate.ofInstant(entity.getDataEnt().toInstant(), ZoneId.systemDefault()));
		}

		textNnfEnt.setText(String.valueOf(entity.getNnfEnt()));
		
		// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getForn() == null) {
			comboBoxFornEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxFornEnt.setValue(entity.getForn());
		}

		if (entity.getProd() == null) {
			comboBoxProdEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxProdEnt.setValue(entity.getProd());
		}
		
		if (entity.getQuantidadeProdEnt() == null) {
			entity.setQuantidadeProdEnt(0.00);
		}
		if (entity.getQuantidadeProdEnt() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdEnt());
			textQtdProdEnt.setText(qtd);
		}

		if (entity.getValorProdEnt() == null) {
			entity.setValorProdEnt(0.00);
		}
		if (entity.getValorProdEnt() != null) {
			String vlr = Mascaras.formataValor(entity.getValorProdEnt());
			textVlrProdEnt.setText(vlr);
		}

		totAnt = entity.getQuantidadeProdEnt() * entity.getValorProdEnt();

		totM = Mascaras.formataValor(totAnt);
		labelTotVlrProdEnt.setText(totM);
		labelTotVlrProdEnt.viewOrderProperty();
		textIniciaisForn.setText(pesquisaForn);
		textIniciaisProd.setText(pesquisaProd);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (fornService == null) {
			throw new IllegalStateException("FornecedorServiço esta nulo");
		}
		if (prodService == null) {
			throw new IllegalStateException("ProdutoServiço esta nulo");
		}
 // buscando (carregando) os dados do bco de dados		
		List<Fornecedor> listForn = fornService.findAll();
		listForn.removeIf(x -> x.getRazaoSocial().equals("Adiantamento"));
		List<Produto> listProd = prodService.findAll();
		
// transf p/ obslist
		obsListForn = FXCollections.observableArrayList(listForn);
		comboBoxFornEnt.setItems(obsListForn);
		obsListProd = FXCollections.observableArrayList(listProd);
		comboBoxProdEnt.setItems(obsListProd);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorNnfEnt.setText((fields.contains("nnf") ? erros.get("nnf") : ""));
		labelErrorDataEnt.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorQtdProdEnt.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorVlrProdEnt.setText((fields.contains("vlr") ? erros.get("vlr") : ""));
		if (fields.contains("confirma")) {
			Alerts.showAlert("Fechamento", null, "Conferindo total", AlertType.INFORMATION);
				try {	totM = Mascaras.formataValor(tot);
						labelTotVlrProdEnt.setText(totM);
						labelTotVlrProdEnt.viewOrderProperty();
					}
				 	catch (ParseException e) {
				 		e.printStackTrace();
				 	}
		}
	}
	
	private synchronized void createDialogForn(Fornecedor obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Fornecedor Ent Form";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			FornecedorFormController controller = loader.getController();
			controller.user = user;
			controller.setFornecedor(obj);
			controller.setFornecedorService(new FornecedorService());
//			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Fornecedor                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
 	}

	private synchronized void createDialogProd(Produto obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Produto Ent Form ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ProdutoFormController controller = loader.getController();
			controller.user = user;
			controller.setProduto(obj);
			controller.setProdutoService(new ProdutoService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Produto                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 	
}
