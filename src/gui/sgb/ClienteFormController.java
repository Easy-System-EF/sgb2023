package gui.sgb;

import java.net.URL;
import java.text.ParseException;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
 
public class ClienteFormController implements Initializable {

	private Cliente entity;
	
/*
 *  dependencia service com metodo set
 */
	private ClienteService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoCliente;
	
	@FXML
	private TextField textNomeCliente;
	
	@FXML
	private TextField textDddCliente;
	
	@FXML
	private TextField textTelefoneCliente;
	
	@FXML
	private TextField textConvenioCliente;
	
 	@FXML
	private Button btSaveCliente;
	
	@FXML
	private Button btCancelCliente;
	
	@FXML
	private Button btSairCliente;
	
	@FXML
	private Label labelErrorNomeCliente;

	@FXML
	private Label labelErrorDddCliente;

	@FXML
	private Label labelErrorTelefoneCliente;

	@FXML
	private Label labelErrorConvenioCliente;

	@FXML
	private Label labelUser;

// auxiliares
	String classe = "Cliente Form";
	public String user = "usuário";		
	
 	public void setCliente(Cliente entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setClienteService(ClienteService service) {
		this.service = service;
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
	public void onBtSaveClienteAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			 int sai = 0;
			 if (entity.getCodigoCli() != null) {
				 sai = 1;
			 }
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 notifyDataChangeListerners();
	    	 if ( sai == 1) {
	    		 Utils.currentStage(event).close();
	    	 }	 
	    	 entity = new Cliente();
	    	 updateFormData();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert(classe + "Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		} catch (ParseException e) {
			e.printStackTrace();
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
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoCli(Utils.tryParseToInt(textCodigoCliente.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
		if (textNomeCliente.getText() == null || textNomeCliente.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		}
		else {
			obj.setNomeCli(textNomeCliente.getText());
			if (textNomeCliente.getText().length() < 4) {
				exception.addErros("nome", "Nome inválido");
			}	
		}	
 		
		if (textDddCliente.getText() == null || textDddCliente.getText().trim().contentEquals("") ||
				textDddCliente.getText() == "   ") {
			exception.addErros("ddd", "Ddd é obrigatório");
  		}	else {
 				obj.setDddCli(Utils.tryParseToInt(textDddCliente.getText()));
			}	
		if (obj.getDddCli() == null || obj.getDddCli() == 0) {
			exception.addErros("ddd", "Ddd é obrigatório");			
		} else {
			if(obj.getDddCli() < 11) {
				exception.addErros("ddd", "Ddd inválido");
			}	
		}

		if (textTelefoneCliente.getText() == null || textTelefoneCliente.getText().trim().contentEquals("")) {
			exception.addErros("telefone", "telefone é obrigatório ");
  		}	else {
 				obj.setTelefoneCli(Utils.tryParseToInt(textTelefoneCliente.getText()));
			}	
		if (obj.getTelefoneCli() == null) {
			exception.addErros("telefone", "telefone é obrigatório ");
		}
		
		obj.setConvenioCli(textConvenioCliente.getText().toUpperCase());

		@SuppressWarnings("unused")
		int ok = 0;
		if (obj.getConvenioCli().contains("S") || obj.getConvenioCli().contains("N")) {
			ok = 1;
		} else {	
			exception.addErros("convenio", "Convênio(S/N) inválido ");
		}	
		
		if (obj.getTelefoneCli() < 20000000) {
			exception.addErros("telefone", "telefone inválido ");
		}
		
		if (obj.getDddCli() != null && obj.getTelefoneCli() != null) {
			List<Cliente> listCli = service.findAll();
			for (Cliente c : listCli) {
				if (c.getDddCli().equals(obj.getDddCli())) {
					if (c.getTelefoneCli().equals(obj.getTelefoneCli())) {					
						if (!c.getCodigoCli().equals(obj.getCodigoCli())) {
							exception.addErros("ddd", "Telefone já existe ");
							exception.addErros("telefone", c.getCodigoCli() + " " + c.getNomeCli());
						}
					}	
				}
			}
		}
		
 // tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	} 
	
// msm processo save p/ fechar	
	@FXML
	public void onBtCancelClienteAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtSairClienteAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		Constraints.setTextFieldInteger(textCodigoCliente);
 		Constraints.setTextFieldInteger(textDddCliente);
 		Constraints.setTextFieldInteger(textTelefoneCliente);
  		Constraints.setTextFieldMaxLength(textDddCliente, 02);
  		Constraints.setTextFieldMaxLength(textTelefoneCliente, 9);
  		Constraints.setTextFieldMaxLength(textConvenioCliente, 1);
   	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		labelUser.setText(user);
 //  string value of p/ casting int p/ string 		
 		textCodigoCliente.setText(String.valueOf(entity.getCodigoCli()));
 		textNomeCliente.setText(entity.getNomeCli());
 		if (entity.getDddCli() == null) {
 			entity.setDddCli(31);
 		}
 		textDddCliente.setText(String.valueOf(entity.getDddCli()));
 		
  		if (entity.getTelefoneCli() == null){
 			entity.setTelefoneCli(0);
 		}
  		textTelefoneCliente.setText(String.valueOf(entity.getTelefoneCli()));

  		if (entity.getConvenioCli() == null) {
 			entity.setConvenioCli("N");
 		}
  		textConvenioCliente.setText(entity.getConvenioCli());

 	}
 	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		if (fields.contains("nome")) {
 			labelErrorNomeCliente.setText(erros.get("nome"));	
 		}
  		labelErrorNomeCliente.setText(fields.contains("nome") ? erros.get("nome") : "");		
  		labelErrorDddCliente.setText(fields.contains("ddd") ? erros.get("ddd") : "");
  		labelErrorTelefoneCliente.setText(fields.contains("telefone") ? erros.get("telefone") : "");
  		labelErrorConvenioCliente.setText(fields.contains("convenio") ? erros.get("convenio") : "");
	}
}	
