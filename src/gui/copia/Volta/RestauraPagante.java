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
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.util.Cryptograf;

public class RestauraPagante  implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraPagante(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		CartelaPagante pag = new CartelaPagante();		
		CartelaPaganteService pagService = new CartelaPaganteService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regPag = null;
		String campoPag = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regPag = str1.split(" CARTELAPAGANTE ");
				for (int i = 1 ; i < regPag.length ; i++) {
					campoPag = regPag[i];
					String[] campo = campoPag.split(" , ");
					pag.setNumeroCartelaPag(Integer.parseInt(campo[0].replaceAll("\s", "")));
					pag.setPaganteCartelaPag(Integer.parseInt(campo[1]));
					Date dataPag = sdfAno.parse(campo[2]);
					cal.setTime(dataPag);
					pag.setDataCartelaPag(cal.getTime());
					pag.setLocalCartelaPag(campo[3]);
					pag.setValorCartelaPag(Double.parseDouble(campo[4]));
					pag.setFormaCartelaPag(campo[5]);
					pag.setSituacaoCartelaPag(campo[6]);
					pag.setCartelaIdOrigemPag(Integer.parseInt(campo[7]));
					pag.setMesCartelaPag(Integer.parseInt(campo[8]));
					pag.setAnoCartelaPag(Integer.parseInt(campo[9]));
					pag.setMesPagamentoPag(Integer.parseInt(campo[10]));
					pag.setAnoPagamentoPag(Integer.parseInt(campo[11]));
					pagService.insertBackUp(pag);
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
