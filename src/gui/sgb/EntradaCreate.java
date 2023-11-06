package gui.sgb;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.service.EntradaService;
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
import javafx.scene.control.Alert.AlertType;


public class EntradaCreate {
	private static EntradaService service = new EntradaService();
	private static TipoConsumoService tipoService = new TipoConsumoService();
	private static ParPeriodoService perService = new ParPeriodoService();
	private static FornecedorService forService = new FornecedorService();
	private static ParcelaService parService = new ParcelaService();
	private static CompromissoService comService = new CompromissoService();
	private static Compromisso com = new Compromisso();
	private static ParPeriodo per = new ParPeriodo();
	private static TipoConsumo tipo = new TipoConsumo();
	private static Fornecedor forn = new Fornecedor();
	
	@SuppressWarnings("unused")
	public static void compromissoCreate(Entrada entity) {
		String classe = "Entrada Form";
		double vlr = 0.00;
		Integer codEnt = 0;
		Integer codCom = 0;
		List<Entrada> listVlr = service.findByNnf(entity.getNnfEnt());
		for (Entrada e : listVlr ) {
			if (e.getForn().getCodigo().equals(entity.getForn().getCodigo())) {   
				vlr += e.getQuantidadeProdEnt() * e.getValorProdEnt();
				codEnt = e.getNumeroEnt();
			}	
		}	
		classe = "Compromisso Ent Form";
		com = comService.findById(entity.getForn().getCodigo(), entity.getNnfEnt());
		if (com == null) {
			codCom = 1;
		} else {
			if (!com.getValorCom().equals(vlr)) {	
				codCom = com.getIdCom(); 
				int flagD = 0;
				conferePagamento(flagD, com);
				if (flagD > 0 ) {
					Alerts.showAlert("Atenção!!!   Verifique Contas a Pagar", "Forn.: " + 
						com	.getFornecedor().getRazaoSocial() + " " + " - Nnf: " + 
						com.getNnfCom(), "Valor não confere!!! ", AlertType.ERROR);
					codCom = 0;
				}	
			} 
		}	

		if (codCom > 0 ) {
			classe = "Tipo Consumo Ent Form";
			List<TipoConsumo> listTipo = tipoService.findAll();
			for (TipoConsumo tc : listTipo) {
				if (tc.getNomeTipo().equals("Empresa")) {
					tipo = tipoService.findById(tc.getCodigoTipo());
				}
			}	
			classe = "Periodo Ent Form";
			per = perService.findById(1);
			classe = "Fornecedor Ent Form";
			forn = forService.findById(entity.getForn().getCodigo());
			classe = "Compromisso Ent Form";
			comService.remove(entity.getForn().getCodigo(), entity.getNnfEnt());
			parService.removeNnf(entity.getNnfEnt(), entity.getForn().getCodigo());
			codCom = null;
			Date data1o = CalculaParcela.CalculaVencimentoDia(entity.getDataEnt(), 1, forn.getPrazo());
			int sit = 0;
			if (forn.getParcela() == 1 && forn.getPrazo() == 1) {
				sit = 1;
			}
			com = new Compromisso(codCom, forn.getCodigo(), forn.getRazaoSocial(), entity.getNnfEnt(), 
				entity.getDataEnt(), data1o, vlr, forn.getParcela(), forn.getPrazo(), forn, tipo, per, sit);
			comService.saveOrUpdate(com);
			CalculaParcela.parcelaCreate(com);
		}	
	}
	
	private static int conferePagamento(int flagD, Compromisso obj) {
	flagD = 0;
		List<Parcela> list = parService.findByIdFornecedorNnf(obj.getCodigoFornecedorCom(), obj.getNnfCom());
		List<Parcela> st = list.stream()
			.filter(p -> p.getNnfPar() == (obj.getNnfCom()))
			.filter(p -> p.getFornecedor().getCodigo() == (obj.getCodigoFornecedorCom()))  
			.filter(p -> p.getPagoPar() > 0)
			.collect(Collectors.toList());	
		if (st.size() > 0) {
			Alerts.showAlert("Erro!!! ", "Parcela paga ", "Existe(m) parcela(s) paga(s) para o Compromisso!!!", AlertType.ERROR);
			flagD = 1;
		}	
		return flagD;
	}		
}
