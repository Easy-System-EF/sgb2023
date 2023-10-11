package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.dao.CartelaCommitDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.Cliente;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.ClienteService;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConvenioListAbertoController implements Initializable, DataChangeListener {

	@FXML
	private TableView<Cartela> tableViewCartela;

	@FXML
	private Label labelTitulo;

	@FXML
	private TableColumn<Cartela, Integer> tableColumnNumeroCartela;

	@FXML
	private TableColumn<Cartela, Date> tableColumnDataCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnLocalCartela;

	@FXML
	private TableColumn<Cartela, Double> tableColumnTotalCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnNomeSituacaoCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnClienteCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnObsCartela;

	@FXML
	private TableColumn<Cartela, Cartela> tableColumnBaixa;

	@FXML
	private Button btCliente;

	@FXML
	private Button btBaixa;

	@FXML
	private Label labelUser;

	public String user = "";

// auxiliar 	
	String classe = "Convênio List Ab";
	String nomeTitulo = "Convênio em aberto";
	public static String nomeCli = null;
	public static String forma = null;
	public static Integer numEmp = null;

// carrega aqui os dados Updatetableview (metodo)
	private ObservableList<Cartela> obsList;

	// inje��o de dependenia sem implementar a classe (instanciat)
	// acoplamento forte - implementa via set
	
	private CartelaService service;

// injeta a dependencia com set (invers�o de controle de inje�ao)	
	public void setCartelaServices(CartelaService service) {
		this.service = service;
	}

	@FXML
	public void onBtClienteAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Convênio em Aberto";
		Cliente cli = new Cliente();
  		createDialogForm(cli, "/gui/sgb/ConvenioForm.fxml", parentStage, 
  				(ConvenioFormController cont) -> {
			cont.setCliente(cli);
			cont.setService(new ClienteService());
			cont.loadAssociatedObjects();
			cont.updateFormData();
 		});
  		updateTableView();
	}

	@FXML
	public void onBtBaixaAction(ActionEvent event) {
		if (service == null) {
			throw new IllegalStateException("Serviço cartela nulo");
		}
		try {
			nomeTitulo = "Convênio em Aberto";
			if (nomeCli != null && forma != null) {
				String letraSit = "P";
				String nomeSit = "Pago";
				String baixa = "Sim";
				CartelaCommitDao commitDao = DaoFactory.createCartelaCommitDao();
				Cartela car = new Cartela();
				List<Cartela> listCar = service.findClienteAberto(nomeCli);
				if (listCar.size() == 0) {
					Alerts.showAlert(null, "Não existe cartela em aberto para o Cliente ", null, AlertType.INFORMATION);
				} else {
					for (Cartela c : listCar) {
						if (car.getNumeroCar() == null) {
							if (c.getSituacaoCar().equals("V")) {
								car = c;
								commitDao.gravaCartelaCommit(car, letraSit, nomeSit, baixa, numEmp, forma);
								nomeCli = null;
							}	
						}	
					}
				}
			} else {
					Alerts.showAlert(null, "Tem que identificar Cliente ", null, AlertType.INFORMATION);				
			}
			updateTableView();
		}
		catch (Exception e) {
			throw new DbException("Commit convênio " + e.getMessage());
		}
	}

	// inicializar as colunas para iniciar nossa tabela initializeNodes
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

// comportamento padr�o para iniciar as colunas 	
	private void initializeNodes() {
		labelTitulo.setText(String.format("%s ", nomeTitulo));
		tableColumnNumeroCartela.setCellValueFactory(new PropertyValueFactory<>("numeroCar"));
		tableColumnDataCartela.setCellValueFactory(new PropertyValueFactory<>("dataCar"));
		Utils.formatTableColumnDate(tableColumnDataCartela, "dd/MM/yyyy");
		tableColumnLocalCartela.setCellValueFactory(new PropertyValueFactory<>("localCar"));
		tableColumnTotalCartela.setCellValueFactory(new PropertyValueFactory<>("totalCar"));
		Utils.formatTableColumnDouble(tableColumnTotalCartela, 2);
		tableColumnNomeSituacaoCartela.setCellValueFactory(new PropertyValueFactory<>("nomeSituacaoCar"));
		tableColumnClienteCartela.setCellValueFactory(new PropertyValueFactory<>("clienteCar"));
		tableColumnObsCartela.setCellValueFactory(new PropertyValueFactory<>("obsCar"));

		// para tableview preencher o espa�o da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCartela.prefHeightProperty().bind(stage.heightProperty());
	}

	/*
	 * carregar o obsList para atz tableview tst de seguran�a p/ servi�o vazio
	 * criando uma lista para Cartela os services instanciando o obsList acrescenta
	 * o botao edit e remove
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço  está vazio");
		}			
		labelTitulo.setText(String.format("%s ", nomeTitulo));
		List<Cartela> list = new ArrayList<>();
		if (nomeCli != null) {
			list = service.findClienteAberto(nomeCli);
			if (list.size() == 0 && nomeCli != null) {
				Alerts.showAlert(null, "Não existe convênio em aberto para o Cliente ", null, AlertType.INFORMATION);
			}	
		}
  		if (nomeCli != null) {
  			double tot = service.sumCliente(nomeCli);
  			if (tot > 0) {
  				Date dt = new Date();
  				list.add(new Cartela(null, dt, null, null, tot, null, null, null, null, null, "<== Total Cliente", 
  						null, null, null, null, null, null, null, null));
  			}	
  		}	
		obsList = FXCollections.observableArrayList(list);
		tableViewCartela.setItems(obsList);
	}

// lista da classe subject (form) - guarda lista de obj p/ Cartela e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * ebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private <T> void createDialogForm(Cliente cli, String absoluteName, Stage parentStage, Consumer<T> InitializeAction) {
		try {
			classe = "convenio List Ab";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			InitializeAction.accept(cont);

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
}
