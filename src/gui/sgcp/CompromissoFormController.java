package gui.sgcp;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import gui.util.CalculaParcela;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;
 
public class CompromissoFormController implements Initializable {

 //	carrega os dados do formulario	
	private Compromisso entity;
	private Compromisso entOld;
	
 //	carrega dados do banco na cri��o do formulario - inje��o de dependencia
	private CompromissoService service;
 	private FornecedorService fornecedorService;
	private TipoConsumoService tipoService;
	private ParcelaService parService;
	private ParPeriodoService perService;
 
//	lista da classe subject - armazena os dados a serem atz no formulario 
//	recebe e emite eventos
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

   	@FXML
	private TextField textNnfCom;

	@FXML
	private DatePicker dpDataCom;
 
	@FXML
	private DatePicker dpDataVencimentoCom;
 
	@FXML
	private TextField textValorCom;

  	@FXML
	private TextField textParcelaCom;

  	@FXML 
	private TextField textPrazoCom;
  	
  	@FXML
  	private TextField textIniciais;

	@FXML
	private ComboBox<Fornecedor> comboBoxFornecedor;

	@FXML
	private ComboBox<TipoConsumo> comboBoxTipoFornecedor;

	@FXML
	private Label labelErrorNnf;

	@FXML
	private Label labelErrorDataCompra;

	@FXML
	private Label labelErrorDataVencimentoCompra;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorParcela;

	@FXML
	private Label labelErrorPrazo;
	
	@FXML
	private Button btPesq;

	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	@FXML
	private Button btSairCom;

	@FXML
	private Label labelUser;

	Integer flagD = 0;
	String classe = "Compromisso  Form";
	String pesquisa = "";
	public String user = "usuário";	
	
 	Parcela parcela = new Parcela();
 	ParPeriodo periodo = new ParPeriodo();
  
//	carrega a lista de dados p/ mostrar comboBox
	private ObservableList<Fornecedor> obsList;
	private ObservableList<TipoConsumo> obsListTipo;
 
	public void setCompromisso(Compromisso entity) {
		this.entity = entity;
	}
 	
// carregando parcelas, se houver 	
	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}
	
	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}
 	
//	busca os dados no bco de dados	
	public void setServices (CompromissoService service, FornecedorService fornecedorService, 
							TipoConsumoService tipoService, ParcelaService parService,
							ParPeriodoService perService) {
		this.service = service;
		this.fornecedorService = fornecedorService;
		this.tipoService = tipoService;
		this.parService = parService;
		this.perService = perService;
	}
//	armazena dados a serem atz no bco de dados	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
 		if (service == null) {
			throw new IllegalStateException("Serviço esta nulo");
		} 
 		classe = "Compromisso Form";
		try {
   			if (entity.getNnfCom() != null && entity.getNnfCom() != 0) {
   				conferePagamento(flagD, entity);
   			}	
   			if (flagD == 0) {	
   				entity = getFormData();
   				List<ParPeriodo> per = perService.findAll();
   				for (ParPeriodo p : per) {
   					periodo.setFornecedor(p.getFornecedor());
   					periodo.setTipoConsumo(p.getTipoConsumo());
   					periodo.setIdPeriodo(p.getIdPeriodo());
   					periodo.setDtiPeriodo(entity.getDataCom());
   					periodo.setDtfPeriodo(entity.getDataVencimentoCom());
   				}	
				classe = "Periodo Form ";
				perService.update(periodo);
				entity.setParPeriodo(periodo);
				classe = "Compromisso Form ";
				service.saveOrUpdate(entity);
				CalculaParcela.parcelaCreate(entity);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
   			}	
   		}	catch (ValidationException e) {
   			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto ", classe , e.getMessage(), AlertType.ERROR);
		}
 	}
	
		private Integer conferePagamento(Integer flag, Compromisso obj) {
		flag = 0;
			List<Parcela> list = parService.findByIdFornecedorNnf(obj.getCodigoFornecedorCom(),	obj.getNnfCom());
			List<Parcela> st = list.stream()
				.filter(p -> p.getNnfPar() == (obj.getNnfCom()))
				.filter(p -> p.getFornecedor().getCodigo() == (obj.getCodigoFornecedorCom()))  
				.filter(p -> p.getPagoPar() > 0)
				.collect(Collectors.toList());	
			if (st.size() > 0) {
				Alerts.showAlert("Erro!!! ", "Edição inválida ", "Existe(m) parcela(s) paga(s) para o Compromisso!!!", AlertType.ERROR);
				flag= 1;
			}	
			return flag;
		}	
		
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		classe = "Fornecedor Form";
		pesquisa = "";
		try {
	  		pesquisa = textIniciais.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Fornecedor> listFor = fornecedorService.findPesquisa(pesquisa);
				if (listFor.size() == 0) { 
					pesquisa = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Fornecedor obj = new Fornecedor();
		 		 		 createDialogForn(obj, "/gui/sgcp/FornecedorForm.fxml", parentStage);
		 		  	}
					listFor = fornecedorService.findPesquisa(pesquisa);
			 	}
	  			obsList = FXCollections.observableArrayList(listFor);
	  			comboBoxFornecedor.setItems(obsList);
	  			notifyDataChangeListeners();
	  			updateFormData();
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

 //	p/ cada listener da lista, eu aciono o metodo onData...	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
   
// carrega o obj com os dados do formulario retornando	obj
	 private Compromisso getFormData() {
		Compromisso obj = new Compromisso();
		ValidationException exception = new ValidationException("Validation error");
		 
// p/ transf string do formulario p/ bco em int \\ ou null		
		obj.setIdCom(entity.getIdCom());
		obj.setNnfCom(Utils.tryParseToInt(textNnfCom.getText()));
		if (textNnfCom.getText() == null  || textNnfCom.getText().trim().contentEquals("")) {
			exception.addErros("nnf", "NNF não pode ser 0");	
		}
		if (obj.getNnfCom() == null) {
			exception.addErros("nnf", "NNF não pode ser 0");	
		}
  
		if (dpDataCom.getValue() == null) {
			exception.addErros("dataCom", "data é obrigatória");
		}
		else {
			Instant instant = Instant.from(dpDataCom.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataCom(Date.from(instant));
		}

		if (dpDataVencimentoCom.getValue() == null) {
			exception.addErros("dataVencimentoCom", "data vencimento é obrigatória");
		}
		else {
			Instant instant = Instant.from(dpDataVencimentoCom.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataVencimentoCom(Date.from(instant));
		}
		if (obj.getDataVencimentoCom() == null) {
			exception.addErros("dataVencimentoCom", "data vencimento é obrigatória");
		}	
		if (obj.getDataCom() != null && obj.getDataVencimentoCom() != null) {
			Date dtc = obj.getDataCom();
			Date dtv = obj.getDataVencimentoCom();
			if (dtv.before(dtc)) {
				exception.addErros("dataVencimentoCom", "data menor que data da compra");
			}
		}
		
  		if (textValorCom.getText() == null || textValorCom.getText().trim().contentEquals("")) {
			exception.addErros("valor", "Valor não pode ser 0");
		} 
  		else {
  			obj.setValorCom(Utils.formatDecimalIn(textValorCom.getText().replace(".", "")));
  		}
  		

   		if (textParcelaCom.getText() == null || textParcelaCom.getText().trim().contentEquals("")) {
 			exception.addErros("parcela", "Parcela não pode ser 0");	
 		}
   		else {
   			obj.setParcelaCom(Utils.tryParseToInt(textParcelaCom.getText()));
   		}
   		if (obj.getParcelaCom() != null) {
   			int par = obj.getParcelaCom();
   			int ok = 1;
   			if (par == 01 || par == 02 || par == 03 || par == 06 || par == 12 || par == 24 || par == 36 || par == 72) {
   				ok = 0;
   			}
   			if (ok == 1) {
   				exception.addErros("parcela", "Parcela = 1, 2, 3, 6, 12, 24, 36, 72");	
 	    	}
 		}

  		if (textPrazoCom.getText() == null || textPrazoCom.getText().trim().contentEquals("")) { 
			exception.addErros("prazo", "Prazo não pode ser nulo");	
		}
  		else {
  			obj.setPrazoCom(Utils.tryParseToInt(textPrazoCom.getText()));
  			if (obj.getParcelaCom() > 1 && obj.getPrazoCom() == 1) { 
  				exception.addErros("prazo", "Parcela não é única, prazo tem que ser > 1"); 
  			}
  		}	
  
  		if (obj.getPrazoCom() != null) {
  			int prz = obj.getPrazoCom();
  			if (prz == 1 || prz == 10 || prz == 14 || prz == 21 || prz == 30 || prz == 60) {
  			}
  			else {
  				exception.addErros("prazo", "Prazo = 1(a vista), 10, 14, 21, 30 ou 60(dias)");	
  			}
  		}
  		
 		obj.setFornecedor(comboBoxFornecedor.getValue());
 		obj.setCodigoFornecedorCom(comboBoxFornecedor.getValue().getCodigo());
 		obj.setNomeFornecedorCom((comboBoxFornecedor.getValue().getRazaoSocial()));
 
 		if (obj.getIdCom() == null && obj.getNnfCom() != null) {
 			Compromisso comp = service.findById(obj.getCodigoFornecedorCom(), obj.getNnfCom());
 			if (comp != null) {
 				if (obj.getCodigoFornecedorCom().equals(comp.getCodigoFornecedorCom())) {
 					if (obj.getNnfCom().equals(comp.getNnfCom())) {
 						if (comp.getIdCom() != null) {
 							exception.addErros("nnf", " Fornecedor e NNF já existe(m) ");
 						}	
 					}	
 				}	
 	 		}	
 		}
 		
		obj.setTipoFornecedor(comboBoxTipoFornecedor.getValue());
  		if (exception.getErros().size() > 0) {
			throw exception;
		}
  		
  		obj.setParPeriodo(entOld.getParPeriodo());
  		return obj;
	}
	 
		@FXML
		public void onBtCancelAction(ActionEvent event) {
			Utils.currentStage(event).close();
	 	}

		@FXML
		public void onBtSairAction(ActionEvent event) {
			Utils.currentStage(event).close();
	 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initializeComboBoxFornecedor();
		initializeComboBoxTipoFornecedor();
	}
 	
	private void initializeNodes() {
 		Constraints.setTextFieldInteger(textNnfCom);
//		Constraints.setTextFieldDouble(textValorCom);
 		Constraints.setTextFieldInteger(textParcelaCom);
		Constraints.setTextFieldInteger(textPrazoCom);
 		Constraints.setTextFieldMaxLength(textNnfCom, 06);
 		Constraints.setTextFieldMaxLength(textParcelaCom, 02);
 		Constraints.setTextFieldMaxLength(textPrazoCom, 02);
 		Constraints.setTextFieldMaxLength(textIniciais, 7);
		Utils.formatDatePicker(dpDataCom, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataVencimentoCom, "dd/MM/yyyy");
   	}

	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
 			}
		};
		
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}		

	private void initializeComboBoxTipoFornecedor() {
		Callback<ListView<TipoConsumo>, ListCell<TipoConsumo>> factory = lv -> new ListCell<TipoConsumo>() {
			@Override
			protected void updateItem(TipoConsumo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeTipo());
			}
		};
		
		comboBoxTipoFornecedor.setCellFactory(factory);
		comboBoxTipoFornecedor.setButtonCell(factory.call(null));
	}		

//  dados do obj p/ preencher o formulario		 
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta vazia ");
		}
		labelUser.setText(user);
// aq pego compromisso p/ remover se trocar alguma coisa 		
		entOld = entity;
//  string value of p/ casting int p/ string
   		textNnfCom.setText(String.valueOf(entity.getNnfCom()));

   		if (entity.getDataCom() == null) {
   			entity.setDataCom(new Date());
   		}
   		if (entity.getDataCom() != null) {
			dpDataCom.setValue(LocalDate.ofInstant(entity.getDataCom().toInstant(), ZoneId.systemDefault()));
 		}
 
   		if (entity.getDataVencimentoCom() != null) {
			dpDataVencimentoCom.setValue(LocalDate.ofInstant(entity.getDataVencimentoCom().toInstant(), ZoneId.systemDefault()));
 		}

   		if (entity.getValorCom() != null) {
   			String vlr = Mascaras.formataValor(entity.getValorCom());
   	   		textValorCom.setText(vlr);
   		}
  
   		textParcelaCom.setText(String.valueOf(entity.getParcelaCom()));
		textPrazoCom.setText(String.valueOf(entity.getPrazoCom()));
		textIniciais.setText(pesquisa);
 		
  // se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getFornecedor() == null) {
			comboBoxFornecedor.getSelectionModel().selectFirst();
		} else {
 			comboBoxFornecedor.setValue(entity.getFornecedor());
		}
		if (entity.getTipoFornecedor() == null) {
			comboBoxTipoFornecedor.getSelectionModel().selectFirst();
		} else {
			comboBoxTipoFornecedor.setValue(entity.getTipoFornecedor());
		}
 	}

//	carrega dados do bco fornecedor dentro obslist
	public void loadAssociatedObjects() {
		if (fornecedorService == null) {
			throw new IllegalStateException("FornecedorServiço esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados		
		List<Fornecedor> list = fornecedorService.findAll();
		list.removeIf(x -> x.getRazaoSocial().contains("Adiantamento"));
// transf p/ obslist		
		obsList = FXCollections.observableArrayList(list);
		comboBoxFornecedor.setItems(obsList);
		if (tipoService == null) {
			throw new IllegalStateException("TipoFornecedorServiço esta nulo");
		}
// buscando (carregando) os tipos q est�o no bco de dados		
		List<TipoConsumo> listTipo = tipoService.findAll();
		listTipo.removeIf(y -> y.getNomeTipo().contains("Adiantamento"));
// transf p/ obslist		
		obsListTipo = FXCollections.observableArrayList(listTipo);
		comboBoxTipoFornecedor.setItems(obsListTipo);
	}

// informa��o de campos labelErro(msg)	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
//	condicional temporal (?), ou seja, se fields contains(igual) é Verdade, entra  opçao erros.get
//	se n�o, entra branco ""  -- fica mais enxuto
		labelErrorNnf.setText((fields.contains("nnf") ? erros.get("nnf") : ""));
		labelErrorDataCompra.setText((fields.contains("dataCom") ? erros.get("dataCom") : ""));
		labelErrorDataVencimentoCompra.setText((fields.contains("dataVencimentoCom") ? erros.get("dataVencimentoCom") : ""));
		labelErrorValor.setText((fields.contains("valor") ? erros.get("valor") : ""));
		labelErrorParcela.setText((fields.contains("parcela") ? erros.get("parcela") : ""));
		labelErrorPrazo.setText((fields.contains("prazo") ? erros.get("prazo") : ""));
	}
	
	private void createDialogForn(Fornecedor obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Fornecedor Form ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			FornecedorFormController controller = loader.getController();
			controller.user = user;
			controller.setFornecedor(obj);
			controller.setFornecedorService(new FornecedorService());
//			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Fornecedor                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
 	}	
}
