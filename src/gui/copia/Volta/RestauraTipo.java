package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Cryptograf;

public class RestauraTipo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static Integer restauraTipo(Integer count, String unid, String meioSgcp, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgcp + file + ext;
		TipoConsumo tipo = new TipoConsumo();		
		TipoConsumoService tipoService = new TipoConsumoService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regTipo = null;
		String campoTipo = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regTipo = str1.split(" TIPO ");
				for (int i = 1 ; i < regTipo.length ; i++) {
					campoTipo = regTipo[i];
					String[] campo = campoTipo.split(" , ");
					tipo.setCodigoTipo(Integer.parseInt(campo[0].replaceAll("\s", "")));
					tipo.setNomeTipo(campo[1].replace(";", ""));
					tipoService.insertBackUp(tipo);
					count += 1;
				}
			}	
			return count;
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
