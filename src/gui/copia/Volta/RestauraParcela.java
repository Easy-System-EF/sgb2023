package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import db.DbException;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Cryptograf;

public class RestauraParcela implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraParcela(Integer count, String unid, String meioSgcp, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgcp + file + ext;
		Parcela par = new Parcela();		
		ParcelaService parService = new ParcelaService();
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regpar = null;
		String campopar = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regpar = str1.split(" PARCELA ");
				for (int i = 1 ; i < regpar.length ; i++) {
					campopar = regpar[i];
					String[] campo = campopar.split(" , ");
					par.setIdPar(Integer.parseInt(campo[0]));
					par.setCodigoFornecedorPar(Integer.parseInt(campo[1]));
					par.setNomeFornecedorPar(campo[2]);
					par.setNnfPar(Integer.parseInt(campo[3]));
					par.setNumeroPar(Integer.parseInt(campo[4]));
					Date dtpar = sdfAno.parse(campo[5]);
					cal.setTime(dtpar);
					par.setDataVencimentoPar(cal.getTime());
					par.setValorPar(Double.parseDouble(campo[6]));
					par.setDescontoPar(Double.parseDouble(campo[7]));
					par.setJurosPar(Double.parseDouble(campo[8]));
					par.setTotalPar(Double.parseDouble(campo[9]));
					par.setPagoPar(Double.parseDouble(campo[10]));
					Date dtpag = sdfAno.parse(campo[11]);
					cal.setTime(dtpag);
					par.setDataPagamentoPar(cal.getTime());
					par.setFornecedor(forService.findById(Integer.parseInt(campo[12])));
					par.setTipoFornecedor(tipoService.findById(Integer.parseInt(campo[13])));
					par.setPeriodo(perService.findById(Integer.parseInt(campo[14])));
					
					parService.insertBackUp(par); 
					count += 1;
				}
			}	
			return count;
		}	
		catch(IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		catch(ParseException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
