package gui.sgcp;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ParcelaListPagoController implements Initializable, DataChangeListener {

	@FXML
	private Label labelTitulo;

	@FXML
	private TableView<Parcela> tableViewParcela;

	@FXML
	private TableColumn<Parcela, String> tableColumnFornecedor;

	@FXML
	private TableColumn<Parcela, Integer> tableColumnNNF;

	@FXML
	private TableColumn<Parcela, Date> tableColumnVencimento;

	@FXML
	private TableColumn<Parcela, Integer> tableColumnParcela;

	@FXML
	private TableColumn<Parcela, Double> tableColumnValor;

	@FXML
	private TableColumn<Parcela, Double> tableColumnJuros;

	@FXML
	private TableColumn<Parcela, Double> tableColumnDesconto;

	@FXML
	private TableColumn<Parcela, Double> tableColumnAPagar;

	@FXML
	private TableColumn<Parcela, Double> tableColumnPago;

	@FXML
	private TableColumn<Parcela, Date> tableColumnPagamento;

	@FXML
	private TableColumn<Parcela, Double> tableColumnTotal;

	@FXML
	private Button btPeriodo;

	@FXML
	private Button btFornecedor;

	@FXML
	private Button btTipo;

	@FXML
	private Label labelUser;

	/*
	 * parametros de find... na updateTable 1 = geral aberto 2 = geral pago
	 */
	/*
	 * periodo, fornecedor, tipo p = periodo aberto q = periodo pago f = fornecedor
	 * aberto g = fornecedor pago t = tipo aberto u = tipo pago
	 */

	public String user = "";
	String nomeTitulo = "Consulta Contas Pagas";
	String classe = "Parcela List Pago ";
	public static Integer codigo = null;
 	char opcao = 'o';
	int tipo = 0;

// carrega aqui Updatetableview (metodo)
	private ObservableList<Parcela> obsListPar;

// inje��o de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set

	private ParcelaService parService;
	Parcela parcela = new Parcela();

	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}

// 	busca os dados no bco de dados	
	public void setParcelaService(ParcelaService parService) {
		this.parService = parService;
	}

// 	busca os dados no bco de dados	
	Fornecedor fornecedor = new Fornecedor();

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	TipoConsumo tipoConsumo = new TipoConsumo();

	public void setTipoConsumo(TipoConsumo tipoConsumo) {
		this.tipoConsumo = tipoConsumo;
	}

	private ParPeriodoService perService;
	ParPeriodo parPeriodo = new ParPeriodo();

	public void setParPeriodoService(ParPeriodoService perService) {
		this.perService = perService;
	}

	public void setParPeriodo(ParPeriodo parPeriodo) {
		this.parPeriodo = parPeriodo;
	}

	/*
	 * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
	 * com currentStage - janela pai - parentstage vamos abrir o forn form
	 */

 	ParPeriodo obj1 = new ParPeriodo();
	Fornecedor obj3 = new Fornecedor();
	TipoConsumo obj4 = new TipoConsumo();
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('q');
		nomeTitulo = "Consulta Contas pagas no Periodo";
  		createDialogForms("/gui/sgcp/ParPeriodoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParPeriodoFormController contP) -> {
		contP.setPeriodo(obj1);
		contP.setPeriodoService(new ParPeriodoService());
   		});
 	}
 
 	public void onBtFornecedorAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
  		setOpcao('g');
		nomeTitulo = "Consulta Contas pagas por Fornecedor";
  		createDialogForms("/gui/sgcp/ParFornecedorForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParFornecedorFormController contF) -> {
			contF.setFornecedor(obj3);
			contF.setService(new FornecedorService());
 		});
		updateTableViewPago();
	}
 	
	@FXML
	public void onBtTipoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('u');
		nomeTitulo = "Consulta Contas pagas por Tipo";
  		createDialogForms("/gui/sgcp/ParTipoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParTipoFormController contP) -> {
  		contP.setPeriodo(obj1);
  		contP.setPeriodoService(new ParPeriodoService(), new TipoConsumoService());
  		});
  		}
  
	// inicializar as colunas para iniciar nossa tabela initializeNodes
	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		initializeNodes();
	}

// comportamento padr�o para iniciar as colunas 	
	private void initializeNodes() {  
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		tableColumnFornecedor.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedorPar"));
		tableColumnNNF.setCellValueFactory(new PropertyValueFactory<>("nnfPar"));
		tableColumnVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoPar"));
		Utils.formatTableColumnDate(tableColumnVencimento, "dd/MM/yyyy");
		tableColumnParcela.setCellValueFactory(new PropertyValueFactory<>("numeroPar"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorPar"));
		Utils.formatTableColumnDouble(tableColumnValor, 2);
		tableColumnJuros.setCellValueFactory(new PropertyValueFactory<>("jurosPar"));
		Utils.formatTableColumnDouble(tableColumnJuros, 2);
		tableColumnDesconto.setCellValueFactory(new PropertyValueFactory<>("descontoPar"));
		Utils.formatTableColumnDouble(tableColumnDesconto, 2);
		tableColumnAPagar.setCellValueFactory(new PropertyValueFactory<>("totalPar"));
		Utils.formatTableColumnDouble(tableColumnAPagar, 2);
		tableColumnPago.setCellValueFactory(new PropertyValueFactory<>("pagoPar"));
		Utils.formatTableColumnDouble(tableColumnPago, 2);
		tableColumnPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoPar"));
		Utils.formatTableColumnDate(tableColumnPagamento, "dd/MM/yyyy");
		tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("ResultadoParStr"));
//		Utils.formatTableColumnDouble(tableColumnTotal, 2);
		// para tableview preencher o espa�o da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewParcela.prefHeightProperty().bind(stage.heightProperty());
	}
	
	int setTipo(Integer num) {
		return tipo = num;
	}

	char setOpcao(char letra) {
		return opcao = letra;
	}
	
 	/*
	 * carregar o obsList para atz tableview tst de seguran�a p/ servi�o vazio
	 * criando uma lista para receber os s instanciando o obsList acrescenta o botao
	 * edit e remove
	 */

	public void updateTableViewPago() {
		if (parService == null) {
			throw new IllegalStateException("Serviço está vazio");
		}
		labelUser.setText(user);
		if (opcao == 'o') {
			porPeriodo();
		}
  		somaTotal();
		tableViewParcela.setItems(obsListPar);
	}

	private void porPeriodo() {
		try {
			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");

			ParPeriodo per = new ParPeriodo();
			per = parPeriodo;
			List<ParPeriodo> listPerio = perService.findAll();
			for (ParPeriodo pe : listPerio) {
				per.setFornecedor(pe.getFornecedor());
				per.setTipoConsumo(pe.getTipoConsumo());
			}
			per.setIdPeriodo(1);
			per.setDtiPeriodo(dti);
			per.setDtfPeriodo(dtf);
			perService.update(per);
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("ParseException ", "Erro Data ", e.getMessage(), AlertType.ERROR);
		}
	}

	private void somaTotal() {
		double soma = 0.00;
		@SuppressWarnings("unused")
		int codFor = 0;
		int codTipo = 0;
		List<ParPeriodo> listPer = perService.findAll();
		for (ParPeriodo per : listPer) {
			codFor = per.getFornecedor().getCodigo();
			codTipo = per.getTipoConsumo().getCodigoTipo();
		}

		List<Parcela> list = new ArrayList<>();
 		if (opcao == 'o') {
			list = parService.findAllPago();
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela paga ", AlertType.INFORMATION);
	 		}
		}
 		if (opcao == 'q') {
			list = parService.findPeriodoPago();
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela paga ", AlertType.INFORMATION);
	 		}
		}
 		if (opcao == 'g') {
 			if (codigo != null) {
 				list = parService.findByIdFornecedorPago(codigo); 
 				if (list.size() == 0) {
 					Alerts.showAlert("Parcela ", null, "Não há parcela paga ", AlertType.INFORMATION);
 					codigo = null;
 				}
 			}	
		}
 		if (opcao == 'u') {
			list = parService.findByIdTipoPago(codTipo);
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela paga ", AlertType.INFORMATION);
	 		}
		}

 		DecimalFormat df = new DecimalFormat("##,##0.00");
 	   	String resultadoParStr = ""; 

 	   	for (Parcela p : list) {
			if (p.getResultadoPar() == null) {
				p.setResultadoPar(0.00);
			}
			soma = soma + p.getPagoPar();
 			p.setResultadoPar(soma);
  	 	   	resultadoParStr = df.format(p.getResultadoPar());
 	 	   	p.setResultadoParStr(resultadoParStr);
 		}
		obsListPar = FXCollections.observableArrayList(list);
	}

	private synchronized <T> void createDialogForms(String absoluteName, ParPeriodo obj1, 
   			Fornecedor obj3, TipoConsumo obj4, Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			if (opcao == 'g')
			{	ParFornecedorFormController controllerF = loader.getController();
 				controllerF.loadAssociatedObjects();
				controllerF.subscribeDataChangeListener(this);
				controllerF.updateFormData();
				classe = "Fornecedor Parc List Pago";
			}
			if (opcao == 'q')
			{	ParPeriodoFormController controllerP = loader.getController();;
 				controllerP.loadAssociatedObjects();
				controllerP.subscribeDataChangeListener(this);
				controllerP.updateFormData();
				classe = "Periodo Parc List Pago ";
			}	
 			if (opcao == 'u')
			{	ParTipoFormController controllerT = loader.getController();
				controllerT.loadAssociatedObjects();
				controllerT.subscribeDataChangeListener(this);
				controllerT.updateFormData();
				classe = "Tipo Fornecedor Parc List Pago";
   			}	

 			Stage dialogStage = new Stage();
			if (opcao == 'g')
			{	dialogStage.setTitle("Selecione Fornecdor                                             ");
			}
			if (opcao == 'u')
			{	dialogStage.setTitle("Selecione Tipo                                             ");
			}
			if (opcao == 'q')
			{	dialogStage.setTitle("Selecione Periodo                                             ");
			}
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}
 
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
 		labelTitulo.setText(String.format(nomeTitulo));
		updateTableViewPago();
	}
}