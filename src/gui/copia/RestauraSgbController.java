package gui.copia;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import gui.copia.Volta.RestauraAdiantamento;
import gui.copia.Volta.RestauraCargo;
import gui.copia.Volta.RestauraCartela;
import gui.copia.Volta.RestauraCliente;
import gui.copia.Volta.RestauraCompromisso;
import gui.copia.Volta.RestauraEntrada;
import gui.copia.Volta.RestauraFornecedor;
import gui.copia.Volta.RestauraFuncionario;
import gui.copia.Volta.RestauraGrupo;
import gui.copia.Volta.RestauraLogin;
import gui.copia.Volta.RestauraPagante;
import gui.copia.Volta.RestauraParcela;
import gui.copia.Volta.RestauraPeriodo;
import gui.copia.Volta.RestauraProduto;
import gui.copia.Volta.RestauraSituacao;
import gui.copia.Volta.RestauraTipo;
import gui.copia.Volta.RestauraVirtual;
import gui.listerneres.DataChangeListener;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RestauraSgbController implements Initializable, DataChangeListener {

	@FXML
	private VBox vBoxRestaura;

	@FXML
	private Button buttonOk;

 	@FXML
	private Label labelFile;

	@FXML
	private Label labelUser;

	@FXML
	private Label labelCount;

	@FXML
	private TableView<Restaura> tableViewRestaura;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnFile;

 	@FXML
 	private TableColumn<Restaura, String> tableColumnStatus;

 	@FXML
 	private TableColumn<Restaura, Integer> tableColumnCount;

 	private ObservableList<Restaura> obsList;

 	int tam = 0;
	Integer count = 0;
	Integer countAk = 0;
	String status = "";
	String crip = null;
    String classe = "BackUp SGB ";
	Date dataI = new Date(System.currentTimeMillis());
	Date dataF = new Date();
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	Calendar cal = Calendar.getInstance();
	static String meioSgb = ":\\Arqs\\Backup\\SGB\\";
	public String meioSgcp= ":\\Arqs\\Backup\\SGBCP\\";
	public static String ext = ".Bck";
	public static String file = "";
	public static String unid = null;
	public static String path = null;
 	public String user = "usuário";
 	
 	public RestauraService service;
 	private Restaura entity;
 	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

 	public void setRestauraService(RestauraService service) {
 		this.service = service;
 	}
 	
 	public void setEntity(Restaura entity) {
 		this.entity = entity;
 	}

 	@FXML
  	public void onBtOkAdiAction(ActionEvent event) throws ParseException {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 createDialogForm("/gui/copia/RestauraForm.fxml", parentStage);
 		 service.zeraAll();
 		 updateTableView();
 		 executaBack();
   	}
 	
 	public void executaBack() throws ParseException {
 		if (unid != null) {
 			count = 0;
 			countAk = 0;
 			cargo();
 			cliente();
 			grupo();
 			situacao();
 			tipoConsumo();
 			fornecedor();
			periodo();
			Optional<ButtonType> result = Alerts.showConfirmation("null", "Execute Scrypt II ");
			if (result.get() == ButtonType.OK) {
				produto();
				funcionario();
				adiantamento();
				cartela();
				cartelaVirtual();
				cartelaPagante();
				entrada();
				compromisso();
				parcela();
				login();
			}
			labelFile.setText("Kbou!!!");
			labelFile.viewOrderProperty();
			labelCount.viewOrderProperty();
 		}	
 	}

	public void situacao() {
		status = "Ok";
		file = "Situacao";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		countAk = RestauraSituacao.restauraSituacao(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
		
	public void adiantamento() throws ParseException {
		status = "Ok";
		file = "Adiantamento";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		countAk = RestauraAdiantamento.restauraAdiantamento(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void cargo() {
		status = "Ok";
		countAk = 0;
		file = "Cargo";
		path = unid + meioSgb + file + ext;
		countAk = RestauraCargo.restauraCargo(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void cliente() {
		status = "Ok";
		countAk = 0;
		file = "Cliente";
		path = unid + meioSgb + file + ext;
		countAk = RestauraCliente.restauraCliente(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void entrada() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Entrada";
		path = unid + meioSgb + file + ext;
		countAk = RestauraEntrada.restauraEntrada(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void funcionario() {
		status = "ok";
		countAk = 0;
		file = "Funcionario";
		path = unid + meioSgb + file + ext;
		countAk = RestauraFuncionario.restauraFuncionario(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void grupo() {
		status = "Ok";
		countAk = 0;
		file = "Grupo";
		path = unid + meioSgb + file + ext;
		countAk = RestauraGrupo.restauraGrupo(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
		}
		
	public void login() throws ParseException {
		status = "Ok";
		file = "Login";
		countAk = 0;
		path = unid + meioSgb + file + ext;
		countAk = RestauraLogin.restauraLogin(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void produto() throws ParseException {
		status = "Ok";
		countAk = 0;
		file = "Produto";
		path = unid + meioSgb + file + ext;
		countAk = RestauraProduto.restauraProduto(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void cartelaVirtual() {
		status = "Ok";
		file = "CartelaVirtual";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		countAk = RestauraVirtual.restauraVirtual(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}

	
	public void cartela() throws ParseException {
		status = "Ok";
		file = "Cartela";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		countAk = RestauraCartela.restauraCartela(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}
	
	public void cartelaPagante() throws ParseException {
		status = "Ok";
		file = "CartelaPagante";
		path = unid + meioSgb + file + ext;
		countAk = 0;
		countAk = RestauraPagante.restauraPagante(countAk, unid, meioSgb, file, ext);
		gravaRestaura();
	}

	public void compromisso() throws ParseException {
		status = "Ok";
		file = "Compromisso";
		path = unid + meioSgcp + file + ext;
		countAk = 0;
		countAk = RestauraCompromisso.restauraCompromisso(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
	
	public void fornecedor() {
		status = "Ok";
		countAk = 0;
		file = "Fornecedor";
		path = unid + meioSgcp + file + ext;
		countAk = RestauraFornecedor.restauraFornecedor(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	public void parcela() throws ParseException {
		status = "Ok";
		file = "Parcela";
		path = unid + meioSgcp + file + ext;
		countAk = 0;
		countAk = RestauraParcela.restauraParcela(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	public void periodo() throws ParseException {
		status = "Ok";
		countAk = 0;
		countAk = RestauraPeriodo.restauraPeriodo(countAk);
		gravaRestaura();
	}
			
	public void tipoConsumo() {
		status = "Ok";
		file = "TipoConsumidor";
		path = unid + meioSgcp+ file + ext;
		countAk = 0;
		countAk = RestauraTipo.restauraTipo(countAk, unid, meioSgcp, file, ext);
		gravaRestaura();
	}
		
	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

 	private void initializeNodes() {
		tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("FileUp"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("StatusUp"));
		tableColumnCount.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("CountUp")));
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewRestaura.prefHeightProperty().bind(stage.heightProperty());
 	}
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
		List<Restaura> listR = service.findAll();
 		labelUser.setText(user);
 		labelFile.setText("<<<aguarde>>>");
 		labelCount.setText(String.valueOf(count));
  		obsList = FXCollections.observableArrayList(listR);
  		tableViewRestaura.setItems(obsList);
	}

	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		initializeNodes();
	}
	
 	public void gravaRestaura() {
		entity.setIdRestauraUp(null);
		entity.setFileUp(file);
		entity.setStatusUp(status);
		entity.setCountUp(countAk);
		service.saveOrUpdate(entity);
		count += 1;
		labelCount.setText(String.valueOf(count));
		labelCount.viewOrderProperty();
		notifyDataChangeListerners();
		updateTableView();
 	}
 	
	private void createDialogForm(String absoluteName, Stage parentStage) {
        String classe = "Restaura SGB ";
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			RestauraSgbFormController controller = loader.getController();
			controller.loadAssociatedObjects();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Selecione unid para Restaurar                 ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 
}
