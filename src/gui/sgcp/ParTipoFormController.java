package gui.sgcp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
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
import javafx.util.Callback;
import model.exception.ValidationException;

public class ParTipoFormController implements Initializable {

	private ParPeriodo entity;
/*
 *  dependencia service com metodo set
 */
	private ParPeriodoService perService;
 	private TipoConsumoService tipoService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<TipoConsumo>  comboBoxTipoConsumo; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorComboBoxTipoConsumo;

	public void setPeriodo(ParPeriodo entity) {
		this.entity = entity;
 	}	

 	private ObservableList<TipoConsumo> obsListTipo;
	@SuppressWarnings("unused")
	private Integer codigo;
 
 // 	 * metodo set /p service
 	public void setPeriodoService(ParPeriodoService perService, TipoConsumoService tipoService) {
		this.perService = perService;
 		this.tipoService = tipoService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (perService == null)
		{	throw new IllegalStateException("Servi�o nulo");
		}
		try {
			List<ParPeriodo> listPer = perService.findAll();
     		entity = getFormData();
 			for (ParPeriodo p : listPer)
 			{ 	entity.setIdPeriodo(p.getIdPeriodo());
 				entity.setDtiPeriodo(p.getDtiPeriodo());
 				entity.setDtfPeriodo(p.getDtfPeriodo());
 				entity.setFornecedor(p.getFornecedor()); 
      		 	codigo = p.getTipoConsumo().getCodigoTipo();
 			} 
 			perService.update(entity);
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
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
	private ParPeriodo getFormData() {
		ParPeriodo obj = new ParPeriodo();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
 
 		if (comboBoxTipoConsumo.getValue().getCodigoTipo() == null) {
 		 	exception.addErros("tipoConsumo", "tipoConsumo inv�lido");
		} else {
			obj.setTipoConsumo(comboBoxTipoConsumo.getValue());
		}	
  			
		List<TipoConsumo> listFor = tipoService.findAll();
		for (TipoConsumo f : listFor) {
			if (f.getCodigoTipo().equals(obj.getTipoConsumo().getCodigoTipo())) {
				codigo = (f.getCodigoTipo());
 			}
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
		initializeComboBoxTipoConsumo();
    	}

	private void initializeComboBoxTipoConsumo() {
		Callback<ListView<TipoConsumo>, ListCell<TipoConsumo>> factory = lv -> new ListCell<TipoConsumo>() {
			@Override
			protected void updateItem(TipoConsumo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeTipo());
 			}
		};
		
		comboBoxTipoConsumo.setCellFactory(factory);
		comboBoxTipoConsumo.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getTipoConsumo() == null) {
			comboBoxTipoConsumo.getSelectionModel().selectFirst();
		} else {
 			comboBoxTipoConsumo.setValue(entity.getTipoConsumo());
		}
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (perService == null) {
			throw new IllegalStateException("PeriodoServi�o esta nulo");
		}
		if (tipoService == null) {
			throw new IllegalStateException("TipoServi�o esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados		
		List<TipoConsumo> listTipo = tipoService.findAll();
 		obsListTipo = FXCollections.observableArrayList(listTipo);
		comboBoxTipoConsumo.setItems(obsListTipo);
		
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxTipoConsumo.setText((fields.contains("tipoConsumo") ? erros.get("tipoConsumo") : ""));
  	}
}	

