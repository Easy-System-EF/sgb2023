package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
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
 
public class AdiantamentoListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private AdiantamentoService service;
	private CompromissoService comService;
	private ParcelaService parService;
	
	private Adiantamento obj = new Adiantamento();
	private Funcionario objFun = new Funcionario();
	private Compromisso objCom = new Compromisso();
	private ParPeriodo objPer = new ParPeriodo();
	private Parcela objPar = new Parcela();
	private TipoConsumo objTip = new TipoConsumo();

	 @FXML
 	private TableView<Adiantamento> tableViewAdiantamento;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Adiantamento, Integer> tableColumnNumeroAdi;

 	@FXML
 	private TableColumn<Adiantamento, String> tableColumnNomeFunAdi;

 	@FXML
 	private TableColumn<Adiantamento, Date> tableColumnDataAdi;

   	@FXML
 	private TableColumn<Adiantamento, String> tableColumnCargoAdi;
 	
   	@FXML
   	private TableColumn<Adiantamento, String> tableColumnSituacaoAdi;
   	
   	@FXML
   	private TableColumn<Adiantamento, Double> tableColumnValeAdi;

 	@FXML
 	private TableColumn<Adiantamento, Adiantamento> tableColumnREMOVE;

 	@FXML
 	private Button btNewAdi;

   	@FXML
   	private Button btMeses;
   	 	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Adiantamento Adi List";
 	String tipo = "A";
 	public static Integer numEmp = null;
 	public static Integer nivel = null;

// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Adiantamento> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	@FXML
  	public void onBtNewAdiAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
         classe = "Adiantamento  Adi List";
 		 createDialogForm(obj, objFun, objCom, objPer, objPar, objTip,   "/gui/sgb/AdiantamentoForm.fxml", parentStage);
   	}

// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setAdiantamentoService(AdiantamentoService service, CompromissoService comService, ParcelaService parService) {
 		this.service = service;
 		this.comService = comService;
 		this.parService = parService;
 	}

 	public void setTipo(String tipo) {
 		this.tipo = tipo;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas  
 	private void initializeNodes() {
		labelTitulo.setText("Adiantamento");
		tableColumnNumeroAdi.setCellValueFactory(new PropertyValueFactory<>("NumeroAdi"));
		tableColumnNomeFunAdi.setCellValueFactory(new PropertyValueFactory<>("nomeFun"));
		tableColumnDataAdi.setCellValueFactory(new PropertyValueFactory<>("dataAdi"));
		Utils.formatTableColumnDate(tableColumnDataAdi, "dd/MM/yyyy");
		tableColumnCargoAdi.setCellValueFactory(new PropertyValueFactory<>("cargoFun"));
		tableColumnSituacaoAdi.setCellValueFactory(new PropertyValueFactory<>("situacaoFun"));
		tableColumnValeAdi.setCellValueFactory(new PropertyValueFactory<>("ValeAdi"));
		Utils.formatTableColumnDouble(tableColumnValeAdi, 2);
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewAdiantamento.prefHeightProperty().bind(stage.heightProperty());
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
 		labelUser.setText(user);
 	 	Date dataHoje = new Date();
 		Calendar cal = Calendar.getInstance();
 		cal.setTime(dataHoje);
 		int mesHj = cal.get(Calendar.MONTH) + 1;
 		int anoHj = cal.get(Calendar.YEAR);
 		List<Adiantamento> list = new ArrayList<>();
		list = service.findMesTipo(mesHj, anoHj, tipo);
  		obsList = FXCollections.observableArrayList(list);
  		tableViewAdiantamento.setItems(obsList);
		initRemoveButtons();
	}
 	
/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	@SuppressWarnings("static-access")
	private void createDialogForm(Adiantamento obj, Funcionario objFun, Compromisso objCom, 
			ParPeriodo objPer, Parcela objPar, TipoConsumo objTip, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
// referencia para o controlador = controlador da tela carregada fornListaForm			
			AdiantamentoFormController controller = loader.getController();
			controller.user = user;
			controller.numEmp = numEmp;
 // injetando passando parametro obj 			
			controller.setAdiantamento(obj, objFun, objCom, objPer, objTip);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new AdiantamentoService(), new FuncionarioService(), new FornecedorService(), 
					new CompromissoService(), new TipoConsumoService(), new ParPeriodoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Adiantamento                                             ");
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
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/* * metodo p/ botao remove do frame
 * ele cria bot�o em cada linha 
 * o cell instancia e cria
*/  
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Adiantamento, Adiantamento>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Adiantamento obj, boolean empty) { 
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

	private void removeEntity(Adiantamento obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
					int cod = 0;
					List<Compromisso> listCod = comService.findPesquisa("Adiantamento");
					for (Compromisso com : listCod) {
						cod = com.getCodigoFornecedorCom();
					}
					objCom = comService.findById(cod, obj.getNumeroAdi());
					if (objCom.getNnfCom() != null) {
						classe = "Parcela  Adi List";
						parService.removeNnf(objCom.getNnfCom(), objCom.getCodigoFornecedorCom());
						classe = "Compromisso  Adi List";
						comService.remove(objCom.getCodigoFornecedorCom(), objCom.getNnfCom());
					}	
					classe = "Adiantamento  Adi List";
					service.remove(obj.getNumeroAdi());
					updateTableView();
				}
				catch (DbException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				}
				catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
				}	
			}
		}
	}	
}
