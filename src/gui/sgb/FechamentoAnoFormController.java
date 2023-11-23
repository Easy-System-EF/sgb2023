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
import java.util.ResourceBundle;

import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.FechamentoAno;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FechamentoAnoService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.DataStaticSGB;
import gui.util.Mascaras;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class FechamentoAnoFormController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	private FechamentoAno entity;
	@SuppressWarnings("unused")
	private Funcionario objFun;
/*
 *  dependencia service com metodo set
 */
	private FechamentoAnoService service;
	private AdiantamentoService adiService;
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

//auxiliar
	String classe = "Fechamento mes ";
	int mm = 0;
	int aa = 0;
	int dd = 0;
	int df = 0;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
// data inicial e final - aberto e pago	
	Date dataInicialDespAberto = new Date();
	Date dataFinalDespAberto = new Date();
	Date dataInicialDespPago = new Date();
	Date dataFinalDespPago = new Date();
	Date dataInicialRecAberto = new Date();
	Date dataFinalRecAberto = new Date();
	Date dataInicialRecPago = new Date();
	Date dataFinalRecPago = new Date();

	int qtdCar = 0;
 
	Calendar cal = Calendar.getInstance();
	
// data inicial e final - aberto e pago	
	 
 	public void setDadosEntitys(FechamentoAno entity, Funcionario objFun) {		
		this.entity = entity;
		this.objFun = objFun;
		
	}

 // 	 * metodo set /p service
 	public void setServices(FechamentoAnoService service,
							AdiantamentoService adiService,
 							CartelaService carService,
 							CartelaVirtualService virService,
 							FuncionarioService funService,
 							TipoConsumoService tipoService,
 							ParcelaService parService) {
 		this.service = service;
 		this.adiService = adiService;
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
   		getFormData();
   		montaFechamentoAnual();
    	notifyDataChangeListerners();
//    	Utils.currentStage(null).close();
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
	private void getFormData() {
		LocalDate ldt = DataStaticSGB.criaLocalAtual();
		mm = DataStaticSGB.mesDaData(ldt);
 		aa = DataStaticSGB.anoDaData(ldt);
 		df = DataStaticSGB.ultimoDiaMes(ldt);
  		
  		LocalDate dt1 = DataStaticSGB.criaAnoMesDia(2001, 01, 01);
		dataInicialDespAberto = DataStaticSGB.localParaDateSdfAno(dt1);

		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalDespAberto = DataStaticSGB.localParaDateSdfAno(dt1);

		dt1 = DataStaticSGB.criaAnoMesDia(aa, 01, 01);
		dataInicialDespPago = DataStaticSGB.localParaDateSdfAno(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalDespPago = DataStaticSGB.localParaDateSdfAno(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(2001, 01, 01);
		dataInicialRecAberto = DataStaticSGB.localParaDateSdfAno(dt1);
				
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalRecAberto = DataStaticSGB.localParaDateSdfAno(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(aa, 01, 01);
		dataInicialRecPago = DataStaticSGB.localParaDateSdfAno(dt1);
		
		dt1 = DataStaticSGB.criaAnoMesDia(aa, mm, df);
		dataFinalRecPago = DataStaticSGB.localParaDateSdfAno(dt1);
		
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.set(Calendar.MONTH, 0);
//		cal.set(Calendar.YEAR, 2001);
//		dtIA = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, df);
//		dtFA = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		dtIP = cal.getTime();
//
//		cal.setTime(data1);
//		cal.set(Calendar.DAY_OF_MONTH, df);
//		dtFP = cal.getTime();
	}
	
	private void montaFechamentoAnual() {
 		if (service == null) { 
			throw new IllegalStateException("Serviço FechamentoAnoMensal está vazio");
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

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unused")
		double vlrAdiantamento = 0.00;
		double vlrFolha = 0.00;		
		int codTipo = 0;
		
// monta total de contas a pagar por tipo se servico	
		
		classe = "Parcela sum ";
		Double sumComAberto = parService.findSumAberto(dataInicialDespAberto, dataFinalDespAberto);
		Double sumComPago = parService.findSumPago(dataInicialDespPago, dataFinalDespPago);

		codTipo = tipoService.findPesquisa("Adiantamento");
		vlrAdiantamento = parService.findSum(aa, mm, codTipo);
// limpa comissão e salário dos funcionarios do mes em referencia ou anteriores
		classe = "Funconário 1   Fechamento Form";
		List<Funcionario> listFun = funService.findAll(aa, mm);
		listFun.removeIf(x -> x.getNomeFun().equals("Consumo Próprio"));
		listFun.removeIf(x -> x.getNomeFun().equals("Consumo Proprio"));
		for (Funcionario f : listFun) {
			f.setComissaoFun(0.00);
			f.setAdiantamentoFun(0.00);
			if (f.getSituacaoFun().equals("Ativo")) {
				f.setSalarioFun(f.getCargo().getSalarioCargo());	
				vlrFolha += f.getSalarioFun();
			}	
			funService.saveOrUpdate(f);			
		}
		
// atualiza comissao, adiantamento e salario atual dos funcionarios
		double acumulado = 0.00;

		List<Adiantamento> listAdi = new ArrayList<>();
		List<CartelaVirtual> listVir = new ArrayList<>();
		List<Cartela> listCar = new ArrayList<>();
		listCar = carService.findByAno(aa);
		qtdCar = listCar.size();
		try {
			for (Cartela c : listCar) {
				entity.setCartelaFechamentoAno(String.valueOf(c.getNumeroCar()));
				entity.setDataFechamentoAno(sdf.format(c.getDataCar()));
				entity.setSituacaoFechamentoAno(c.getNomeSituacaoCar());
				entity.setValorCartelaFechamentoAno(Mascaras.formataValor(c.getTotalCar() - c.getDescontoCar()));
				double vlrCartela = c.getTotalCar() - c.getDescontoCar();
				double comissao = 0.00;
				listAdi = adiService.findByCartela(c.getNumeroCar());
				for (Adiantamento a : listAdi) {
					if (a.getTipoAdi().equals("C")) {
						vlrFolha += a.getComissaoAdi();
						comissao += a.getComissaoAdi();
					}
				}					
				listVir = virService.findCartela(c.getNumeroCar());
				double custo = 0.00;
				for (CartelaVirtual v : listVir) {
					custo += v.getQuantidadeProdVir() * v.getPrecoProdVir();
// se for consumo proprio (que não paga), nao tem resultado, so custo							
				}	
				entity.setValorProdutoFechamentoAno(Mascaras.formataValor(custo));
				entity.setValorComissaoFechamentoAno(Mascaras.formataValor(comissao));
				if 	(entity.getValorResultadoFechamentoAno() == null) {
					entity.setValorResultadoFechamentoAno(Mascaras.formataValor(0.00));
				}
				double resultado = 0.00;
				resultado = vlrCartela - (custo + comissao);							
				entity.setValorResultadoFechamentoAno(Mascaras.formataValor(resultado));
				if (entity.getValorAcumuladoFechamentoAno() == null) {
					entity.setValorAcumuladoFechamentoAno(Mascaras.formataValor(0.00));
				}
				acumulado += resultado;
				entity.setValorAcumuladoFechamentoAno(Mascaras.formataValor(acumulado));
				classe = "entity Fechamento Form";
				service.insert(entity);
			}	
		} 
		catch (ParseException p) {
			p.getStackTrace();
		}
	
		@SuppressWarnings("unused")
		double despesa = 0.00;
// monta tributos		
		if (vlrFolha > 0) {
			try {
				double porCartela = 0.00;
				double somaTudo = 0.00;
				somaTudo =  acumulado - (sumComAberto);
				despesa =  sumComAberto + sumComPago;
					if (qtdCar > 0) {
						porCartela = (somaTudo / qtdCar);
					} else {
						porCartela = somaTudo;
					}

				entity.setNumeroFechamentoAno(null);
				entity.setCartelaFechamentoAno("");
				entity.setDataFechamentoAno("");
				entity.setSituacaoFechamentoAno("");
				entity.setValorCartelaFechamentoAno("");
				entity.setValorProdutoFechamentoAno("");
				entity.setValorComissaoFechamentoAno("");
				entity.setValorResultadoFechamentoAno("");
				entity.setValorAcumuladoFechamentoAno("");
				service.insert(entity);			

				entity.setNumeroFechamentoAno(null);
				entity.setCartelaFechamentoAno("");
				entity.setDataFechamentoAno("");
				entity.setSituacaoFechamentoAno("");
				entity.setValorCartelaFechamentoAno("       === ");
				entity.setValorProdutoFechamentoAno("Fechamento");
				entity.setValorComissaoFechamentoAno(" === ");
				entity.setValorResultadoFechamentoAno("");
				entity.setValorAcumuladoFechamentoAno("");
				service.insert(entity);			

				entity.setNumeroFechamentoAno(null);
				entity.setCartelaFechamentoAno("");
				entity.setDataFechamentoAno("");
				entity.setSituacaoFechamentoAno("");
				entity.setValorCartelaFechamentoAno("");
				entity.setValorProdutoFechamentoAno("Despesa");
				entity.setValorComissaoFechamentoAno(" ==== ");
				entity.setValorResultadoFechamentoAno("");
				entity.setValorAcumuladoFechamentoAno("");
				service.insert(entity);			

				entity.setNumeroFechamentoAno(null);
				entity.setCartelaFechamentoAno("");
				entity.setDataFechamentoAno("Total ===");
				entity.setSituacaoFechamentoAno("====>");
				entity.setValorCartelaFechamentoAno("Aberto +");
				entity.setValorProdutoFechamentoAno("Pago ");
				entity.setValorComissaoFechamentoAno("# Folha");
				entity.setValorResultadoFechamentoAno("Despesa - Receita");
				entity.setValorAcumuladoFechamentoAno("p/ Cartela");
				service.insert(entity);			

				entity.setNumeroFechamentoAno(null);
				entity.setCartelaFechamentoAno("");
				entity.setDataFechamentoAno("===");
				entity.setValorCartelaFechamentoAno(Mascaras.formataValor(sumComAberto));
				entity.setValorProdutoFechamentoAno(Mascaras.formataValor(sumComPago));
				entity.setValorComissaoFechamentoAno(Mascaras.formataValor(vlrFolha));
				entity.setValorResultadoFechamentoAno(Mascaras.formataValor(somaTudo));
				entity.setValorAcumuladoFechamentoAno(Mascaras.formataValor(porCartela));
				service.insert(entity);			
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}	
		
		// monta percentuais		
				if (vlrFolha > 0) {
					try {
						entity.setNumeroFechamentoAno(null);
						entity.setCartelaFechamentoAno("");
						entity.setDataFechamentoAno("");
						entity.setSituacaoFechamentoAno("");
						entity.setValorCartelaFechamentoAno("");
						entity.setValorProdutoFechamentoAno("");
						entity.setValorComissaoFechamentoAno("");
						entity.setValorResultadoFechamentoAno("");
						entity.setValorAcumuladoFechamentoAno("");
						service.insert(entity);			

						double perDespesa = ((acumulado - sumComAberto) * 100) / acumulado; 
						double perFolha = ((acumulado - vlrFolha) * 100) / acumulado;

						entity.setNumeroFechamentoAno(null);
						entity.setCartelaFechamentoAno("");
						entity.setDataFechamentoAno("");
						entity.setSituacaoFechamentoAno("");
						entity.setValorCartelaFechamentoAno("       === ");
						entity.setValorProdutoFechamentoAno("Percentual");
						entity.setValorComissaoFechamentoAno(" === ");
						entity.setValorResultadoFechamentoAno("");
						entity.setValorAcumuladoFechamentoAno("");
						service.insert(entity);			

						entity.setNumeroFechamentoAno(null);
						entity.setCartelaFechamentoAno("");
						entity.setDataFechamentoAno("Perc rec ");
						entity.setSituacaoFechamentoAno(">< =>");
						entity.setValorCartelaFechamentoAno("% Desp.. +");
						entity.setValorProdutoFechamentoAno("");
						entity.setValorComissaoFechamentoAno("% Folha =");
						entity.setValorResultadoFechamentoAno("");
						entity.setValorAcumuladoFechamentoAno("");
						service.insert(entity);			

						entity.setNumeroFechamentoAno(null);
						entity.setCartelaFechamentoAno("");
						entity.setDataFechamentoAno("===");
						entity.setValorCartelaFechamentoAno(Mascaras.formataValor(perDespesa));
						entity.setValorProdutoFechamentoAno("");
						entity.setValorComissaoFechamentoAno(Mascaras.formataValor(perFolha));
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
  		updateFormData();
    }

 /*
  * transforma string da tela p/ o tipo no bco de entity 
  */
 	public void updateFormData() {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
     } 	
}	