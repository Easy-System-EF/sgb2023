package gui.sgb;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import exception.ValidationException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.FechamentoMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FechamentoMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.DataStaticSGB;
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

public class FechamentoMesFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	private FechamentoMes entity;
	private Funcionario objFun;
/*
 *  dependencia service com metodo set
 */
	private FechamentoMesService service;
	private AdiantamentoService adiService;
	private MesesService mesService;
	private AnosService anoService;
	private CartelaService carService;
	private CartelaVirtualService virService;
	private FuncionarioService funService;
	private TipoConsumoService tipoService;
	private ParcelaService parService;

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
	String classe = "Fechamento mes ";
	int mm = 0;
	int aa = 0;
	int dd = 0;
	int df = 0;
	private final int ddInicial = 01;
	private final int mmInicial = 01;
	private final int aaInicial = 2000;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
// data inicial e final - aberto e pago	
	Date dataInicialDespAberto = new Date();
	Date dataFinalDespAberto = new Date();
	Date dataInicialDespPago = new Date();
	Date dataFinalDespPago = new Date();

 	Integer mes = null;
 	Integer ano = null;
 	Integer dia = null;
 	String nomeMes = null;
	int qtdCar = 0;
 
	Calendar cal = Calendar.getInstance();
	Date dataHj = new Date();
	LocalDate dt1 = DataStaticSGB.dateParaLocal(dataHj);
	
// data inicial e final - aberto e pago	
	 
 	public void setFechMesEntityes(FechamentoMes entity,
 								 Funcionario objFun) {		
		this.entity = entity;
		this.objFun = objFun;
	}

 // 	 * metodo set /p service
 	public void setServices(FechamentoMesService service,
							AdiantamentoService adiService,
 							MesesService mesService,
 							AnosService anoService,
 							CartelaService carService,
 							CartelaVirtualService virService,
 							FuncionarioService funService,
 							TipoConsumoService tipoService,
 							ParcelaService parService) {
 		this.service = service;
 		this.adiService = adiService;
 		this.mesService = mesService;
 		this.anoService = anoService;
 		this.carService = carService;
 		this.virService = virService;
 		this.funService = funService;
 		this.tipoService = tipoService;
 		this.parService = parService;
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
     		montaFechamentoMensal();
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
		mes = DataStaticSGB.mesDaData(dt1);
		ano = DataStaticSGB.anoDaData(dt1);
//		cal = Calendar.getInstance();
//		cal.setTime(dataHj);
//		
//		mes = cal.get(Calendar.MONTH) + 1;
//		ano = cal.get(Calendar.YEAR);		
  		String tabelaMeses = "Janeiro de, Fevereiro de, Março de, Abril de, Maio de, Junho de, Julho de, "
       		         	+ "Agosto de, Setembro de, Outubro de, Novembro de, Dezembro de ";
  		String[] tabMes = tabelaMeses.split(",");
//	  	nomeMes = tabMes[mes - 1];
	  	nomeMes = tabMes[mes - 1];
  	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se n�o for atz
 * tb verificamos se cpos obrigat�rios est�o preenchidos, para informar erro(s)
 * para cpos string n�o precisa tryParse	
 */
	private FechamentoMes getFormData() {
		FechamentoMes obj = new FechamentoMes();
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

		df = DataStaticSGB.ultimoDiaMes(dt1);

		dt1 = DataStaticSGB.criaAnoMesDia(aaInicial, mmInicial, ddInicial);
		dataInicialDespAberto = DataStaticSGB.localParaDateFormatada(dt1);
				
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalDespAberto = DataStaticSGB.localParaDateFormatada(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, ddInicial);
		dataInicialDespPago = DataStaticSGB.localParaDateFormatada(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalDespPago = DataStaticSGB.localParaDateFormatada(dt1);
		
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

	private void montaFechamentoMensal() {
 		if (service == null) { 
			throw new IllegalStateException("Serviço FechamentoMesMensal está vazio");
 		}
 		if (adiService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (carService == null) {
			throw new IllegalStateException("Serviço Cartela está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		}
 		if (funService == null) {
			throw new IllegalStateException("Serviço Funcionário está vazio");
 		}
 		if (parService == null) {
			throw new IllegalStateException("Serviço Parcela está vazio");
 		}
 		if (tipoService == null) {
			throw new IllegalStateException("Serviço TipoConsumo está vazio");
 		}

 		classe = "Fechamento Mes Form 1 ";
		service.zeraAll();

		FechamentoMesListController.mesNome = ": " + nomeMes + " " + ano;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unused")
		double vlrAdiantamento = 0.00;
		double vlrFolha = 0.00;		
		int codTipo = 0;
		
// monta total de contas a pagar por tipo se servico	
		
		int mesp = mes;
		int anop = ano;
		classe = "TipoConsumo cod imposto  Fechamento Form";
		classe = "Parcela sum ";
		Double sumComAberto = parService.findSumAberto(dataInicialDespAberto, dataFinalDespAberto);
		Double sumComPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);
		qtdCar = carService.findByMesAnoFecha(mes, ano, mesp, anop).size();

		codTipo= tipoService.findPesquisa("Adiantamento");
		vlrAdiantamento = parService.findSum(ano, mes, codTipo);

// limpa comissão e salário dos funcionarios do mes em referencia ou anteriores
		classe = "Funconário 1   Fechamento Form";
		List<Funcionario> listFun = funService.findAll(ano, mes);
		listFun.removeIf(x -> x.getNomeFun().equals("Consumo Próprio"));
		for (Funcionario f : listFun) {
			if (f.getNomeFun() != null) {
				f.setComissaoFun(0.00);
				f.setAdiantamentoFun(0.00);
				f.setSalarioFun(f.getCargo().getSalarioCargo());
				objFun = f;
				funService.saveOrUpdate(objFun);
				vlrFolha += objFun.getSalarioFun();
			}	
		}

// atualiza comissao, adiantamento e salario atual dos funcionarios
		double acumulado = 0.00;
		double consumo = 0.00;
		List<Adiantamento> listAdi = new ArrayList<>();
		List<CartelaVirtual> listVir = new ArrayList<>();
		List<Cartela> listCar = new ArrayList<>();
		listCar = carService.findByMesAnoFecha(ano, mes, mesp, anop);
		try {
			for (Cartela c : listCar) {
				double vlrCartela = c.getTotalCar();
				entity.setCartelaFechamentoMes(String.valueOf(c.getNumeroCar()));
				entity.setDataFechamentoMes(sdf.format(c.getDataCar()));
				entity.setValorCartelaFechamentoMes(Mascaras.formataValor(c.getTotalCar()));
				entity.setSituacaoFechamentoMes(c.getNomeSituacaoCar());

				double comissao = 0.00;
				listAdi = adiService.findByCartela(c.getNumeroCar());
				for (Adiantamento a : listAdi) {
					if (a.getCartelaAdi().equals(c.getNumeroCar())) {
						if (a.getTipoAdi().equals("C")) {
							objFun = funService.findById(a.getCodigoFun());					
							if (a.getTipoAdi().contains("A")) {
								objFun.somaAdiantamento(a.getValeAdi());
							}	
							if (a.getTipoAdi().contains("C")) {
								objFun.somaComissao(a.getComissaoAdi());
								vlrFolha += a.getComissaoAdi();
								comissao += a.getComissaoAdi();
							}	
						}
					}
				}	
				
				listVir = virService.findCartela(c.getNumeroCar());
				double custo = 0.00;
				consumo = 0.00;
				int flagConsumo = 0;
				for (CartelaVirtual v : listVir) {
					custo += v.getQuantidadeProdVir() * v.getPrecoProdVir();
// se for consumo proprio (que não paga), nao tem resultado, so custo							

					if (v.getNomeFunVir().equals("Consumo Próprio")) {
						consumo = custo;
						flagConsumo = 1;
						vlrCartela = 0.00;
					}
				}	
				entity.setValorProdutoFechamentoMes(Mascaras.formataValor(custo));
				entity.setValorComissaoFechamentoMes(Mascaras.formataValor(comissao));
				if 	(entity.getValorResultadoFechamentoMes() == null) {
					entity.setValorResultadoFechamentoMes(Mascaras.formataValor(0.00));
				}
				double resultado = 0.00;
				if (c.getSituacaoCar().equals("P") && flagConsumo == 0) {
					if (consumo > 0) {
						resultado -= custo;
					} else {
						resultado = vlrCartela;
					}	
				} 
				if (flagConsumo == 1) {
					entity.setSituacaoFechamentoMes("C Prop");
					resultado = 0.00;
					resultado -= custo;
				} else {
					resultado -= (custo + comissao);							
				}
				entity.setValorResultadoFechamentoMes(Mascaras.formataValor(resultado));
				if (entity.getValorAcumuladoFechamentoMes() == null) {
					entity.setValorAcumuladoFechamentoMes(Mascaras.formataValor(0.00));
				}
				acumulado += resultado;
				entity.setValorAcumuladoFechamentoMes(Mascaras.formataValor(acumulado));
				classe = "entity Fechamento Form";
				funService.saveOrUpdate(objFun);
				service.insert(entity);
			}	
		} 
		catch (ParseException p) {
			p.getStackTrace();
		}
	
		double despesa = 0.00;
// monta tributos		
		if (vlrFolha > 0) {
			try {
				double porCartela = 0.00;
				double somaTudo = 0.00;
				somaTudo =  acumulado - (sumComAberto - sumComPago);
				despesa =  sumComAberto + sumComPago;
					if (qtdCar > 0) {
						porCartela = (somaTudo / qtdCar);
					} else {
						porCartela = somaTudo;
					}
				entity.setNumeroFechamentoMes(null);
				entity.setCartelaFechamentoMes("");
				entity.setDataFechamentoMes("");
				entity.setSituacaoFechamentoMes("");
				entity.setValorCartelaFechamentoMes("");
				entity.setValorProdutoFechamentoMes("");
				entity.setValorComissaoFechamentoMes("");
				entity.setValorResultadoFechamentoMes("");
				entity.setValorAcumuladoFechamentoMes("");
				service.insert(entity);			

				entity.setNumeroFechamentoMes(null);
				entity.setCartelaFechamentoMes("");
				entity.setDataFechamentoMes("");
				entity.setSituacaoFechamentoMes("");
				entity.setValorCartelaFechamentoMes("       === ");
				entity.setValorProdutoFechamentoMes("Fechamento");
				entity.setValorComissaoFechamentoMes(" === ");
				entity.setValorResultadoFechamentoMes("");
				entity.setValorAcumuladoFechamentoMes("");
				service.insert(entity);			

				entity.setNumeroFechamentoMes(null);
				entity.setCartelaFechamentoMes("");
				entity.setDataFechamentoMes("");
				entity.setSituacaoFechamentoMes("");
				entity.setValorCartelaFechamentoMes("");
				entity.setValorProdutoFechamentoMes("Despesa");
				entity.setValorComissaoFechamentoMes("Despesa");
				entity.setValorResultadoFechamentoMes("");
				entity.setValorAcumuladoFechamentoMes("");
				service.insert(entity);			

				entity.setNumeroFechamentoMes(null);
				entity.setCartelaFechamentoMes("");
				entity.setDataFechamentoMes("Total ===");
				entity.setSituacaoFechamentoMes("====>");
				entity.setValorCartelaFechamentoMes("Aberto +");
				entity.setValorProdutoFechamentoMes("Pago ");
				entity.setValorComissaoFechamentoMes("Folha =");
				entity.setValorResultadoFechamentoMes("Despesa - Receita");
				entity.setValorAcumuladoFechamentoMes("p/ Cartela");
				service.insert(entity);			

				entity.setNumeroFechamentoMes(null);
				entity.setCartelaFechamentoMes("");
				entity.setDataFechamentoMes(sdf.format(dataHj));
				entity.setValorCartelaFechamentoMes(Mascaras.formataValor(sumComAberto));
				entity.setValorProdutoFechamentoMes(Mascaras.formataValor(sumComPago));
				entity.setValorComissaoFechamentoMes(Mascaras.formataValor(vlrFolha));
				entity.setValorResultadoFechamentoMes(Mascaras.formataValor(somaTudo));
				entity.setValorAcumuladoFechamentoMes(Mascaras.formataValor(porCartela));
				service.insert(entity);			
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}	
		
		// monta percentuais		
				if (vlrFolha > 0) {
					try {
						entity.setNumeroFechamentoMes(null);
						entity.setCartelaFechamentoMes("");
						entity.setDataFechamentoMes("");
						entity.setSituacaoFechamentoMes("");
						entity.setValorCartelaFechamentoMes("");
						entity.setValorProdutoFechamentoMes("");
						entity.setValorComissaoFechamentoMes("");
						entity.setValorResultadoFechamentoMes("");
						entity.setValorAcumuladoFechamentoMes("");
						service.insert(entity);			

						double perDespesa = ((sumComAberto + sumComPago) * 100) / despesa;
						double perFolha = (vlrFolha * 100) / despesa;

						entity.setNumeroFechamentoMes(null);
						entity.setCartelaFechamentoMes("");
						entity.setDataFechamentoMes("");
						entity.setSituacaoFechamentoMes("");
						entity.setValorCartelaFechamentoMes("       === ");
						entity.setValorProdutoFechamentoMes("Percentual");
						entity.setValorComissaoFechamentoMes(" === ");
						entity.setValorResultadoFechamentoMes("");
						entity.setValorAcumuladoFechamentoMes("");
						service.insert(entity);			

						entity.setNumeroFechamentoMes(null);
						entity.setCartelaFechamentoMes("");
						entity.setDataFechamentoMes("Perc ====");
						entity.setSituacaoFechamentoMes("====>");
						entity.setValorCartelaFechamentoMes("% Desp.. +");
						entity.setValorProdutoFechamentoMes("");
						entity.setValorComissaoFechamentoMes("% Folha =");
						entity.setValorResultadoFechamentoMes("");
						entity.setValorAcumuladoFechamentoMes("");
						service.insert(entity);			

						entity.setNumeroFechamentoMes(null);
						entity.setCartelaFechamentoMes("");
						entity.setDataFechamentoMes(sdf.format(dataHj));
						entity.setValorCartelaFechamentoMes(Mascaras.formataValor(perDespesa));
						entity.setValorProdutoFechamentoMes("");
						entity.setValorComissaoFechamentoMes(Mascaras.formataValor(perFolha));
						service.insert(entity);			
					} catch (ParseException e) {
						e.printStackTrace();
					}	
				}	
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
  * transforma string da tela p/ o tipo no bco de entity 
  */
 	public void updateFormData() {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}

		comboBoxMeses.getSelectionModel().selectFirst();
		comboBoxAnos.getSelectionModel().selectFirst();
     }
 	
//	carrega entity do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de entity		
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