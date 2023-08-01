package gui.sgcp;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.ParcelaService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
 
public class ParcelaFormController implements Initializable {

//	lista da classe subject - armazena os dados a serem atz no formulario 
//	recebe e emite eventos
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

   	@FXML
	private TextField textFornecedorPar;

   	@FXML
	private TextField textNnfPar;

	@FXML
	private TextField textDataVencimentoPar;
 
  	@FXML
	private TextField textNumeroPar;

 	@FXML
	private TextField textValorPar; 

 	@FXML
	private TextField textDescontoPar;

 	@FXML
	private TextField textJurosPar;

  	@FXML
	private Label labelTotalPar;

 	@FXML
	private TextField textPagoPar;

	@FXML
	private DatePicker dpDataPagamentoPar;

 	@FXML
	private Label labelErrorDesconto;

 	@FXML
	private Label labelErrorPago;

 	@FXML
	private Label labelErrorPago1;

	@FXML
	private Label labelErrorDataPagamento;

 	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataf;
	String classe = "Parcela Form ";
	 	
//	carrega a lista de dados p/ mostrar comboBox
	@SuppressWarnings("unused")
	private ObservableList<Parcela> obsList;
  
//	carrega os dados do formulario	
	private Parcela entity;
		
//	carrega dados do banco na cri��o do formulario - inje��o de dependencia
 	private ParcelaService parService;
	
	  
	public void setParcela(Parcela entity) {
		this.entity = entity;
	}
  	
//	busca os dados no bco de dados	
	public void setParcelaService (ParcelaService parService) {
 		this.parService = parService;
 	}

//	armazena dados a serem atz no bco de dados	
	public void subscribeDataChangeListener(DataChangeListener listener) {
  		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtOkAction(ActionEvent event) throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
 		if (parService == null) {
			throw new IllegalStateException("Serviço esta nulo");
 		} 
		if (entity.getPagoPar() > 0) {
			Alerts.showAlert("Aviso ", "Parcela quitada ", "sem acesso ", AlertType.INFORMATION);
		} else { 		
			try { entity = getFormData();
  				parService.saveUpdate(entity);
 				notifyDataChangeListeners();
 				updateFormData();
				Utils.currentStage(event).close();
			} catch (ValidationException e) {
				setErrorMessages(e.getErros());
			} catch (DbException e) {
				Alerts.showAlert("Erro salvando objeto ", classe , e.getMessage(), AlertType.ERROR);
			}
		}	
 	}

//	p/ cada listener da lista, eu aciono o metodo onData...	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

 // carrega o obj com os dados do formulario retornando	obj
	 private Parcela getFormData() throws ParseException {
			Parcela obj = entity;
 			ValidationException exception = new ValidationException("Validation error");
 			
  			if (textDescontoPar.getText() != null) {
 				textDescontoPar.getText().replace(".", "");
  				obj.setDescontoPar(Utils.formatDecimalIn(textDescontoPar.getText())); 	 		
  			}
 
			if (obj.getDescontoPar() > obj.getValorPar()) { 
				exception.addErros("desconto", "desconto é maior que o valor");
 			}
			
 			if (textJurosPar.getText() != null) {
 				obj.setJurosPar(Utils.formatDecimalIn(textJurosPar.getText().replace(".", "")));
 	 		}
 
 			double total = obj.getValorPar() + obj.getJurosPar() - obj.getDescontoPar();
 			
  			labelTotalPar.setText(Mascaras.formataValor(total));
 			if (labelTotalPar.getText() != null) {
 				obj.setTotalPar(Utils.formatDecimalIn(labelTotalPar.getText().replace(".", "")));
  	 		}
  
   			if (textPagoPar.getText() != null || textPagoPar.getText().trim().contentEquals("")) {
 				obj.setPagoPar(Utils.formatDecimalIn(textPagoPar.getText().replace(".", "")));
 				if (obj.getPagoPar() > 0 && obj.getPagoPar() != total) {	
 					exception.addErros("totalPagoPar", "valor inválido - total a pagar R$");
 					exception.addErros("totalPagoPar1", String.format("%.2f", total));
 					obj.setPagoPar(0.00);
  				}
 			}
   			
   			if (obj.getPagoPar() > 0) {
   	 			Instant instant = Instant.from(dpDataPagamentoPar.getValue().atStartOfDay(ZoneId.systemDefault()));
   				obj.setDataPagamentoPar(Date.from(instant));
 				if (dpDataPagamentoPar.getValue() == null) { 
					exception.addErros("dataPagamentoPar", "data é obrigatória");
				}
 			}
   			
   			if (obj.getDataPagamentoPar() != null) {
   				if (obj.getDataPagamentoPar().before(obj.getDataVencimentoPar())) {
   					exception.addErros("dataPagamentoPar", "data pagto é menor que data vecto");
   					obj.setDataPagamentoPar(null);
   				}	
			}
   			obj.setResultadoPar(0.00);
   			obj.setResultadoParStr("");
   			obj.setFornecedor(entity.getFornecedor());
   			obj.setTipoFornecedor(entity.getTipoFornecedor());
   			obj.setPeriodo(entity.getPeriodo());
    			
      		if (exception.getErros().size() > 0) {
      			throw exception;
     		}
   		return obj;
 	}
	 
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}
 	
	private void initializeNodes() {
 		Constraints.setTextFieldDouble(textJurosPar);
		Constraints.setTextFieldDouble(textDescontoPar);
// 		Constraints.setTextFieldDouble(textTotalPar);
		Constraints.setTextFieldDouble(textPagoPar);
// 		Utils.formatDatePicker(textDataVencimentoPar, "dd/MM/yyyy");
 		Utils.formatDatePicker(dpDataPagamentoPar, "dd/MM/yyyy");
 	}

 //  dados do obj p/ preencher o formulario		 
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta vazia ");
		}
		Calendar cal = Calendar.getInstance();
 //  string value of p/ casting int p/ string
		textFornecedorPar.setText(entity.getFornecedor().getRazaoSocial());
		textNnfPar.setText(String.valueOf(entity.getNnfPar()));
		textNumeroPar.setText(String.valueOf(entity.getNumeroPar()));
		cal.setTime(entity.getDataVencimentoPar());
		int dd = cal.get(Calendar.DAY_OF_MONTH);
		int mm = 1 + cal.get(Calendar.MONTH);
		int aa = cal.get(Calendar.YEAR);
		String data = (dd + "/" + mm + "/" + aa);
		if (entity.getDataVencimentoPar() != null ) {
			textDataVencimentoPar.setText(data);
		}
		double vlr = 0.0;
		String vlrM = null;
		if (entity.getValorPar() != null) {
			vlr = entity.getValorPar();
			vlrM = Mascaras.formataValor(vlr); 
			textValorPar.setText(vlrM);
		}

		if (entity.getDescontoPar() != null) {
			vlr = entity.getDescontoPar();
			vlrM = Mascaras.formataValor(vlr); 
			textDescontoPar.setText(vlrM);
		}

		if (entity.getJurosPar() != null) {
			vlr = entity.getJurosPar();
			vlrM = Mascaras.formataValor(vlr); 
			textJurosPar.setText(vlrM);
		}
	
		if (entity.getTotalPar() != null) {
			vlr = entity.getTotalPar();
			vlrM = Mascaras.formataValor(vlr); 
			labelTotalPar.setText(vlrM);
		}
 
		if (entity.getPagoPar() != null) {
			textPagoPar.setText(String.format("%.2f", entity.getPagoPar()));
		}
 		
		if (entity.getDataPagamentoPar() != null) {
			dpDataPagamentoPar.setValue(LocalDate.ofInstant(entity.getDataPagamentoPar().toInstant(), 
					ZoneId.systemDefault()));
		}
	}	
  	
	
  //	carrega dados do bco fornecedor dentro obslist
	public void loadAssociatedObjects() {
		if (parService == null) {
			throw new IllegalStateException("Parcela Serviço esta nulo");
		}
  		List<Parcela> list = parService.findAllAberto();
 		obsList = FXCollections.observableArrayList(list);
}
 
 // informa��o de campos labelErro(msg)	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
  		labelErrorPago.setText((fields.contains("totalPagoPar") ? erros.get("totalPagoPar") : ""));
  		labelErrorDesconto.setText((fields.contains("desconto") ? erros.get("desconto") : ""));
  		labelErrorPago1.setText((fields.contains("totalPagoPar1") ? erros.get("totalPagoPar1") : ""));
		labelErrorDataPagamento.setText((fields.contains("dataPagamentoPar") ? erros.get("dataPagamentoPar") : ""));
  	}
}
