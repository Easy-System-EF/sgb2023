package gui.sgcp;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.service.FornecedorService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.CpfCnpjNum;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;

public class FornecedorFormController implements Initializable, Serializable  {

	private static final long serialVersionUID = 1L;

	private Fornecedor entity;
	
/*
 *  dependencia service com metodo set
 */
	private FornecedorService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigo;
	
	@FXML
	private TextField textRazaoSocial;
	
	@FXML
	private TextField textRua;
	
	@FXML
	private TextField textNumero;
	
	@FXML
	private TextField textComplemento;
	
	@FXML
	private TextField textBairro;
	
	@FXML
	private TextField textCidade;
	
	@FXML
	private TextField textUF;
	
	@FXML
	private TextField textCep;
	
	@FXML
	private TextField textDdd01;
	
	@FXML
	private TextField textTelefone01;
	
	@FXML
	private TextField textDdd02;
	
	@FXML
	private TextField textTelefone02;
	
	@FXML
	private TextField textContato;
	
	@FXML
	private TextField textDddContato;
	
	@FXML
	private TextField textTelefoneContato;
	
	@FXML
	private TextField textEmail;
	
	@FXML
	private TextField textPix;
	
	@FXML
	private TextField textObservacao;
	
	@FXML
	private TextField textPrazo;
	
	@FXML
	private TextField textParcela;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Button btSair;
	
	@FXML
	private Label labelErrorRazaoSocial;

	@FXML
	private Label labelErrorRua; 
	
	@FXML
	private Label labelErrorBairro; 
	
	@FXML
	private Label labelErrorCidade;
	
	@FXML
	private Label labelErrorCep; 
	
	@FXML
	private Label labelErrorUF; 

	@FXML
	private Label labelErrorTelefone01; 

	@FXML
	private Label labelErrorEmail; 

	@FXML
	private Label labelErrorPrazo; 

	@FXML
	private Label labelErrorParcela; 

	@FXML
	private Label labelUser;

	String classe = "Fornecedor ";
	public String user = "usuário";	
	 	
// flag p/ verificar UF	
		int flag = 0;
 
 	public void setFornecedor(Fornecedor entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setFornecedorService(FornecedorService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um forn e salvar no bco de dados
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
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			int sai = 0;
			if (entity.getCodigo() != null) {
				sai = 1;
			}
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 notifyDataChangeListerners();
	    	 if (sai == 1) {
	    		 Utils.currentStage(event).close();
	    	 }	 
	 			entity = new Fornecedor();
	 			updateFormData();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
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
	private Fornecedor getFormData() {
		Fornecedor obj = new Fornecedor();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigo(Utils.tryParseToInt(textCodigo.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
		obj.setRazaoSocial(textRazaoSocial.getText());
		if (obj.getRazaoSocial() == null) {
			exception.addErros("razaoSocial", "Razão Social é obrigatório");
		}
		if (obj.getRazaoSocial() != null) {
			if (textRazaoSocial.getText().length() < 4) {
				exception.addErros("razaoSocial", "Razão Social muito curta");
			}	
		}	
			
		if (textRua.getText() == null || textRua.getText().trim().contentEquals("")) {
			exception.addErros("rua", "Rua é obrigatório");
		}
		obj.setRua(textRua.getText());
		if (obj.getRua() != null) {
			if (textRua.getText().length() < 3) {
				exception.addErros("rua", "Rua muito curta");
			}	
		}

		obj.setNumero(Utils.tryParseToInt(textNumero.getText()));

		obj.setComplemento(textComplemento.getText());
		
		if (textBairro.getText() != null) {
			if (textBairro.getText().length() < 3) {
				exception.addErros("bairro", "Bairro inválido");
			}
		}
		obj.setBairro(textBairro.getText());
		if (obj.getBairro() == null) {
			exception.addErros("bairro", "Bairro é obrigatório");
		}
			
		if (textCidade.getText() != null) {
			if (textCidade.getText().length() < 3) {
				exception.addErros("cidade", "Cidade inválida");
			}
		}
		obj.setCidade(textCidade.getText());
		if (obj.getCidade() == null) {
			exception.addErros("cidade", "Cidade é obrigatória");
		}

		if (textUF.getText() == null || textUF.getText().trim().contentEquals("")) {
			exception.addErros("uf", "UF inválida ");
		} else {	
			String uf = textUF.getText();
			obj.setUf(uf.toUpperCase());
// verifica se UF existe
			verificaUf(obj);
			if (obj.getUf()  == "XX") {
				exception.addErros("uf", "UF não existe ");
			}	
		}
		
		String cep = null;
		if (textCep.getText() == null || textCep.getText().trim().contentEquals(" ")) {
			exception.addErros("cep", "Cep é obrigatório");
		} else {
			cep = textCep.getText().replace("-", "");
			if (cep != null) {
				obj.setCep(cep);
				obj.setCep(CpfCnpjNum.confereZero(obj.getCep()));
				if (obj.getCep() == "zerado") {
					exception.addErros("cep", "Cep não pode ser 000...");
				}	else {
					if (obj.getCep().length() < 8) {
						exception.addErros("cep", "Cep inválido");
					}	else {
						obj.setCep(CpfCnpjNum.confereNumeral(obj.getCep()));
					}
					if (obj.getCep() == null) {
						exception.addErros("cep", "Cep não tem letra");
					}	
				}	
			}
		}
	
   		obj.setDdd01(Utils.tryParseToInt(textDdd01.getText()));
   		if (obj.getDdd01() == null || obj.getDdd01() == 0 || obj.getDdd01() < 10) {
			exception.addErros("telefone01", "1o DDD é obrigatório");   			
   		}
   		int tel1 = 0;
   		int tel2 = 10000001;
   		obj.setTelefone01(Utils.tryParseToInt(textTelefone01.getText()));
   		if (obj.getTelefone01() == null) {
			exception.addErros("telefone01", "1o Telefone é obrigatório");
		}	else {
			if (obj.getTelefone01()  != null || obj.getTelefone01() == 0) {
				tel1 = Utils.tryParseToInt(textTelefone01.getText());
				tel2 = 10000001;
				if (tel1 <  tel2) {
					exception.addErros("telefone01", "1o Telefone é obrigatório");
				}	
			}	
		}
		
  		obj.setDdd02(Utils.tryParseToInt(textDdd02.getText()));
  		obj.setTelefone02(Utils.tryParseToInt(textTelefone02.getText()));
		obj.setContato(textContato.getText());
  		obj.setDddContato(Utils.tryParseToInt(textDddContato.getText()));
 		obj.setTelefoneContato(Utils.tryParseToInt(textTelefoneContato.getText()));

 		if (textEmail.getText() != null) {
 	 		if (textEmail.getText().length() < 5) {
 			exception.addErros("email", "Email deve ser inválido");
 	 		}
 		}	
		obj.setEmail(textEmail.getText());

		obj.setPix(textPix.getText());
		obj.setObservacao(textObservacao.getText());
		obj.setPrazo(Utils.tryParseToInt(textPrazo.getText()));

		@SuppressWarnings("unused")
		int ok = 0;
		if (obj.getPrazo() == null) {
			exception.addErros("prazo", "Prazo = 1 (a vista), 10, 14, 21, 30 ou 60(dias)");				
		}
		if (obj.getPrazo() != null) {
			int prz = obj.getPrazo();
			if (prz == 1 || prz == 10 || prz == 14 || prz == 21 || prz == 30 || prz == 60) {
				ok = 1;
			}
			else {
				exception.addErros("prazo", "Prazo = 1 (a vista), 10, 14, 21, 30 ou 60(dias)");	
			}
		}
	
   		int par = 0;
		obj.setParcela(Utils.tryParseToInt(textParcela.getText()));
   		if (obj.getParcela() == null) {
 			exception.addErros("parcela", "Parcela não pode ser 0");	
 		} else {
 			par = obj.getParcela();
			}
   		if (par == 01 || par == 02 || par == 03 || par == 06 || par == 12 || par == 24 || par == 36 || par == 72) {
   			ok = 0;
   		} else {
   			exception.addErros("parcela", "Parcela = 1, 2, 3, 6, 12, 24, 36, 72");	
 	      	}
 		
   		if (obj.getParcela() != null) {
//   			if (obj.getParcela() == 1 && obj.getPrazo() != 1) {
//				exception.addErros("prazo", "Parcela única, prazo tem que ser = 1"); 
//			}	
			if (obj.getParcela() > 1 && obj.getPrazo() == 1) { 
				exception.addErros("prazo", "Parcela não é única, prazo tem que ser > 1"); 
			}	
   		}	

// tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	} 
	
	private static Fornecedor verificaUf(Fornecedor obj) {
   		int flag = 0;
  		int i = 0;
  		String vuf = obj.getUf();
  		String tabelaUF = "AC,AL,AM, AP,AC, BA, CE, DF, GO, MA, MG, MT, MS, PB, PE, PI, PR, "
       		         	+ "RJ, RN, RO, RR, RS, SE, SC, SP, TO, 'null'";
  		String[] tabuf = tabelaUF.split(",");
	  	
  		for ( i = 0 ; i < tabuf.length ; i ++)
  			{	Boolean fv = tabuf[i].contains(vuf) ;
  				if (fv == true )
  				{	flag = 1;	}
  			}	
  		if (flag == 0)
  		{	obj.setUf("XX"); 	}
  		return obj;
  	}
  	
// msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtSairAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		initializeNodes();
  	}	
  		
  	private void initializeNodes() {	
 		Constraints.setTextFieldInteger(textCodigo);
 		Constraints.setTextFieldInteger(textNumero);
 		Constraints.setTextFieldInteger(textDdd01);
 		Constraints.setTextFieldInteger(textTelefone01);
 		Constraints.setTextFieldInteger(textDdd02);
 		Constraints.setTextFieldInteger(textTelefone02);
 		Constraints.setTextFieldInteger(textDddContato);
 		Constraints.setTextFieldInteger(textTelefoneContato);
 		Constraints.setTextFieldInteger(textPrazo);
 		Constraints.setTextFieldInteger(textParcela);

 		Constraints.setTextFieldMaxLength(textRazaoSocial, 40);
 		Constraints.setTextFieldMaxLength(textUF, 2);
 		Constraints.setTextFieldMaxLength(textDdd01, 2);
 		Constraints.setTextFieldMaxLength(textTelefone01, 9);
 		Constraints.setTextFieldMaxLength(textDdd02, 2);
 		Constraints.setTextFieldMaxLength(textTelefone02, 9);
 		Constraints.setTextFieldMaxLength(textDddContato, 2);
 		Constraints.setTextFieldMaxLength(textTelefoneContato, 9);

 		Constraints.setTextFieldMaxLength(textRua, 40);
 		Constraints.setTextFieldMaxLength(textNumero, 6);
 		Constraints.setTextFieldMaxLength(textComplemento, 20);
 		Constraints.setTextFieldMaxLength(textBairro, 20);
 		Constraints.setTextFieldMaxLength(textCidade, 20);
 		Constraints.setTextFieldMaxLength(textCep, 9);
 		Constraints.setTextFieldMaxLength(textContato, 20);
 		Constraints.setTextFieldMaxLength(textEmail, 40);
 		Constraints.setTextFieldMaxLength(textObservacao, 40);
 		Constraints.setTextFieldMaxLength(textPix, 40);
 		Constraints.setTextFieldMaxLength(textPrazo,  2);
 		Constraints.setTextFieldMaxLength(textParcela,  2);
 	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() throws ParseException {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
 		limpaNumeros(entity);

 		String cep = null;
 		String cepM = null;
 		labelUser.setText(user);
 //  string value of p/ casting int p/ string 		
 		textCodigo.setText(String.valueOf(entity.getCodigo()));
 		textRazaoSocial.setText(entity.getRazaoSocial());
 		textRua.setText(entity.getRua());
		textNumero.setText(String.valueOf(entity.getNumero()));
 		textComplemento.setText(entity.getComplemento());
 		textCep.setText(entity.getCep());
 		if (entity.getCep() != null)
 		{	cep = textCep.getText();
   			cepM = Mascaras.formataString(cep, "Cep");
			textCep.setText(cepM);
 		}
  		textBairro.setText(entity.getBairro());
 		textCidade.setText(entity.getCidade());
 		textUF.setText(entity.getUf());
		textDdd01.setText(String.valueOf(entity.getDdd01()));
		textTelefone01.setText(String.valueOf(entity.getTelefone01()));
		textDdd02.setText(String.valueOf(entity.getDdd02()));
		textTelefone02.setText(String.valueOf(entity.getTelefone02()));
 		textContato.setText(entity.getContato());
		textDddContato.setText(String.valueOf(entity.getDddContato()));
		textTelefoneContato.setText(String.valueOf(entity.getTelefoneContato()));
 	 	textEmail.setText(entity.getEmail());
 		textPix.setText(entity.getPix());
 		textObservacao.setText(entity.getObservacao());
		textPrazo.setText(String.valueOf(entity.getPrazo()));
		textParcela.setText(String.valueOf(entity.getParcela()));
  	}
 	
 	private void limpaNumeros(Fornecedor entity) {
 		if (entity.getNumero() == null)
 		{ entity.setNumero(0); 	}	
  		if (entity.getDdd01() == null)
 		{ entity.setDdd01(0); 	}	
 		if (entity.getDdd02() == null)
 		{ entity.setDdd02(0); 	}	
 		if (entity.getDddContato() == null)
 		{ entity.setDddContato(0); 	}	
 		if (entity.getTelefone01() == null)
 		{ entity.setTelefone01(0); 	}	
 		if (entity.getTelefone02() == null)
 		{ entity.setTelefone02(0); 	}	
 		if (entity.getTelefoneContato() == null)
 		{ entity.setTelefoneContato(0); 	}	
  	}
 	
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorRazaoSocial.setText((fields.contains("razaoSocial") ? erros.get("razaoSocial") : ""));
		labelErrorRua.setText((fields.contains("rua") ? erros.get("rua") : ""));
		labelErrorUF.setText((fields.contains("uf") ? erros.get("uf") : ""));
		labelErrorCep.setText((fields.contains("cep") ? erros.get("cep") : ""));
		labelErrorBairro.setText((fields.contains("bairro") ? erros.get("bairro") : ""));
		labelErrorCidade.setText((fields.contains("cidade") ? erros.get("cidade") : ""));
		labelErrorTelefone01.setText((fields.contains("telefone01") ? erros.get("telefone01") : ""));
		labelErrorEmail.setText((fields.contains("email") ? erros.get("email") : ""));
		labelErrorPrazo.setText((fields.contains("prazo") ? erros.get("prazo") : ""));
		labelErrorParcela.setText((fields.contains("parcela") ? erros.get("parcela") : ""));
 	}	
 } 
