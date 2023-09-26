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
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Cryptograf;

public class RestauraPeriodo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraPeriodo(Integer count, String unid, String meioSgcp, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgcp + file + ext;
		ParPeriodo per = new ParPeriodo();		
		ParPeriodoService perService = new ParPeriodoService();
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regPer = null;
		String campoPer = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regPer = str1.split(" PERIODO ");
				for (int i = 1 ; i < regPer.length ; i++) {
					campoPer = regPer[i];
					String[] campo = campoPer.split(" , ");
					per.setIdPeriodo(Integer.parseInt(campo[0]));
					Date dti = sdfAno.parse(campo[1]);
					cal.setTime(dti);
					per.setDtiPeriodo(cal.getTime());
					Date dtf = sdfAno.parse(campo[2]);
					cal.setTime(dtf);
					per.setDtfPeriodo(cal.getTime());
					per.setFornecedor(forService.findById(Integer.parseInt(campo[3])));
					per.setTipoConsumo(tipoService.findById(Integer.parseInt(campo[4])));
					perService.update(per);
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
