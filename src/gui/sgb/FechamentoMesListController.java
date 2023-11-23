package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.FechamentoMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FechamentoMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class FechamentoMesListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FechamentoMesService service;
	
	@FXML
 	private TableView<FechamentoMes> tableViewFechamentoMes;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnCartelaFechamentoMes;

   	@FXML
 	private TableColumn<FechamentoMes, String> tableColumnDataFechamentoMes;
 	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnSituacaoFechamentoMes;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorCartelaFechamentoMes;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorProdutoFechamentoMes;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorComissaoFechamentoMes;
   	
   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorResultadoFechamentoMes;

   	@FXML
   	private TableColumn<FechamentoMes, String> tableColumnValorAcumuladoFechamentoMes;   	
   	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FechamentoMes> obsList;
 
 	public static String mesNome = "";
 	
// auxiliar
 	String classe = "Fechamento Mes List";
 	int flagStart = 0;
 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesFechamentoMesAction(ActionEvent event) {
		updateTableView();
		flagStart = 1;
		Stage parentStage = Utils.currentStage(event);
		FechamentoMes obj = new FechamentoMes();
		Funcionario objFun = new Funcionario();
		classe = "Meses Fechamento Mes List";		
  		createDialogOpcao("/gui/sgb/FechamentoMesForm.fxml", parentStage, 
  				(FechamentoMesFormController contF) -> {
			contF.setFechMesEntityes(obj, objFun);
			contF.setServices(new FechamentoMesService(),
						      new AdiantamentoService(),
							  new MesesService(),
							  new AnosService(),
							  new CartelaService(),
							  new CartelaVirtualService(),
							  new FuncionarioService(),
							  new TipoConsumoService(),
							  new ParcelaService());
			contF.loadAssociatedObjects();
			contF.updateFormData();
 		});
		updateTableView();
		service.zeraAll();
		flagStart = 0;
		mesNome = null;
	}
	 	 	
 	private <T> void createDialogOpcao(String absoluteName, Stage parentStage, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Fechamento Mes              ");
		tableColumnCartelaFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("CartelaFechamentoMes"));
		tableColumnDataFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("DataFechamentoMes"));
//		Utils.formatTableColumnDate(tableColumnDataFechamentoMes, "dd/MM/yyyy");
		tableColumnSituacaoFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("SituacaoFechamentoMes"));
		tableColumnValorCartelaFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("ValorCartelaFechamentoMes"));
		tableColumnValorProdutoFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("ValorProdutoFechamentoMes"));
		tableColumnValorComissaoFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoFechamentoMes"));
		tableColumnValorResultadoFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoFechamentoMes"));
		tableColumnValorAcumuladoFechamentoMes.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoFechamentoMes"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewFechamentoMes.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Dados está vazio");
 		}
 		labelUser.setText(user);
		classe = "Fechamento Mes List";
		List<FechamentoMes> list = service.findAll();
		if (list.size() == 0 && flagStart == 0) {
			labelTitulo.setText("Fechamento Mes             ");
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, "processando", null, null, null));
			list.add(new FechamentoMes(null, null, null, null, null, "<<aguarde>>", null, null, null));
		} else {
			labelTitulo.setText("Fechamento Mes " + mesNome);
		}	
		obsList = FXCollections.observableArrayList(list);
		tableViewFechamentoMes.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
