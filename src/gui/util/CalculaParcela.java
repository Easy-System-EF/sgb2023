package gui.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.ParcelaService;
import javafx.scene.control.Alert.AlertType;

public class CalculaParcela {

	String classe = "Parcela Create";

	public static Date CalculaVencimentoDia(Date data, int parcela, int dia) {
		Date dataVen = data;
		if (dia > 1) {
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(data);
			cal.add(Calendar.DAY_OF_MONTH, (dia * parcela));
			dataVen = cal.getTime();
		}	
		return dataVen;
	}
	
	public static Date CalculaVencimentoMes(Date data, int mes) {
   		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data);
		cal.add(Calendar.MONTH, (mes));
 		Date dataVen = cal.getTime();
		return dataVen;
	}
	
	public static Double calculaParcelas(Double valor, int qtd, int parc) {
		double a = valor / qtd;
		double b = a * qtd;
		double c = 0.00;
		double d = 0.00;
		if (b == valor) {
			c = b / qtd;
			d = c;
		} else {
			a = valor % qtd;
			b = valor - a;
			c = b / qtd;
			d = c + a;
		}	
		if (parc == 1) {
		 	valor = d;
		} else {
			valor = c;
			}
		return valor;
	}

	public static void parcelaCreate(Compromisso objCom) {
  		ParcelaService parService = new ParcelaService();
  		List<Parcela> list = parService.findByIdFornecedorNnf(objCom.getNnfCom(), objCom.getCodigoFornecedorCom());
  		for (Parcela p : list) {
  			if (p.getNnfPar() != null) {
  				if (p.getNnfPar() == objCom.getNnfCom() && p.getFornecedor().getCodigo() == objCom.getCodigoFornecedorCom()) {
  					parService.removeNnf(objCom.getNnfCom(), objCom.getCodigoFornecedorCom());
  				}	
  			}
  		}	
   		Calendar cal = Calendar.getInstance(); 
   		if (objCom.getDataVencimentoCom().after(objCom.getDataCom())) {
   			cal.setTime(objCom.getDataVencimentoCom());
   		} else {
   			cal.setTime(objCom.getDataCom());
   		}
   		
   		if (objCom.getDataCom().after(objCom.getDataVencimentoCom())) {
   			Alerts.showAlert("Data inválida!!!", "vencimento maior que data da operação", null, AlertType.ERROR);   			
   		} else {
   			for (int i = 1; i < objCom.getParcelaCom() + 1; i ++) {
   				if (objCom.getDataCom().equals(objCom.getDataVencimentoCom())) {
   					if (objCom.getPrazoCom() > 1) {
   						cal.add(Calendar.DAY_OF_MONTH, (objCom.getPrazoCom()));   					
   					}
   				}
   				if (objCom.getDataCom().before(objCom.getDataVencimentoCom())) {
   					if (objCom.getParcelaCom() > 1 && i > 1) {
   						cal.add(Calendar.DAY_OF_MONTH, (objCom.getPrazoCom()));   					
   					}
   				}
   				Parcela parcela = new Parcela();
   				parcela.setIdPar(null);
   				parcela.setCodigoFornecedorPar(objCom.getCodigoFornecedorCom());
   				parcela.setNomeFornecedorPar(objCom.getNomeFornecedorCom());
   				parcela.setNnfPar(objCom.getNnfCom());
   				parcela.setNumeroPar(i);
   				parcela.setDataVencimentoPar(cal.getTime());
   				parcela.setValorPar(calculaParcelas(objCom.getValorCom(), objCom.getParcelaCom(), i ));
   				parcela.setDescontoPar(0.00);
   				parcela.setJurosPar(0.00);
   				parcela.setTotalPar(parcela.getValorPar()); 
   				parcela.setPagoPar(0.00);
   				if (objCom.getPrazoCom() == 1 || objCom.getParcelaCom() == 1) {
   	   				if (objCom.getDataCom().equals(parcela.getDataVencimentoPar())) {
   						parcela.setPagoPar(objCom.getValorCom());
   					}	
   				}
   				parcela.setDataPagamentoPar(cal.getTime());
   				parcela.setFornecedor(objCom.getFornecedor());
   				parcela.setTipoFornecedor(objCom.getTipoFornecedor());
   				parcela.setPeriodo(objCom.getParPeriodo());
   				cal.setTime(parcela.getDataVencimentoPar());
   				parService.saveUpdate(parcela);
   			}	
   		}
	}
}
