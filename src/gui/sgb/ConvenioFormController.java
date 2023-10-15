package gui.sgb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Cliente;
import gui.sgbmodel.service.ClienteService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;

public class ConvenioFormController implements Initializable {

	private Cliente entity;
/*
 *  dependencia service com metodo set
 */
 	private ClienteService cliService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
 	@FXML
 	private TextField textPesquisa;
 	
	@FXML
	private ComboBox<Cliente>  comboBoxCliente; 
	
	@FXML
	private RadioButton rbFormaDinheiroCartelaPag;

	@FXML
	private RadioButton rbFormaPixCartelaPag;

	@FXML
	private RadioButton rbFormaDebitoCartelaPag;

	@FXML
	private RadioButton rbFormaCartaoCartelaPag;

  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Button btPesquisa;
	
	@FXML
	private Label labelErrorComboBoxCliente;

	@FXML
	private Label labelErrorForma;

 	private ObservableList<Cliente> obsListCli;
	String pesquisa = "";
	String classe = "Cliente";
 
 	public void setCliente(Cliente entity) {		
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setService(ClienteService cliService) {
 		this.cliService = cliService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		try {
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Cliente> list = cliService.findPesquisa(pesquisa);
				if (list.size() == 0) { 
					pesquisa = "";
					Alerts.showAlert("Cliente ", null, "Não encontrado ", AlertType.INFORMATION);
					list = cliService.findAll();
			 	}
				pesquisa = "";
	  			obsListCli = FXCollections.observableArrayList(list);
				comboBoxCliente.setItems(obsListCli);
				comboBoxCliente.getSelectionModel().selectFirst();
	  			notifyDataChangeListerners();
	  			updateFormData();
	  		}	
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

/* 
 * vamos instanciar um tipoforn e salvar no bco de dados
 * meu obj entity (l� em cima) vai receber uma chamada do getformdata
 *  metodo q busca dados do formulario convertidos getForm (string p/ int ou string)		
 *  pegou no formulario e retornou (convertido) p/ jogar na variavel entity
 *  chamo o service na rotina saveupdate e mando entity
 *  vamos tst entity e service = null -> n�o foi injetado
 *  para fechar a janela, pego a referencia para janela atual (event) e close
 *  DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar uma lista
 *  qdo ela salvar obj com sucesso, � s� notificar (juntar)
 *  recebe l� no  listController    		 
 */
	@SuppressWarnings("unused")
	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		try {
			List<Cliente> listCli = new ArrayList<>();
			if (pesquisa != "") { 
				listCli = cliService.findPesquisa(pesquisa);
			} else {	
				listCli = cliService.findAll();
		 	}			
     		entity = getFormData();
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se n�o for atz
 * tb verificamos se cpos obrigat�rios est�o preenchidos, para informar erro(s)
 * para cpos string n�o precisa tryParse	
 */
	private Cliente getFormData() {
		Cliente obj = new Cliente();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

 		if (comboBoxCliente.getValue().getNomeCli() == null) {
 		 	exception.addErros("cliente", "cliente inválido");
		} else {
			ConvenioListAbertoController.nomeCli = comboBoxCliente.getValue().getNomeCli();
		}
 		
		if (comboBoxCliente.getValue().getConvenioCli() == null ||
				comboBoxCliente.getValue().getConvenioCli().equals("N")) {
			exception.addErros("cliente", "cliente não tem convênio");
			ConvenioListAbertoController.nomeCli = null;
		}	
		
		int flag3 = 0;
		if (rbFormaDinheiroCartelaPag.isSelected()) {
			ConvenioListAbertoController.forma = "Dinheiro";
			flag3 += 1;
		}
		if (rbFormaPixCartelaPag.isSelected()) {
			ConvenioListAbertoController.forma = "Pix";
			flag3 += 1;
		}
		if (rbFormaDebitoCartelaPag.isSelected()) {
			ConvenioListAbertoController.forma = "Débito";
			flag3 += 1;
		}
		if (rbFormaCartaoCartelaPag.isSelected()) {
			ConvenioListAbertoController.forma = "Cartão";
			flag3 += 1;
		}
		if (ConvenioListAbertoController.forma == null || flag3 == 0) {
			exception.addErros("forma", "Forma de pagto é obrigatório");
			ConvenioListAbertoController.forma = null;
		}
		if (flag3 > 1) {
			exception.addErros("forma", "Só pode uma opção");
			ConvenioListAbertoController.forma = null;
		}
 		
  		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxCliente();
    	}

	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeCli() + " conv " + item.getConvenioCli());
 			}
		};
		
		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
		comboBoxCliente.getSelectionModel().selectFirst();
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
// buscando (carregando) os forn q est�o no bco de dados		
		List<Cliente> listCli = cliService.findAll();
 		obsListCli = FXCollections.observableArrayList(listCli);
		comboBoxCliente.setItems(obsListCli);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxCliente.setText((fields.contains("cliente") ? erros.get("cliente") : ""));
		labelErrorForma.setText((fields.contains("forma") ? erros.get("forma") : ""));
  	}
}	
