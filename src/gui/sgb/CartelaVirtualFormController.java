package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class CartelaVirtualFormController implements Initializable, DataChangeListener {

	private CartelaVirtual entity;
	private CartelaVirtual entityAnterior;
	Produto prod = new Produto();
	Produto prodAnt = new Produto();

	/*
	 * dependencia service com metodo set
	 */
	private CartelaVirtualService service;
	private FuncionarioService funService;
	private ProdutoService prodService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
	// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		try {
			updateFormData();
			getFormData();
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	@FXML
	private Label labelEntidade;

	@FXML
	private TextField textCartelaVir;

	@FXML
	private TextField textLocalVir;

	@FXML
	private TextField textPesquisaFun;

	@FXML
	private ComboBox<Funcionario> comboBoxFunVir;

	@FXML
	private TextField textPesquisaProd;

	@FXML
	private ComboBox<Produto> comboBoxProdVir;

	@FXML
	private TextField textQtdProdVir;

	@FXML
	private Label labelVendaProdVir;

	@FXML
	private Label labelTotalProdVir;

	@FXML
	private Button btPesquisaFun;

	@FXML
	private Button btPesquisaProd;

	@FXML
	private Button btSaveVir;

	@FXML
	private Button btCancelVir;

	@FXML
	private Button btSairVir;

	@FXML
	private Label labelErrorQtdProdVir;

	@FXML
	private Label labelErrorTotProdVir;

	@FXML
	private Label labelUser;

	// auxiliar
	public String user = "usuário";
	String classe = "CartelaVirtual Form";
	String pesquisaFun = "";
	String pesquisaProd = "";
	double totAnt = 0.00;
	int flag = 0;
	int estoque = 0;
	public static Integer numCar = 0;
	public static String local = "null";
	public static String situacao = "null";
	public static int mm = 0;
	public static int aa = 0;
	private int gravaVir = 0;

	private ObservableList<Funcionario> obsListFun;
	private ObservableList<Produto> obsListProd;

	public void setCartelaVirtual(CartelaVirtual entity) {
		this.entity = entity;
	}

	// * metodo set /p service
	public void setVirtualServices(CartelaVirtualService service, FuncionarioService funService,
			ProdutoService prodService) {
		this.service = service;
		this.funService = funService;
		this.prodService = prodService;
	}

	@FXML
	public void onBtPesqProdAction(ActionEvent event) {
		classe = "Produto Cart Vir Form";
		pesquisaProd = "";
		try {
			pesquisaProd = textPesquisaProd.getText().toUpperCase().trim();
			if (pesquisaProd != "") {
				List<Produto> listPro = prodService.findPesquisa(pesquisaProd);
				if (listPro.size() == 0) {
					pesquisaProd = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						Produto obj = new Produto();
						createDialogPro(obj, "/gui/sgb/ProdutoForm.fxml", parentStage);
					}
					listPro = prodService.findPesquisa(pesquisaProd);
				}
				obsListProd = FXCollections.observableArrayList(listPro);
				comboBoxProdVir.setItems(obsListProd);
	  			notifyDataChangeListerners();
				updateFormData();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto ", classe, e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto ", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtPesqFunAction(ActionEvent event) {
		classe = "Funcionario Cart Vir Form";
		pesquisaFun = "";
		try {
			pesquisaFun = textPesquisaFun.getText().toUpperCase().trim();
			if (pesquisaFun != "") {
				List<Funcionario> listFun = funService.findPesquisa(
						pesquisaFun, aa, mm);
				if (listFun.size() == 0) {
					pesquisaFun = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						Funcionario obj = new Funcionario();
						createDialogFun(obj, "/gui/sgb/FuncionarioForm.fxml", parentStage);
					}
					listFun = funService.findPesquisa(
							pesquisaFun, aa, mm);
				}
				obsListFun = FXCollections.observableArrayList(listFun);
				comboBoxFunVir.setItems(obsListFun);
	  			notifyDataChangeListerners();
				updateFormData();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto ", classe, e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto ", classe, e.getMessage(), AlertType.ERROR);
		}
	}
	
	/*
	 * vamos instanciar um orc e salvar no bco de dados meu obj entity (l� em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> n�o foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, � s� notificar (juntar) recebe l�
	 * no listController
	 */
	@FXML
	public void onBtSaveVirAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sair = 0;
			if (entity.getNumeroVir() != null) {
				sair = 1;
			}
			entity = getFormData();
			gravaVir = 2;
			classe = "CartelaVirtual Form";
			service.saveOrUpdate(entity);
			if (gravaVir == 2) {
				updateProduto(event);
			}
			notifyDataChangeListerners();
			entity = new CartelaVirtual();
			labelVendaProdVir.setText("0,00");
			labelTotalProdVir.setText("0,00");
			labelVendaProdVir.viewOrderProperty();
			labelTotalProdVir.viewOrderProperty();
			updateFormData();
			if (sair == 1) {
				Utils.currentStage(event).close();
			}	
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto ", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	public CartelaVirtual getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
		CartelaVirtual obj = new CartelaVirtual();
		// instanciando uma exce��o, mas n�o lan�ado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null
		obj.setNumeroVir(entity.getNumeroVir());
		if (numCar > 0) {
			obj.setOrigemIdCarVir(numCar);
		}
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro

		if (textLocalVir.getText() == null || textLocalVir.getText().trim().contentEquals("")) {
			exception.addErros("local", "Local é obrigatório");
		} else {
			obj.setLocalVir(textLocalVir.getText());
		}

		obj.setSituacaoVir("A");
		obj.setFuncionario(comboBoxFunVir.getValue());
		obj.setNomeFunVir(comboBoxFunVir.getValue().getNomeFun());
		prod = comboBoxProdVir.getValue();
		obj.setProduto(prod);
		obj.setNomeProdVir(prod.getNomeProd());
		obj.setVendaProdVir(prod.getVendaProd());
		obj.setPrecoProdVir(prod.getPrecoProd());
		
		String vlr = Mascaras.formataValor(obj.getVendaProdVir());
		labelVendaProdVir.setText(vlr);

		if (textQtdProdVir.getText() == null || textQtdProdVir.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		}
		if (textQtdProdVir.getText() != null) {
			obj.setQuantidadeProdVir(Utils.formatDecimalIn(textQtdProdVir.getText().replace(".", "")));
			estoque = 0;
			confereEstoque(prod.getCodigoProd(), obj.getQuantidadeProdVir());
		}
		if (obj.getQuantidadeProdVir() == 0) {
			exception.addErros("qtd", "Quantidade não pode ser 0");
		}
		if (estoque == 1) {
			obj.setQuantidadeProdVir(0.00);
			exception.addErros("qtd", "Qtd é maior que estoque");
		}

		obj.setTotalProdVir(obj.getQuantidadeProdVir() * obj.getVendaProdVir());

		if (obj.getTotalProdVir() != totAnt) {
			totAnt = obj.getTotalProdVir();
			String vlr2 = Mascaras.formataValor(obj.getVendaProdVir());
			labelVendaProdVir.setText(vlr2);
			vlr2 = Mascaras.formataValor(obj.getTotalProdVir());
			labelTotalProdVir.setText(vlr2);
			labelTotalProdVir.viewOrderProperty();
			exception.addErros("tot", "");
		}	

// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	private void confereEstoque(int cod, Double qtd) {
		classe = "Produto Cart Vir Form";
		if (!comboBoxProdVir.getValue().getGrupo().getNomeGru().contains("Serviços")) {
			if (comboBoxProdVir.getValue().getSaldoProd() < qtd) {
				estoque = 1;
			}
		}
	}	
	private void updateProduto(ActionEvent event) {
		try {
			ValidationException exception = new ValidationException("Validation exception");
			if (gravaVir == 2) {
				if (prod.getGrupo().getNomeGru().contains("Serviços")) {
					if (prod.getSaldoProd() < entity.getQuantidadeProdVir()) {
						prod.entraSaldo(entity.getQuantidadeProdVir());
// serviço não tem estoque - entra no momento da solicitação
					}
				}
				if (entityAnterior.getNumeroVir() != null) {
					if (prod.getCodigoProd() == entityAnterior.getProduto().getCodigoProd()) {						
						prod.setSaidaCmmProd(-1 * entityAnterior.getQuantidadeProdVir());
						prod.entraSaldo(entityAnterior.getQuantidadeProdVir());
					} else {
						prodAnt = prodService.findById(entityAnterior.getProduto().getCodigoProd());
						prodAnt.setSaidaCmmProd(-1 * entityAnterior.getQuantidadeProdVir());
						prodAnt.entraSaldo(entityAnterior.getQuantidadeProdVir());
						prodAnt.calculaCmm();
						prodService.saveOrUpdate(prodAnt);
					}
				}	
				if(entity.getQuantidadeProdVir() > prod.getSaldoProd()) {
					Alerts.showAlert("Saldo", "saldo vai ficar negativo", entity.getNomeProdVir(), AlertType.ERROR);
					gravaVir = 0;
					exception.addErros("orcto", "Erro !!! Saldo < saida ");
					exception.addErros("parcela", prod.getNomeProd());
					exception.addErros("prazo", "Efetue entrada");
					Optional<ButtonType> result = Alerts.showConfirmation("Saldo insuficiente ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						Compromisso com = new Compromisso();
						Parcela par = new Parcela();
						ParPeriodo per = new ParPeriodo();
						Fornecedor forn =new Fornecedor();
						TipoConsumo tipo = new TipoConsumo();
						Entrada ent = new Entrada();
						createDialogEnt(ent, prod, com, per, par, forn, tipo,
											"/gui/sgo/EntradaForm.fxml", parentStage);
							prod = prodService.findById(entity.getProduto().getCodigoProd());
							if (prod.getSaldoProd() > entity.getQuantidadeProdVir()) {
								gravaVir = 2;
							}
							try {
								notifyDataChangeListerners();
								updateFormData();
							} catch (ParseException e) {
								e.printStackTrace();
							}	
						} else {
							Alerts.showAlert("Atenção", "Execute entrada", "volte a operação", AlertType.INFORMATION);
							notifyDataChangeListerners();
							Utils.currentStage(event).close();
						}
					} else {
						prod.saidaSaldo(entity.getQuantidadeProdVir());
						prod.setSaidaCmmProd(entity.getQuantidadeProdVir());
						prod.calculaCmm();
						prodService.saveOrUpdate(prod);
						entity.setProduto(prod);
// saidaCmmProd p/ calculo cmmm
						if (prod.getGrupo().getNomeGru().contains("Cozinha")) {
							listaCozinha();
						}
						if (prod.getSaldoProd() < prod.getEstMinProd()) {
							if (prod.getSaldoProd() == 0.00) {
								Alerts.showAlert("Estoque", prod.getNomeProd(), "Estoque zerou ", AlertType.WARNING);
							} else {	
								Alerts.showAlert("Estoque", prod.getNomeProd(), "Recompor estoque ", AlertType.WARNING);
							}	
						}
					}	
				}	
				if (exception.getErros().size() > 0) {
					throw exception;
				}
			}	
			catch (DbException e) {
				Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
	
	private void listaCozinha() {
		CartelaCozinhaImprimeController.local = local;
		CartelaCozinhaImprimeController.nomeProd = prod.getNomeProd();
		CartelaCozinhaImprimeController.quantidade = entity.getQuantidadeProdVir();
		try {
			CartelaCozinhaImprimeController.onBtImprimeProduto();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelVirAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtSairVirAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLength(textPesquisaFun, 7);
		Constraints.setTextFieldMaxLength(textPesquisaProd, 7);
		Constraints.setTextFieldDouble(textQtdProdVir);
		initializeComboBoxProdVir();
		initializeComboBoxFuncVir();
	}

	private void initializeComboBoxProdVir() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeProd());
			}
		};

		comboBoxProdVir.setCellFactory(factory);
		comboBoxProdVir.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxFuncVir() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFunVir.setCellFactory(factory);
		comboBoxFunVir.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		entityAnterior = entity; 
		labelEntidade.setText("Consumo");
		labelUser.setText(user);
//  string value of p/ casting int p/ string
		textCartelaVir.setText(String.valueOf(numCar));
		textLocalVir.setText(entity.getLocalVir());
		if (textLocalVir.getText() == null) {
			textLocalVir.setText(local);
		}	
// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getFuncionario() == null) {
			comboBoxFunVir.getSelectionModel().selectFirst();
		} else {
			comboBoxFunVir.setValue(entity.getFuncionario());
		}

		if (entity.getProduto() == null) {
			comboBoxProdVir.getSelectionModel().selectFirst();
		} else {
			comboBoxProdVir.setValue(entity.getProduto());
		}
		if (entity.getQuantidadeProdVir() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdVir());
			textQtdProdVir.setText(qtd);
		} else {
			entity.setQuantidadeProdVir(0.00);
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdVir());
			textQtdProdVir.setText(qtd);
		}

		if (entity.getVendaProdVir() != null) {
			String vlr = Mascaras.formataValor(entity.getProduto().getVendaProd());
			labelVendaProdVir.setText(vlr);
		}

		totAnt = 0.00;
		if (entity.getTotalProdVir() != null) {
			totAnt = entity.getTotalProdVir();
		}

		String vlr2 = Mascaras.formataValor(totAnt);
		labelTotalProdVir.setText(vlr2);
		labelTotalProdVir.viewOrderProperty();
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("FuncionarioServiço esta nulo");
		}
		if (prodService == null) {
			throw new IllegalStateException("Produto Serviço esta nulo");
		}
		if (service == null) {
			throw new IllegalStateException("VirtualServiço esta nulo");
		}
		labelUser.setText(user);
		// buscando (carregando) os dados do bco de dados
		List<Funcionario> listFun = funService.findAll(aa, mm); 
		List<Produto> listPro = prodService.findAll();
// transf p/ obslist
		obsListFun = FXCollections.observableArrayList(listFun);
		obsListProd = FXCollections.observableArrayList(listPro);
		comboBoxFunVir.setItems(obsListFun);
		comboBoxProdVir.setItems(obsListProd);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorQtdProdVir.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
//		labelErrorTotProdVir.setText((fields.contains("tot") ? erros.get("tot") : ""));
		if (fields.contains("tot")) {
			Alerts.showAlert(null, "Confirmando total ", null, AlertType.INFORMATION);
		}
	}

	private void createDialogPro(Produto obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Produto Cart Virt Form";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
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

	private void createDialogFun(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Funcionário Cart Virt Form ";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FuncionarioFormController controller = loader.getController();
			controller.user = user;
			controller.setFuncionario(obj);
			controller.setServices(new FuncionarioService(), new CargoService(), new SituacaoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Funcionário                                             ");
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
	private void createDialogEnt(Entrada obj, Produto prod, Compromisso objCom, ParPeriodo objPer, Parcela objPar, 
			Fornecedor objFor, TipoConsumo objTipo, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj
			String nome = prod.getNomeProd();
			controller.setPesquisa(nome);
			controller.setObjects(obj, prod, objCom, objPer, objPar, objFor, objTipo);
			controller.setServices(new EntradaService(), new FornecedorService(), new ProdutoService(), 
					new CompromissoService(), new TipoConsumoService(), new ParPeriodoService(),
					new ParcelaService());
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Entrada                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela ", e.getMessage(), AlertType.ERROR);
		}
 	} 		
}
