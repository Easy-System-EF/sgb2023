package gui.sgb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Alerts;
import gui.util.Utils;
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

public class ProdutoMVRListController implements Initializable {

		private ProdutoService service;
		
		@FXML
	 	private TableView<Produto> tableViewProduto;
	 	
	 	@FXML
	 	private TableColumn<Produto, Integer>  tableColumnCodigoProd;
	 	
	 	@FXML
	 	private TableColumn<Produto, String> tableColumnNomeProd;

	   	@FXML
	 	private TableColumn<Produto, Double> tableColumnPercentualProd;
	 	
	   	@FXML
	 	private TableColumn<Produto, Double> tableColumnLetraProd;
	 	
		@FXML
		private Label labelUser;

		// Auxiliar
	 	public static Integer nivel = null;
	 	String classe = "Produto List ";
		public String user = "usuário";		
		int ok = 0;
	 	
	 	private ObservableList<Produto> obsList;
	 
	 	public void setProdutoService(ProdutoService service) {
	 		this.service = service;
	 	}

	 	@Override
		public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
	 	}

	// comportamento padr�o para iniciar as colunas 	
	 	private void initializeNodes() {
			tableColumnCodigoProd.setCellValueFactory(new PropertyValueFactory<>("codigoProd"));
	  		tableColumnNomeProd.setCellValueFactory(new PropertyValueFactory<>("nomeProd"));
			tableColumnPercentualProd.setCellValueFactory(new PropertyValueFactory<>("PercentualProd"));
			Utils.formatTableColumnDouble(tableColumnPercentualProd, 2);
			tableColumnLetraProd.setCellValueFactory(new PropertyValueFactory<>("LetraProd"));
	 		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
			Stage stage = (Stage) MainSgb.getMainScene().getWindow();
			tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
	 	}

		public void mvrForm() {
			Optional<ButtonType> result = Alerts.showConfirmation("Pode demorar um pouco", "Confirma?");
			if (result.get() == ButtonType.OK) {
				ProdutoMVRForm mvr = new ProdutoMVRForm();
				mvr.setProdutoMVRService(service);
				mvr.materialCustoEstoque();
				mvr.materialPercentual();
				mvr.materialClassifica();
				ok = 1;
			} else {
				ok = 2;
			}	
			updateTableView();
		}
		
		public void updateTableView() {
	 		if (service == null) {
				throw new IllegalStateException("Serviço está vazio");
	 		}
	 		labelUser.setText(user);
 			List<Produto> list = new ArrayList<>();
 			char nada = ' ';
 			if (ok == 0) {
 				list.add(new Produto(null, null, null, null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, null, null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, null, null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, null, null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, null, null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, "                       processando", null, null, null, null, null, null, null, null, 0.0, nada));
 				list.add(new Produto(null, null, "                    <<<aguarde>>>", null, null, null, null, null, null, null, null, 0.0, nada));
 			}
	 		if (ok == 1) {
	 			list = service.findMVR();
	 			list.removeIf(x -> x.getPercentualProd() == 0);
	 		}	
	 		if (ok > 1) {
	 			list = service.findPesquisa("xyzwst");
	 		}
	 		obsList = FXCollections.observableArrayList(list);
			tableViewProduto.setItems(obsList);
		}
}
