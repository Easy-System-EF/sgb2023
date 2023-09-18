package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.FolhaMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.FolhaMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
import gui.util.Mascaras;
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

public class FolhaMesFormController implements Initializable {

	private FolhaMes entity;
/*
 *  dependencia service com metodo set
 */
	private FolhaMesService service;
	private AdiantamentoService adService;
	private FuncionarioService funService;
	private MesesService mesService;
	private AnosService anoService;

	private Funcionario objFun;

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
 	String classe = "";
 	String nomeMes = null;
 	Integer mes = null;
 	Integer ano = null;
 	
	Calendar cal = Calendar.getInstance();
 
 	public void setFolhaMes(FolhaMes entity,
 							Funcionario objFun) {		
		this.entity = entity;
		this.objFun = objFun;
	}

 // 	 * metodo set /p service
 	public void setServices(FolhaMesService service, 
 							AdiantamentoService adService,
 							FuncionarioService funService,
 							MesesService mesService,
 							AnosService anoService) {
 		this.service = service;
 		this.adService = adService;
 		this.funService = funService;
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
			throw new IllegalStateException("Entidade nula");
		}
		try {
     		entity = getFormData();
     		montaFolha(entity);
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
	
	private void datas() {
		Date dataHj = new Date();
		cal = Calendar.getInstance();
		cal.setTime(dataHj);
		mes = cal.get(Calendar.MONTH) + 1;
		ano = cal.get(Calendar.YEAR);		
  		String tabelaMeses = "Janeiro de, Fevereiro de, Março de, Abril de, Maio de, Junho de, Julho de, "
       		         	+ "Agoto de, Setembro de, Outubro de, Novembro de, Dezembro de ";
  		String[] tabMes = tabelaMeses.split(",");
	  	nomeMes = tabMes[mes - 1];
  	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se n�o for atz
 * tb verificamos se cpos obrigat�rios est�o preenchidos, para informar erro(s)
 * para cpos string n�o precisa tryParse	
 */
	private FolhaMes getFormData() {
		FolhaMes obj = new FolhaMes();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

		int mm = comboBoxMeses.getValue().getNumeroMes();
 		if (mes == null || mes != mm) {
 		 	exception.addErros("meses", "mes inválido");
		}

 		int aa = comboBoxAnos.getValue().getAnoAnos();
 		if (ano == null || ano != aa) {
 		 	exception.addErros("anos", "ano inválido");
		}

 		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	}
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void montaFolha(FolhaMes entity2) {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (funService == null) {
			throw new IllegalStateException("Serviço Funcionarios está vazio");
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço FolhaFolha está vazio");
 		}
 		try {
			classe = "FolhaMes 1 Form";
			zeraMes();
			
// aqui pego os func's que não tem vale ou comissão			
			int cod = 0;
			while (cod < 999999) {
				cod += 1;
				objFun = funService.findById(cod);
				if (objFun == null) {
					cod = 999999;
				} else {
					if (objFun.getNomeFun() != "Consumo Próprio") {
						objFun.setSalarioFun(objFun.getCargo().getSalarioCargo());
						objFun.setComissaoFun(0.00);
						objFun.setAdiantamentoFun(0.00);
						funService.saveOrUpdate(objFun);
					}	
				}	
			}	

			FolhaMesListController.mes = ": " + nomeMes + " " + ano;
			classe = "Adiantamento Folha 0 Folha mes Form";
			List<Adiantamento> adianto = adService.findMes(mes, ano);
			for (Adiantamento a : adianto) {
				if (a.getNumeroAdi() != null) {
					classe = "Funcionário Folha mes Form";
					objFun = funService.findById(a.getCodigoFun());
					if (a.getTipoAdi().equals("A")) {
						objFun.setAdiantamentoFun(adService.valeSumTotal(mes, ano, objFun.getCodigoFun()));
					}
					if (a.getTipoAdi().equals("C")) {
						objFun.setComissaoFun(adService.comSumTotal(mes, ano, objFun.getCodigoFun()));
					}	
					objFun.setSalarioFun(a.getSalarioAdi());
					funService.saveOrUpdate(objFun);
				}
			}	
			
			Double totRec = 0.0;			
			Double totFol = 0.0;			

			classe = "Funcionário Folha 2 Folha mes Form";
			List<Funcionario> listFun = funService.findAll(ano, mes);
			listFun.removeIf(x -> x.getNomeFun().equals("Consumo Próprio"));
			for (Funcionario f : listFun) {
				entity.setNumeroFolha(null);
				entity.setFuncionarioFolha(f.getNomeFun());
				entity.setCargoFolha(f.getCargoFun());
				entity.setSituacaoFolha(f.getSituacaoFun());
				entity.setComissaoFolha(Mascaras.formataValor(f.getComissaoFun()));
				entity.setValeFolha(Mascaras.formataValor(f.getAdiantamentoFun()));
				entity.setSalarioFolha(Mascaras.formataValor(f.getSalarioFun()));
				entity.setMesFolha(mes);
				entity.setAnoFolha(ano);
				totRec = f.TotalMesFun();
				entity.setReceberFolha(Mascaras.formataValor(totRec));
				totFol += totRec;
				entity.setTotalFolha(Mascaras.formataValor(totFol));
				service.insert(entity);
			}
		}
 		catch (ParseException p) {
 			p.getStackTrace();
 		}
	}
	
	public void zeraMes() {
		service.zeraAll();
	}

/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		datas();
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
  * transforma string da tela p/ o tipo no bco de Folha 
  */
 	public void updateFormData() {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}

 		comboBoxMeses.getSelectionModel().selectFirst();
		comboBoxAnos.getSelectionModel().selectFirst();
 	}	

// 	carrega Folha do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de Folha
		Meses mm = mesService.findId(mes);
		List<Meses> listMes = new ArrayList<>();
		listMes.add(mm);
		Anos aa = anoService.findAno(ano);
		List<Anos> listAno = new ArrayList<>();
		listAno.add(aa);
 		obsListMes = FXCollections.observableArrayList(listMes);
		comboBoxMeses.setItems(obsListMes);
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