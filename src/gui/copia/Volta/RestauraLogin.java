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
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.service.LoginService;
import gui.util.Cryptograf;

public class RestauraLogin  implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraLogin(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Login log = new Login();		
		LoginService logService = new LoginService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regLog = null;
		String campoLog = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regLog = str1.split(" LOGIN ");
				for (int i = 1 ; i < regLog.length ; i++) {
					campoLog = regLog[i];
					String[] campo = campoLog.split(" , ");
					log.setNumeroLog(Integer.parseInt(campo[0]));
					log.setSenhaLog(campo[1]);
					log.setNomeLog(campo[2]);
					log.setNivelLog(Integer.parseInt(campo[3]));
					log.setAlertaLog(Integer.parseInt(campo[4]));
					Date dtLog = sdfAno.parse(campo[5]);
					Date dtVen = sdfAno.parse(campo[6]);
					Date dtMax = sdfAno.parse(campo[7]);
					Date dtAce = sdfAno.parse(campo[8]);
					cal.setTime(dtLog);
					log.setDataLog(cal.getTime());
					cal.setTime(dtVen);
					log.setDataVencimentoLog(cal.getTime());
					cal.setTime(dtMax);
					log.setDataMaximaLog(cal.getTime());
					cal.setTime(dtAce);
					log.setAcessoLog(dtAce);
					log.setEmpresaLog(Integer.parseInt(campo[9]));

					logService.insertBackUp(log);
					count += 1;
				}
				System.out.println();
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
