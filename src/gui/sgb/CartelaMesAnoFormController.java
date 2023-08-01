package gui.sgb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.MesesService;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;

public class CartelaMesAnoFormController implements Initializable {

	private Meses entity;
	private Anos objAno;
/*
 *  dependencia service com metodo set
 */
	
	private MesesService mesService;
	private AnosService anoService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<Meses>  comboBoxMeses; 
	
	@FXML
	private ComboBox<Anos>  comboBoxAnos; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorComboBoxMeses;

	@FXML
	private Label labelErrorComboBoxAnos;

 	private ObservableList<Meses> obsListMes;
 	private ObservableList<Anos> obsListAno;
 	
//auxiliar
 	String classe = "Cartela MesAno";
 
 	public void setMesAno(Meses entity, Anos objAno) {		
		this.entity = entity;
		this.objAno = objAno;
	}

 // 	 * metodo set /p service
 	public void setServices(MesesService mesService,
 							AnosService anoService) {
 		this.mesService = mesService;
 		this.anoService = anoService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade mes nula");
		}
		try {
			
     		entity = getFormData();
     		objAno = getFormDataAno();
     		notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
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
	private Meses getFormData() {
		Meses mes = new Meses();
		ValidationException exception = new ValidationException("Validation exception");

		mes.setNumeroMes(comboBoxMeses.getValue().getNumeroMes());
		CartelaListAbertoController.mm = comboBoxMeses.getValue().getNumeroMes();
		CartelaListPagoController.mm = comboBoxMeses.getValue().getNumeroMes();
 		
 		if (exception.getErros().size() > 0) {
			throw exception;
		}
 		return mes;
	}
	
	private Anos getFormDataAno() {
		ValidationException exception = new ValidationException("Validation exception");

		objAno.setAnoAnos(comboBoxAnos.getValue().getAnoAnos());
		CartelaListAbertoController.aa = comboBoxAnos.getValue().getAnoAnos();
		CartelaListPagoController.aa = comboBoxAnos.getValue().getAnoAnos();
 		if (exception.getErros().size() > 0) {
			throw exception;
		}
 		return objAno;
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
		initializeComboBoxMeses();
		initializeComboBoxAnos();
    	}

	private void initializeComboBoxMeses() {
		Callback<ListView<Meses>, ListCell<Meses>> factory = lv -> new ListCell<Meses>() {
			@Override
			protected void updateItem(Meses item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMes());
 			}
		};
		
		comboBoxMeses.setCellFactory(factory);
		comboBoxMeses.setButtonCell(factory.call(null));
	}		
   	
	private void initializeComboBoxAnos() {
		Callback<ListView<Anos>, ListCell<Anos>> factory = lv -> new ListCell<Anos>() {
			@Override
			protected void updateItem(Anos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getAnoStrAnos());
 			}
		};
		
		comboBoxAnos.setCellFactory(factory);
		comboBoxAnos.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
			comboBoxMeses.getSelectionModel().selectFirst();
			comboBoxAnos.getSelectionModel().selectFirst();
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null || anoService == null) {
			throw new IllegalStateException("Tem Serviço nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados
		List<Meses> listMes = mesService.findAll();
 		obsListMes = FXCollections.observableArrayList(listMes);
		comboBoxMeses.setItems(obsListMes);
		List<Anos> listAno = anoService.findAll();
 		obsListAno = FXCollections.observableArrayList(listAno);
		comboBoxAnos.setItems(obsListAno);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxMeses.setText((fields.contains("meses") ? erros.get("meses") : ""));
 		labelErrorComboBoxAnos.setText((fields.contains("anos") ? erros.get("anos") : ""));
  	}
}	