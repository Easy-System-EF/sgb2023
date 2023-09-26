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

import application.MainSgb;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.dao.CartelaCommitDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;

/*
 * Monta formaul�rio do cartela e virtual
 */

public class CartelaFormController implements Initializable, DataChangeListener {

	private Cartela entity;
	private CartelaVirtual virtual;
	private CartelaPagante pagante;

	/*
	 * dependencia service com metodo set
	 */
	private CartelaService service;
	private CartelaVirtualService virService;
	private CartelaPaganteService pagService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private GridPane gridPaneCar;

	@FXML
	private TextField textNumeroCar;

	@FXML
	private DatePicker dpDataCar;

	@FXML
	private TextField textLocalCar;

	@FXML
	private TextField textDescontoCar;

	@FXML
	private TextField textObsCar;

	@FXML
	private TextField textNumeroPaganteCar;

	@FXML
	private RadioButton rbServicoSimCar;

	@FXML
	private RadioButton rbServicoNaoCar;

	@FXML
	private TextField textServicoCar;

	@FXML
	private Label labelTotalCar;

	@FXML
	private Label labelValorPaganteCar;

	@FXML
	private TableView<CartelaVirtual> tableViewVir;

	@FXML
	private TableColumn<CartelaVirtual, String> tableColumnNomeProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnQtdProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnVendaProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnTotalProdVir;

	@FXML
	private TableColumn<CartelaVirtual, CartelaVirtual> tableColumnEditaVir;

	@FXML
	private TableColumn<CartelaVirtual, CartelaVirtual> tableColumnRemoveVir;

	@FXML
	private Button btSaveCar;

	@FXML
	private Button btCancelCar;

	@FXML
	private Button btFechaCar;

	@FXML
	private Button btCaloteCar;

	@FXML
	private Label labelErrorDataCar;

	@FXML
	private Label labelErrorLocalCar;

	@FXML
	private Label labelErrorServicoCar;

	@FXML
	private Button btNewVir;

	@FXML
	private Label labelUser;

	// auxiliar
	public static String situacao = null;
	public static Integer numPag = 0;
	public static Integer sair  = 0;
	public Integer nivel = 0;
	public String user = "usuário";
	public String local = null;
	String servico = null;
	String consumo = null;
	String classe = "Cartela Form";
	String letraSit = null;
	String nomeSit = null;
	Integer numCar = 0;
	Integer flag = 0;
	int codFun = 0;
	double totProd = 0.00;
	Double vlr = null;

// aux total	
	String vlrTotMasc = "";
	String vlrPagMasc = "";

	private ObservableList<CartelaVirtual> obsListVir;

	@FXML
	public void onBtFechaAction(ActionEvent event) throws ParseException {
		confereTotal();
		String result = "sim";
		if (entity.getTotalCar() == 0.00) {
			Alerts.showAlert(null, "Atenção", "Cartela sem valor!!!", AlertType.ERROR);
			result = "nao";
		} else {
			if (entity.getLocalCar() == null) {
				Alerts.showAlert(null, "Atenção", "Não existe local!!!", AlertType.ERROR);
				result = "nao";
			}
		}
		if (entity.getNumeroPaganteCar() == 0) {
			Alerts.showAlert(null, "Atenção", "Informe número de pagante(s) e Ok", AlertType.INFORMATION);
			result = "nao";
		}
		if (result == "sim") {
			pagService.remove(numCar);
			numPag = 0;
			while (numPag < entity.getNumeroPaganteCar()) {
				numPag += 1;
				pagante = new CartelaPagante();
				Stage parentStage = Utils.currentStage(event);
				createDialogFormFecha(entity, pagante, "/gui/sgb/CartelaPaganteForm.fxml", parentStage);
			}	
		} else {
			Alerts.showAlert(null, "não existe pagante!!!", null, AlertType.INFORMATION);
		}	

		if (situacao == "P") {
			List<CartelaPagante> listPag = pagService.findByCartela(numCar);
			for (CartelaPagante p : listPag) { 	
				entity.setMesPagCar(p.getMesPagamentoPag());
				entity.setAnoPagCar(p.getAnoPagamentoPag());
			}	
			letraSit = "P";
			nomeSit = "Pago";
			commitForm();			
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		}
	}

	private void commitForm() {
		CartelaCommitDao commitDao = DaoFactory.createCartelaCommitDao();
		commitDao.gravaCartelaCommit(entity, letraSit, nomeSit);
	}

//	Map<Integer, Double> mapFun = new HashMap<>();
	
	@FXML
	public void onBtCaloteAction(ActionEvent event) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operação não permitida", AlertType.INFORMATION);
		} else {
			if (entity != null) {
				confereTotal();
			}
			classe = "CartelaVirtual Cart Form";
			List<CartelaVirtual> listVir = virService.findCartela(numCar);
			for (CartelaVirtual v : listVir) {
				virtual = v;
				virtual.setSituacaoVir("C");
				virService.saveOrUpdate(virtual);
			}
			entity.setSituacaoCar("C");
			entity.setNomeSituacaoCar("Calote");
			letraSit = "C";
			nomeSit = "Calote";
			commitForm();			
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		}
	}

	@FXML
	public void onBtNewVirAction(ActionEvent event) {
		if (numCar == null) {
			if (textLocalCar.getText() == null) {
				Alerts.showAlert("Atenção", "Preencha no mínimo", "data, local ", AlertType.ERROR);
			} else {
				newCartela();
			}
		}
		if (entity.getNumeroCar() != null) {
				Cartela car = service.findById(entity.getNumeroCar());
				local = car.getLocalCar().toUpperCase();
				Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
				CartelaVirtual virtual = new CartelaVirtual(); 
				createDialogForm(virtual, "/gui/sgb/CartelaVirtualForm.fxml", parentStage);
				confereTotal();
		}	
	}

	private void newCartela() {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço cartela nulo");
		}
		try {
			Instant instant = Instant.from(dpDataCar.getValue().atStartOfDay(ZoneId.systemDefault()));
			entity.setDataCar(Date.from(instant));
			entity.setLocalCar(textLocalCar.getText().toUpperCase());
			if (textLocalCar.getText() != null) {
				confereLocal();
			}	
			if (textLocalCar.getText() == null) {
				Alerts.showAlert("Atenção!!! ", "Local em uso e/ou aberto ", null, AlertType.INFORMATION);
			} else {	
				entity.setDescontoCar(0.00);
				entity.setTotalCar(0.00);
				entity.setSituacaoCar("A");
				entity.setNumeroPaganteCar(1);
				entity.setValorPaganteCar(0.00);
				entity.setMesCar(dpDataCar.getValue().getMonthValue());
				entity.setAnoCar(dpDataCar.getValue().getYear());
				entity.setObsCar("");
				entity.setServicoCar("Sem serviço");
				entity.setValorServicoCar(0.00);
				entity.setSubTotalCar(0.00);
				classe = "Cartela Form";
				service.saveOrUpdate(entity);
				numCar = entity.getNumeroCar();
				try {
					updateFormData();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				notifyDataChangeListerners();
			}	
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void confereLocal() {
		int uso = 0;
		List<Cartela> cartelaLocal = service.findSituacao("A");
		if (cartelaLocal.size() > 0) {
			for (Cartela cl : cartelaLocal) {
				if (uso == 0) {
					if (entity.getNumeroCar() != null) {
						if (entity.getNumeroCar() != cl.getNumeroCar()) {
							if (cl.getLocalCar().equals(textLocalCar.getText().toUpperCase())) {
								uso = 1;
							}	
						}	
					} else {	
						if (cl.getLocalCar().equals(textLocalCar.getText().toUpperCase())) {
							uso = 1;
						}	
					}	
				}
			}
			if (uso == 1) {
				textLocalCar.setText(null);
			}	
			notifyDataChangeListerners();
		}
	}

	public void setCartelas(Cartela entity, CartelaVirtual virtual, CartelaPagante pagante,
			Adiantamento adiantamento, Funcionario funcionario) {
		this.entity = entity;
		this.virtual = virtual;
		this.pagante = pagante;
//		this.funcionario = funcionario;
//		this.adiantamento = adiantamento;
	}

	// * metodo set /p service
	public void setServices(CartelaService service, CartelaVirtualService virService,
			CartelaPaganteService pagService, AdiantamentoService adiService,
			FuncionarioService funService) {
		this.service = service;
		this.virService = virService;
		this.pagService = pagService;
//save		this.adiService = adiService;
//save		this.funService = funService;
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (l� em cima)
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
	public void onBtSaveCarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço cartela nulo");
		}
		@SuppressWarnings("unused")
		ValidationException exception = new ValidationException("Validation exception");
		try {
			classe = "Cartela Form";
			entity = getFormData();
			confereTotal();
			classe = "Cartela Form";
			service.saveOrUpdate(entity);
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	private void confereTotal() {
		try {
			@SuppressWarnings("unused")
			ValidationException exception = new ValidationException("Validation exception");
			getFormData();
			if (entity.getNumeroCar() != null) {
				entity.setTotalCar(virService.sumTotalCartela(entity.getNumeroCar()));
				if	(numCar == entity.getNumeroCar()) {
					entity.calculaValorPagante();
					if (entity.getTotalCar() > 0.00) {
						vlrTotMasc = Mascaras.formataValor(entity.getTotalCar());
						labelTotalCar.setText(vlrTotMasc);						
						labelTotalCar.viewOrderProperty();
						vlrPagMasc = Mascaras.formataValor(entity.getValorPaganteCar());
						labelValorPaganteCar.setText(vlrPagMasc);
						labelValorPaganteCar.viewOrderProperty();
						updateFormData();			
						notifyDataChangeListerners();
					}	
				}
			}
			
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}	
	}	

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

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se n�o for atz tb verificamos se cpos
	 * obrigat�rios est�o preenchidos, para informar erro(s) para cpos string n�o
	 * precisa tryParse
	 */
	private Cartela getFormData() throws ParseException {
		Cartela obj = new Cartela();
		obj = entity;
		// instanciando uma exce��o, mas n�o lan�ado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null
		if (numCar != null) {
			obj.setNumeroCar(numCar);
		} else {
			obj.setNumeroCar(Utils.tryParseToInt(textNumeroCar.getText()));
		}

// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro

		if (dpDataCar.getValue() != null) {
			Instant instant = Instant.from(dpDataCar.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataCar(Date.from(instant));
		} else {
			if (dpDataCar.getValue() == null) {
				exception.addErros("data", "Data é obrigatória");
			}
		}

		if (textLocalCar.getText() == null || textLocalCar.getText().trim().contentEquals("")) {
			exception.addErros("local", "Local é obrigatório");
		} else {
			if (textLocalCar.getText() != null || textLocalCar.getText().trim().contentEquals("")) {
				obj.setLocalCar(textLocalCar.getText().toUpperCase());
			}
		}
		confereLocal();
		if (textLocalCar.getText() == null) {
			exception.addErros("local", "Local em uso e/ou aberto");
		}	

		if (obj.getSituacaoCar() == null) {
			obj.setSituacaoCar("A");
			obj.setNomeSituacaoCar("Aberto");
		}	

		if (textDescontoCar.getText() == null || textDescontoCar.getText().trim().contentEquals("")) {
			obj.setDescontoCar(0.00);
		} else {
			obj.setDescontoCar(Utils.tryParseToDouble(textDescontoCar.getText().replace(",", ".")));
		}

		obj.setNumeroPaganteCar(Utils.tryParseToInt(textNumeroPaganteCar.getText()));

		obj.setValorServicoCar(0.00);
		obj.setSubTotalCar(0.00);

		int flagServ = 0;
		if (rbServicoSimCar.isSelected()) {
			obj.setServicoCar("Com serviço");
			rbServicoNaoCar.setSelected(false);
			flagServ += 1;
		}
		if (rbServicoNaoCar.isSelected()) {
			obj.setServicoCar("Sem serviço");
			rbServicoSimCar.setSelected(false);
			flagServ += 1;
		}
		if (flagServ > 1) {
			exception.addErros("servico", "Só pode uma opção");
		}
		
		obj.setObsCar(textObsCar.getText());
		if (dpDataCar.getValue() != null) {
			entity.setMesCar(dpDataCar.getValue().getMonthValue());
			entity.setAnoCar(dpDataCar.getValue().getYear());
		} else {
			obj.setMesCar(0);
			obj.setAnoCar(0);
		}
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	public void updateTableView() {
		if (virService == null) {
			throw new IllegalStateException("Serviço virtual está vazio");
		}
		labelUser.setText(user);
		if (entity.getNumeroCar() != null) {
			List<CartelaVirtual> listVir = virService.findCartela(entity.getNumeroCar());
			obsListVir = FXCollections.observableArrayList(listVir);
			tableViewVir.setItems(obsListVir);
			initEditButtons();
			initRemoveButtons();
		}
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelCarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroCar);
		Utils.formatDatePicker(dpDataCar, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(textDescontoCar);
		Constraints.setTextFieldMaxLength(textLocalCar, 10);
		Constraints.setTextFieldMaxLength(textNumeroPaganteCar, 02);
		initializeNodes();
	}

	// comportamento padr�o para iniciar as colunas
	private void initializeNodes() {
		tableColumnNomeProdVir.setCellValueFactory(new PropertyValueFactory<>("NomeProdVir"));
		tableColumnQtdProdVir.setCellValueFactory(new PropertyValueFactory<>("QuantidadeProdVir"));
		Utils.formatTableColumnDouble(tableColumnQtdProdVir, 2);
		tableColumnVendaProdVir.setCellValueFactory(new PropertyValueFactory<>("VendaProdVir"));
		Utils.formatTableColumnDouble(tableColumnVendaProdVir, 2);
		tableColumnTotalProdVir.setCellValueFactory(new PropertyValueFactory<>("TotalProdVir"));
		Utils.formatTableColumnDouble(tableColumnTotalProdVir, 2);
		// para tableview preencher o espa�o da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewVir.prefHeightProperty().bind(stage.heightProperty());
	}

	@SuppressWarnings("static-access")
	private void createDialogForm(CartelaVirtual virtual, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "CartelaVirtual  Cart Form";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaVirtualFormController controller = loader.getController();
			controller.user = user;
			controller.local = local;
// injetando passando parametro obj 			
			controller.setCartelaVirtual(virtual);
			controller.numCar = numCar;
			controller.mm = dpDataCar.getValue().getMonthValue();
			controller.aa = dpDataCar.getValue().getYear();

// injetando servi�os vindo da tela de formulario fornform
			controller.setVirtualServices(new CartelaVirtualService(), new FuncionarioService(), new ProdutoService());
			controller.loadAssociatedObjects();
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Consumo                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	@SuppressWarnings("static-access")
	private void createDialogFormFecha(Cartela obj, CartelaPagante pagante, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "CartelaPagante Cart Form";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaPaganteFormController controller = loader.getController();

			controller.user = user;
//injetando passando parametro obj 			
			controller.numCar = numCar;
			controller.numPagante = numPag;
			controller.setPagantes(pagante, obj);
//injetando servi�os vindo da tela de formulario fornform
			controller.setServices(new CartelaPaganteService(), new CartelaService());
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Fechamento Consumo                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initRemoveButtons() {
		tableColumnRemoveVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveVir.setCellFactory(param -> new TableCell<CartelaVirtual, CartelaVirtual>() {
			private final Button button = new Button("exclui");

			@Override
			protected void updateItem(CartelaVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(virtual));
			}
		});
	}

	private void removeEntity(CartelaVirtual virtualRmv) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (virService == null) {
					throw new IllegalStateException("Serviço virtual está vazio");
				}
				try {
					classe = "Cartela Form";
					estoque(virtualRmv);
					virService.removeLinha(virtualRmv.getNumeroVir());
					confereTotal();
					updateFormData();
					updateTableView();
				} catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				} catch (ParseException p) {
					p.getStackTrace();
				}
			}
		}
	}
	
	private void estoque(CartelaVirtual virtualRemove) {
		Produto prod = new Produto();
		ProdutoService prodService = new ProdutoService();
		int codProd = virtualRemove.getProduto().getCodigoProd();
		prod = prodService.findById(codProd);
		if (! prod.getGrupo().getNomeGru().contains("Serviços")) {
			prod.setSaidaCmmProd(-1 * virtualRemove.getQuantidadeProdVir());
			prod.calculaCmm();
			prod.entraSaldo(virtualRemove.getQuantidadeProdVir());
			prodService.saveOrUpdate(prod);
		}
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela esta nula");
		}

		// string value of p/ casting int p/ string
		textNumeroCar.setText(String.valueOf(entity.getNumeroCar()));
		numCar = entity.getNumeroCar();
		// se for uma inclusao, vai posicionar no 1o depto//tipo (First)

		
		if (entity.getDataCar() == null) {
			entity.setDataCar(new Date());
		}	
		dpDataCar.setValue(LocalDate.ofInstant(entity.getDataCar().toInstant(), ZoneId.systemDefault()));

		textLocalCar.setText(entity.getLocalCar());

		String vlr = "0.00";
		if (entity.getDescontoCar() == null) {
			entity.setDescontoCar(0.00);
		}
		vlr = Mascaras.formataValor(entity.getDescontoCar());
		textDescontoCar.setText(vlr);
		
		if (entity.getServicoCar() == null) {
			entity.setServicoCar("Sem serviço");
			rbServicoNaoCar.setSelected(true);
			rbServicoSimCar.setSelected(false);			
		} else {
			if (entity.getServicoCar().equals("Com serviço")) {
				rbServicoSimCar.setSelected(true);
				rbServicoNaoCar.setSelected(false);
			} else {
				rbServicoSimCar.setSelected(false);			
				rbServicoNaoCar.setSelected(true);
				entity.setServicoCar("Sem serviço");
			}
		}	

		textServicoCar.setText(entity.getServicoCar());

		if (entity.getMesCar() == null) {
			entity.setMesCar(0);
		}
		
		if (entity.getAnoCar() == null) {
			entity.setAnoCar(0);
		}

		if (entity.getTotalCar() == null) {
			entity.setTotalCar(0.00);
		}
		vlrTotMasc = Mascaras.formataValor(entity.getTotalCar());
		labelTotalCar.setText(vlrTotMasc);
		labelTotalCar.viewOrderProperty();

		if (entity.getNumeroPaganteCar() == null) {
			entity.setNumeroPaganteCar(1);
		}
		textNumeroPaganteCar.setText(String.valueOf(entity.getNumeroPaganteCar()));

		if (entity.getValorPaganteCar() == null) {
			entity.setValorPaganteCar(0.00);
		}
		vlrPagMasc = Mascaras.formataValor(entity.getValorPaganteCar());
		labelValorPaganteCar.setText(vlrPagMasc);
		labelValorPaganteCar.viewOrderProperty();

		entity.setSubTotalCar(0.00);
		entity.setValorServicoCar(0.00);
	}

	private void initEditButtons() {
		tableColumnEditaVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditaVir.setCellFactory(param -> new TableCell<CartelaVirtual, CartelaVirtual>() {
			private final Button button = new Button("edita");

			@Override
			protected void updateItem(CartelaVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(virtual, "/gui/sgb/CartelaVirtualForm.fxml",
						Utils.currentStage(event)));
				confereTotal();
			}
		});
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (virService == null) {
			throw new IllegalStateException("Virtual Serviço esta nulo");
		}
// buscando (carregando) bco de dados
		List<CartelaVirtual> listVir = new ArrayList<>();
		listVir = virService.findCartela(numCar);
// transf p/ obslist		
		obsListVir = FXCollections.observableArrayList(listVir);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataCar.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorLocalCar.setText((fields.contains("local") ? erros.get("local") : ""));
		labelErrorServicoCar.setText((fields.contains("servico") ? erros.get("servico") : ""));
		if (fields.contains("confirma")) {
			Alerts.showAlert("Total", null, "Conferindo total", AlertType.INFORMATION);
			labelTotalCar.viewOrderProperty();
			labelValorPaganteCar.viewOrderProperty();
			flag = 1;
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
