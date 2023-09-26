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
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Cryptograf;

public class RestauraProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraProduto(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Produto prod = new Produto();		
		ProdutoService prodService = new ProdutoService();
		GrupoService gruService = new GrupoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regSit = null;
		String campoSit = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regSit = str1.split(" PRODUTO ");
				for (int i = 1 ; i < regSit.length ; i++) {
					campoSit = regSit[i];
					String[] campo = campoSit.split(" , ");
					prod.setCodigoProd(Integer.parseInt(campo[0].replaceAll("\s", "")));
					prod.setGrupoProd(Integer.parseInt(campo[1]));
					prod.setNomeProd(campo[2]);
					prod.setSaldoProd(Double.parseDouble(campo[3]));
					prod.setEstMinProd(Double.parseDouble(campo[4]));
					prod.setPrecoProd(Double.parseDouble(campo[5]));
					prod.setVendaProd(Double.parseDouble(campo[6]));
					prod.setCmmProd(Double.parseDouble(campo[7]));
					prod.setSaidaCmmProd(Double.parseDouble(campo[8]));
					Date data = sdfAno.parse(campo[9]);
					cal.setTime(data);
					prod.setDataCadastroProd(cal.getTime());
					prod.setGrupo(gruService.findById(Integer.parseInt(campo[10])));
					prod.setPercentualProd(Double.parseDouble(campo[11]));
					prod.setLetraProd(campo[12].charAt(0));
					prodService.insertBackUp(prod);
					count += 1;
				}
			}	
			return count;
		}	
		catch(	IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		catch(ParseException e3) {
			status = "Er";
			throw new DbException(e3.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
