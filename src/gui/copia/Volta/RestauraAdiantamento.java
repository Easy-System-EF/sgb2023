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
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.SituacaoService;
import gui.util.Cryptograf;

public class RestauraAdiantamento  implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraAdiantamento(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Adiantamento adi = new Adiantamento();		
		AdiantamentoService adiService = new AdiantamentoService();
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regAdi = null;
		String campoAdi = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();	
				str1 = Cryptograf.desCriptografa(str);
				regAdi = str1.split(" ADI ");
				for (int i = 1 ; i < regAdi.length ; i++) {
					campoAdi = regAdi[i];
					String[] campo = campoAdi.split(" , ");
					adi.setNumeroAdi(Integer.parseInt(campo[0].replaceAll("\s", "")));
					Date data = sdfAno.parse(campo[1]);
					cal.setTime(data); 
					adi.setDataAdi(cal.getTime());
					adi.setValeAdi(Double.parseDouble(campo[2]));
					adi.setMesAdi(Integer.parseInt(campo[3]));
					adi.setAnoAdi(Integer.parseInt(campo[4]));
					adi.setValorCartelaAdi(Double.parseDouble(campo[5]));
					adi.setCartelaAdi(Integer.parseInt(campo[6]));
					adi.setComissaoAdi(Double.parseDouble(campo[7]));
					adi.setTipoAdi(campo[8]);
					adi.setSalarioAdi(Double.parseDouble(campo[9]));
					adi.setCodigoFun(Integer.parseInt(campo[10]));
					adi.setNomeFun(campo[11]);
					adi.setMesFun(Integer.parseInt(campo[12]));
					adi.setAnoFun(Integer.parseInt(campo[13]));
					adi.setCargoFun(campo[14]);
					adi.setSituacaoFun(campo[15]);
					adi.setSalarioFun(Double.parseDouble(campo[16]));
					adi.setCargo(carService.findById(Integer.parseInt(campo[17])));
					adi.setSituacao(sitService.findById(Integer.parseInt(campo[18])));
					adiService.insertBackUp(adi);
					count += 1;
				}
			}	
			return count;
		}	
		catch(	ParseException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
		}
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
