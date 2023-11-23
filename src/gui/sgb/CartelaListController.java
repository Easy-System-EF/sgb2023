package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.ClienteService;
import gui.sgbmodel.service.EmpresaService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Alerts;
import gui.util.DataStaticSGB;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class CartelaListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private CartelaService service;
	CartelaVirtual objVir = new CartelaVirtual();
	CartelaPagante objPag = new CartelaPagante();
	Adiantamento objAdi = new Adiantamento();
	Funcionario objFun = new Funcionario();
	Cargo objCar = new Cargo();
	

	@FXML
 	private TableView<Cartela> tableViewCartela;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Cartela, Integer>  tableColumnNumeroCar;
 	
 	@FXML
 	private TableColumn<Cartela, Date>  tableColumnDataCar;
 	
 	@FXML
 	private TableColumn<Cartela, String> tableColumnLocalCar;

   	@FXML
 	private TableColumn<Cartela, Double> tableColumnTotalCar;
 	
   	@FXML
 	private TableColumn<Cartela, Double> tableColumnNomeSituacaoCar;
 	
  	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnEDITA;

 	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnList ;

 	@FXML
 	private Button btNewCar;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Cartela List Con";
 	public static Integer numEmp = 0;
 	public static Integer nivel = 0;
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Cartela> obsList;

 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
	@FXML
  	public void onBtNewCarAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Cartela obj = new Cartela();
 		 createDialogForm(obj, objVir, objPag, objAdi, objFun, objCar,  "/gui/sgb/CartelaForm.fxml", parentStage);
 		 initializeNodes();
   	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setCartelaService(CartelaService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroCar.setCellValueFactory(new PropertyValueFactory<>("numeroCar"));
		tableColumnDataCar.setCellValueFactory(new PropertyValueFactory<>("dataCar"));
		Utils.formatTableColumnDate(tableColumnDataCar, "dd/MM/yyyy");
   		tableColumnLocalCar.setCellValueFactory(new PropertyValueFactory<>("localCar"));
 		tableColumnTotalCar.setCellValueFactory(new PropertyValueFactory<>("totalCar"));
		Utils.formatTableColumnDouble(tableColumnTotalCar, 2);
   		tableColumnNomeSituacaoCar.setCellValueFactory(new PropertyValueFactory<>("NomeSituacaoCar"));
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCartela.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de seguran�a p/ servi�o vazio
 *  criando uma lista para receber os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
 		LocalDate dth = DataStaticSGB.criaLocalAtual();
 		int mm = DataStaticSGB.mesDaData(dth);
 		labelUser.setText(user);
		List<Cartela> list = service.findAll();
//		list.removeIf(x -> x.getMesCar() != mm );
		list.removeIf(x -> x.getMesCar() != mm && x.getNomeSituacaoCar().equals("Pago"));
 		obsList = FXCollections.observableArrayList(list);
		tableViewCartela.setItems(obsList);
		notifyDataChangeListerners();
		initEditButtons();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	@SuppressWarnings("static-access")
	private void createDialogForm(Cartela obj, CartelaVirtual objVir, CartelaPagante objPag,
				Adiantamento objAdi, Funcionario objFun, Cargo objCar, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "Cartela List Con";
// referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaFormController controller = loader.getController();
 // injetando passando parametro obj
			controller.setCartelas(obj, objVir, objPag, objAdi, objFun);
 // injetando servi�os vindo da tela de formulario fornform
			controller.setServices(new CartelaService(), 
									new CartelaVirtualService(),
									new CartelaPaganteService(),
									new AdiantamentoService(),
									new FuncionarioService(),
									new ClienteService());
			controller.user = user;
			controller.local = obj.getLocalCar();
			controller.situacao = "A";
			controller.nivel = nivel;
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateTableView();
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Cartela                                             ");
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
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

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
		updateTableView();
	}

/*
 * metodo p/ botao edit do frame
 * ele cria bot�o em cada linha 
 * o cell instancia e cria
*/	
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Cartela obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
				 setGraphic(null); 
				 return; 
		      }
		 
			if (obj.getSituacaoCar().contains("P")) {
				@SuppressWarnings("unused")
				int nada = 0;
			}
			else {
		      setGraphic(button); 
		      button.setOnAction( 
					event -> createDialogForm(obj, objVir, objPag, objAdi, objFun, objCar, 
							"/gui/sgb/CartelaForm.fxml", Utils.currentStage(event)));
			}  
		    }
		  }); 
		}
	
	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		  private final Button button = new Button("lista");
		 
		  @Override 
		  protected void updateItem(Cartela obj, boolean empty) { 
		     super.updateItem(obj, empty); 
		     if (obj == null) { 
		    	  setGraphic(null); 
		    	  return; 
		     }
		     Empresa emp = new Empresa();
		     CartelaVirtual objVir = new CartelaVirtual();
		     setGraphic(button); 
		     button.setOnAction( 
	    	  event -> imprimeCartela(obj, objVir, emp, obj.getNumeroCar()));
		  }
		  }); 
		}
	
	@SuppressWarnings("static-access")
	private void imprimeCartela(Cartela obj, CartelaVirtual objVir, Empresa emp, Integer codCar) {
		CartelaImprimeController cartelaImpr = new CartelaImprimeController();
		cartelaImpr.setCartela(obj, emp);
		cartelaImpr.numEmp = numEmp;
		cartelaImpr.numCar = obj.getNumeroCar();
		cartelaImpr.setCartelaService(new CartelaService(),
						 new CartelaVirtualService(),
						 new EmpresaService(),
						 new ClienteService());
		cartelaImpr.imprimeCartela();
	}	
	
	private void initRemoveButtons() {
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Cartela obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(event -> removeEntity(obj));
		    		
		        } 
		    });
 		} 

	private void removeEntity(Cartela obj) {
   		if (nivel > 1 && nivel < 9) {
   			Alerts.showAlert(null,"Atenção", "Operação não permitida", AlertType.INFORMATION);
   		} else {
   			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
   			if (result.get() == ButtonType.OK) {
   				if (service == null) {
   					throw new IllegalStateException("Serviço está vazio");
   				}
   				try {
					estoque(obj.getNumeroCar());
					comissao(obj.getNumeroCar());
   					CartelaVirtualService virService = new CartelaVirtualService();
   					virService.removeCartela(obj.getNumeroCar());
   					service.remove(obj.getNumeroCar());
   					updateTableView();
   				}
   				catch (DbIntegrityException e) {
   					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
   				}
   			}
   		}
	}
	
	private void estoque(int numCarEst) {
		Produto prod = new Produto();
		ProdutoService prodService = new ProdutoService();
		CartelaVirtualService virService = new CartelaVirtualService();
		List<CartelaVirtual> listVirtual = virService.findCartela(numCarEst);
		for (CartelaVirtual cv : listVirtual) {
			if (cv.getNumeroVir() != null) {
				if (cv.getOrigemIdCarVir() == numCarEst) {
					prod = prodService.findById(cv.getProduto().getCodigoProd());
					if (! prod.getGrupo().getNomeGru().contains("Serviços")) {
						prod.entraSaldo(cv.getQuantidadeProdVir());
						prod.setSaidaCmmProd(-1 *  cv.getQuantidadeProdVir());
						prod.calculaCmm();
						prodService.saveOrUpdate(prod);
					}	
				}
			}	
		}
	}

	private void comissao(int numCarAdi) {
		AdiantamentoService adiService = new AdiantamentoService();
		adiService.remove(numCarAdi);
	}
}	