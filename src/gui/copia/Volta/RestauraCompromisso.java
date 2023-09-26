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
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Cryptograf;

public class RestauraCompromisso implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraCompromisso(Integer count, String unid, String meioSgcp, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgcp + file + ext;
		Compromisso comp = new Compromisso();		
		CompromissoService comService = new CompromissoService();
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
		ParPeriodoService perService = new ParPeriodoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regCom = null;
		String campoCom = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regCom = str1.split(" COMPROMISSO ");
				for (int i = 1 ; i < regCom.length ; i++) {
					campoCom = regCom[i];
					String[] campo = campoCom.split(" , ");
					comp.setIdCom(Integer.parseInt(campo[0]));
					comp.setCodigoFornecedorCom(Integer.parseInt(campo[1]));
					comp.setNomeFornecedorCom(campo[2]);
					comp.setNnfCom(Integer.parseInt(campo[3]));
					Date dtcom = sdfAno.parse(campo[4]);
					cal.setTime(dtcom);
					comp.setDataCom(cal.getTime());
					Date dtvem = sdfAno.parse(campo[5]);
					cal.setTime(dtvem);
					comp.setDataVencimentoCom(cal.getTime());
					comp.setValorCom(Double.parseDouble(campo[6]));
					comp.setParcelaCom(Integer.parseInt(campo[7]));
					comp.setPrazoCom(Integer.parseInt(campo[8]));
					comp.setFornecedor(forService.findById(Integer.parseInt(campo[9])));
					comp.setTipoFornecedor(tipoService.findById(Integer.parseInt(campo[10])));
					comp.setParPeriodo(perService.findById(Integer.parseInt(campo[11])));
					
					comService.insertBackUp(comp);
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
