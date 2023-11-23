package gui.sgb;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.FechamentoAno;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FechamentoAnoService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import gui.util.DataStaticSGB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FechamentoAnoListController implements Initializable, DataChangeListener, Serializable {

	private static final long serialVersionUID = 1L;

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FechamentoAnoService service;
//	private FechamentoAno entity;
	
	@FXML
 	private TableView<FechamentoAno> tableViewAnual;

// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnCartelaFechamentoAno;

   	@FXML
 	private TableColumn<FechamentoAno, String> tableColumnDataFechamentoAno;
 	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnSituacaoFechamentoAno;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorCartelaFechamentoAno;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorProdutoFechamentoAno;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorComissaoFechamentoAno;
   	
   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorResultadoFechamentoAno;

   	@FXML
   	private TableColumn<FechamentoAno, String> tableColumnValorAcumuladoFechamentoAno;   	
   	
	@FXML
	private Label labelTitulo;
	
	@FXML
	private Label labelUser;

// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FechamentoAno> obsList;
 
// auxiliar
 	String classe = "Dados Fechamento Ano";
 	Integer numAno = null;
 	int flagStart = 0;
	public String user = "";
		 	 
 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setServices(FechamentoAnoService service) {
 		this.service = service;
 	}
 	
	public void montaForm() {
		service.zeraAll();
		flagStart = 1;
		Optional<ButtonType> result = Alerts.showConfirmation("Processamento", "<<<aguarde>>>");
		if (result.get() == ButtonType.OK) {
			FechamentoAno obj = new FechamentoAno();
			Funcionario objFun = new Funcionario();
			FechamentoAnoFormController contF = new FechamentoAnoFormController();
			contF.setDadosEntitys(obj, objFun);
			contF.setServices(new FechamentoAnoService(), new AdiantamentoService(), new CartelaService(), 
					new CartelaVirtualService(), new FuncionarioService(), new TipoConsumoService(), 
					new ParcelaService());
			contF.subscribeDataChangeListener(this);
			contF.onBtOkAction(null);
			contF.updateFormData();
		}	
		updateTableView();
		service.zeraAll();
	}

 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
 	}

	// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Fechamento Anual ");
		tableColumnCartelaFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("CartelaFechamentoAno"));
		tableColumnDataFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("DataFechamentoAno"));
		tableColumnSituacaoFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("SituacaoFechamentoAno"));
		tableColumnValorCartelaFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("ValorCartelaFechamentoAno"));
		tableColumnValorProdutoFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("ValorProdutoFechamentoAno"));
		tableColumnValorComissaoFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoFechamentoAno"));
		tableColumnValorResultadoFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoFechamentoAno"));
		tableColumnValorAcumuladoFechamentoAno.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoFechamentoAno"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewAnual.prefHeightProperty().bind(stage.heightProperty());
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
 		if (numAno == null) {
 			LocalDate ldt = DataStaticSGB.criaLocalAtual();
 			numAno = DataStaticSGB.anoDaData(ldt);
 		}	
 		labelUser.setText(user); 		
		List<FechamentoAno> list = new ArrayList<>();
		labelTitulo.setText("Fechamento anual : " + numAno);
		classe = "Fechamento anual ";
		list = service.findAll();
		if (list.size() == 0 && flagStart == 0) {
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, null, null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, "processando", null, null, null));
			list.add(new FechamentoAno(null, null, null, null, null, "<<aguarde>>", null, null, null));
		}
		obsList = FXCollections.observableArrayList(list);
		tableViewAnual.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
