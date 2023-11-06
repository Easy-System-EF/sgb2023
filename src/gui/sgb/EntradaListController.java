package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParcelaService;
import gui.util.Alerts;
import gui.util.CalculaParcela;
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
 
public class EntradaListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private EntradaService service;
	private ProdutoService prodService;
	private ParcelaService parService = new ParcelaService();
	
	@FXML
 	private TableView<Entrada> tableViewEntrada;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Entrada, Date> tableColumnDataEnt; 
 	
 	@FXML
 	private TableColumn<Entrada, Integer>  tableColumnNumeroEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeFornEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeProdEnt;

   	@FXML
 	private TableColumn<Entrada, Double> tableColumnQtdProdEnt;
 	
   	@FXML
 	private TableColumn<Entrada, Double> tableColumnVlrProdEnt;
 	
  	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnEDITA;

 	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnREMOVE ;

 	@FXML
 	private Button btNewEnt;
 	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";
 	public Integer nivel = 0;
 	String classe = "Entrada List";
 	
// carrega aqui Updatetableview (metodo)
 	private ObservableList<Entrada> obsList;
  
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
		Produto objProd = new Produto();
		Fornecedor objForn = new Fornecedor();
		Compromisso objCom = new Compromisso();
		ParPeriodo objPer = new ParPeriodo();
		Parcela objPar = new Parcela();
		TipoConsumo objTipo = new TipoConsumo();

 	@FXML
  	public void onBtNewEntAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		Entrada obj = new Entrada();
 		createDialogForm(obj, objProd, objCom, objPer, objPar, objForn, objTipo, "/gui/sgb/EntradaForm.fxml", parentStage);
   	}

 	// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setProdutoService(ProdutoService prodService) {
 		this.prodService = prodService;
 	}

 	public void setEntradaService(EntradaService service) {
 		this.service = service;
 	}

// inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroEnt.setCellValueFactory(new PropertyValueFactory<>("numeroEnt"));
		tableColumnDataEnt.setCellValueFactory(new PropertyValueFactory<>("dataEnt"));
		Utils.formatTableColumnDate(tableColumnDataEnt, "dd/MM/yyyy");
		tableColumnNomeFornEnt.setCellValueFactory(new PropertyValueFactory<>("nomeFornEnt"));
		tableColumnNomeProdEnt.setCellValueFactory(new PropertyValueFactory<>("nomeProdEnt"));
 		tableColumnQtdProdEnt.setCellValueFactory(new PropertyValueFactory<>("quantidadeProdEnt"));
		Utils.formatTableColumnDouble(tableColumnQtdProdEnt, 2);
		tableColumnVlrProdEnt.setCellValueFactory(new PropertyValueFactory<>("valorProdEnt"));
		Utils.formatTableColumnDouble(tableColumnVlrProdEnt, 2);
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewEntrada.prefHeightProperty().bind(stage.heightProperty());
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
// aq vou buscar tds Id e madar pronto  		
 		List<Entrada> list = service.findAll();
   		obsList = FXCollections.observableArrayList(list);
 		tableViewEntrada.setItems(obsList);
 		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private synchronized void createDialogForm(Entrada obj, Produto prod, Compromisso objCom, ParPeriodo objPer, Parcela objPar, 
			Fornecedor objFor, TipoConsumo objTipo, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setObjects(obj, prod);
			controller.setServices(new EntradaService(), new FornecedorService(), new ProdutoService());
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Entrada                                             ");
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
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela ", e.getMessage(), AlertType.ERROR);
		}
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Entrada obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm(obj, objProd, objCom, objPer, objPar, 
		  			objForn, objTipo, "/gui/sgb/EntradaForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
 
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Entrada obj, boolean empty) { 
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

	private void removeEntity(Entrada obj) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operaçaoo não permitida", AlertType.INFORMATION);
		} else {
			classe = "Entrada List";
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
					String simNao = "sim";
					verificaPago(obj, simNao);
					if (simNao == "nao") {
						Alerts.showAlert("Parcela Paga", "Existe patcela(s) paga(s) ", "sem exclusão", AlertType.ERROR);
					}
					if (simNao == "sim") {
						if (obj.getForn().getCodigo() != null) {
							simNao = confereSaldo(obj.getNnfEnt(), obj.getForn().getCodigo(), simNao);
							acertaCompromisso(obj, simNao);
							saiProduto(obj.getNnfEnt(), obj.getForn().getCodigo(), simNao);
							service.remove(obj);
							updateTableView();
						}	
		   			}	
				}
				catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				}
			}
		}	
	}

	private String  verificaPago(Entrada obj, String simNao) {
		ParcelaService parService = new ParcelaService();
		List<Parcela> listPar = new ArrayList<>();
		listPar = parService.findByIdFornecedorNnf(obj.getForn().getCodigo(), obj.getNnfEnt());	
		if (listPar.size() != 0) {
			for (Parcela p : listPar) {
				if (p.getFornecedor().getCodigo().equals(obj.getForn().getCodigo()) || 
						p.getNnfPar().equals(obj.getNnfEnt())) {
					if (p.getPagoPar() > 0) {
						simNao = "nao";
					}	
				}
			}
		}
		return simNao;
	}
	
	private String confereSaldo(int nnf, int forn, String simNao) {
		Produto pro1 = new Produto();
		List<Entrada> listEnt2 = service.findByNnf(nnf);
		if (listEnt2.size() > 0) {
			for (Entrada e2 : listEnt2) {
				if (e2.getForn().getCodigo().equals(forn)) {
					pro1 = prodService.findById(e2.getProd().getCodigoProd());
					if (e2.getQuantidadeProdEnt() > pro1.getSaldoProd()) {
						Alerts.showAlert("Atenção ","Saldo é menor que a quantidade ", "sem exclusão ", AlertType.ERROR);
						simNao = "nao";
					}	
				}
			}
		}	
		return simNao;
	}	
	
	private void saiProduto(int nnf, Integer forn, String simNao) {
		if (simNao == "sim") {
			Produto pro2 = new Produto();
			List<Entrada> listEnt2 = service.findByNnf(nnf);
			for (Entrada e2 : listEnt2) {
				if (e2.getForn().getCodigo() == forn) {
					pro2 = prodService.findById(e2.getProd().getCodigoProd()); 
					if (e2.getQuantidadeProdEnt() <= pro2.getSaldoProd()) {
						pro2.saidaSaldo(e2.getQuantidadeProdEnt());
						pro2.getSaldoProd();
						pro2.setSaidaCmmProd(-1 * e2.getQuantidadeProdEnt());
						prodService.saveOrUpdate(pro2);
					}	
				}
			}
		}	
	}	
	
	private void acertaCompromisso(Entrada obj, String simNao){
		if (simNao == "sim") {
			Compromisso com = new Compromisso();
			CompromissoService comService = new CompromissoService();
			double vlrFim = 0.00;
			double vlrMat = 0.99;
			com = comService.findById(obj.getForn().getCodigo(), obj.getNnfEnt());
			if (com != null) {
				vlrMat = (obj.getQuantidadeProdEnt() * obj.getValorProdEnt());
				if (com.getValorCom() >= vlrMat) {
					vlrFim = com.getValorCom() - vlrMat;
					com.setValorCom(vlrFim);
				}	
			}	
			if (com != null) {
				if (com.getPrazoCom() == 1 && com.getParcelaCom() == 1) {
					com.setSituacaoCom(1);
				} else {
					com.setSituacaoCom(0);
				}
				if (com.getValorCom() > 0.00) {
					comService.saveOrUpdate(com);
					CalculaParcela.parcelaCreate(com);
				} else {	
					parService.removeNnf(obj.getNnfEnt(), obj.getForn().getCodigo());
					comService.remove(obj.getForn().getCodigo(), obj.getNnfEnt());
				}	
			}	
		}
	}
 }
