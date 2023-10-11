package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.MesesService;
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
 
public class CartelaListPagoController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<CartelaPagante> tableViewCartelaPagante;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<CartelaPagante, Integer>  tableColumnNumeroCartelaPagante;
 	
 	@FXML
 	private TableColumn<CartelaPagante, Date>  tableColumnDataCartelaPagante;
 	
 	@FXML
 	private TableColumn<CartelaPagante, String>  tableColumnLocalCartelaPagante;
 	
   	@FXML
 	private TableColumn<CartelaPagante, Double> tableColumnValorCartelaPagante;
 	
   	@FXML
 	private TableColumn<CartelaPagante, String> tableColumnFormaCartelaPagante;
 	 	
 	@FXML
	private Button btMesAnoPag;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelMesPagto;

	public String user = "";
 	
// auxiliar 	
 	String classe = "Cartela List Pg";
 	String nomeTitulo = "Cartela Paga: ";
 	public static int mm = 0;
 	public static int aa = 0;
 	
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<CartelaPagante> obsList;
 
 // inje��o de dependenia sem implementar a classe (instanciat)
 // acoplamento forte - implementa via set
 	private CartelaPaganteService service;

 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setCartelaPaganteService(CartelaPaganteService service) {
	 		this.service = service;
 	}

	@FXML
	public void onBtMesAnoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Cartela Paga: ";
		Meses objMes = new Meses();
		Anos objAno = new Anos();
		classe = "Mes e Ano Cart List Pg";
  		createDialogOpcao(objMes, objAno, "/gui/sgb/CartelaMesAnoForm.fxml", (parentStage), 
  				(CartelaMesAnoFormController contP) -> {
  		contP.setMesAno(objMes, objAno);
  		contP.setServices(new MesesService(), new AnosService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
  		});
		updateTableView();
  	}
  
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
 		tableColumnNumeroCartelaPagante.setCellValueFactory(new PropertyValueFactory<>("cartelaIdOrigemPag"));
		tableColumnDataCartelaPagante.setCellValueFactory(new PropertyValueFactory<>("dataCartelaPag"));
		Utils.formatTableColumnDate(tableColumnDataCartelaPagante, "dd/MM/yyyy");
		tableColumnLocalCartelaPagante.setCellValueFactory(new PropertyValueFactory<>("localCartelaPag"));
 		tableColumnValorCartelaPagante.setCellValueFactory(new PropertyValueFactory<>("ValorCartelaPag"));
 		Utils.formatTableColumnDouble(tableColumnValorCartelaPagante, 2);
   		tableColumnFormaCartelaPagante.setCellValueFactory(new PropertyValueFactory<>("FormaCartelaPag"));
   		
 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCartelaPagante.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de seguran�a p/ servi�o vazio
 *  criando uma lista para CartelaPagante os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço  está vazio");
 		}
		if (mm > 0) {
	  		String tabelaMeses = "Janeiro de, Fevereiro de, Março de, Abril de, Maio de, Junho de, Julho de, "
		         	+ "Agosto de, Setembro de, Outubro de, Novembro de, Dezembro de ";
	  		String[] tabMes = tabelaMeses.split(",");
	  		String nomeMes = tabMes[mm - 1];
			List<CartelaPagante> list = new ArrayList<>();
			list = service.findByMesAnoPago(mm, aa, "P");
			if (list.size() == 0) {
				Alerts.showAlert("Cartela Paga por período ", "Período ", "Não há Cartela Paga no período ", AlertType.INFORMATION);
			} else  {	
				labelTitulo.setText(String.format("%s%s%s%d ", nomeTitulo, nomeMes, " ", aa));
				obsList = FXCollections.observableArrayList(list);
				tableViewCartelaPagante.setItems(obsList);
			}	
 		}
	}
 	
	private synchronized <T> void createDialogOpcao(Meses objMes, Anos objAno, 
			String absoluteName,  Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
// lista da classe subject (form) - guarda lista de obj p/ CartelaPagante e emitir o evento
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

 }
