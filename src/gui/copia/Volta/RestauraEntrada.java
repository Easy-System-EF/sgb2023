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
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.util.Cryptograf;

public class RestauraEntrada implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraEntrada(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Entrada ent = new Entrada();		
		EntradaService entService = new EntradaService();
		FornecedorService forService = new FornecedorService();
		ProdutoService proService = new ProdutoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regEnt = null;
		String campoEnt = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regEnt = str1.split(" ENTRADA ");
				for (int i = 1 ; i < regEnt.length ; i++) {
					campoEnt = regEnt[i];
					String[] campo = campoEnt.split(" , ");
					ent.setNumeroEnt(Integer.parseInt(campo[0].replaceAll("\s", "")));
					ent.setNnfEnt(Integer.parseInt(campo[1]));
					Date dataEnt = sdfAno.parse(campo[2]);
					cal.setTime(dataEnt);
					ent.setDataEnt(cal.getTime());
					ent.setNomeFornEnt(campo[3]);
					ent.setNomeProdEnt(campo[4]);
					ent.setQuantidadeProdEnt(Double.parseDouble(campo[5]));
					ent.setValorProdEnt(Double.parseDouble(campo[6]));
					ent.setForn(forService.findById(Integer.parseInt(campo[7])));
					ent.setProd(proService.findById(Integer.parseInt(campo[8])));

					entService.insertBackUp(ent);
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
