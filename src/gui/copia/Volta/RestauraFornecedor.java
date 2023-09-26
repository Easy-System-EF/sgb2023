package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.service.FornecedorService;
import gui.util.Cryptograf;

public class RestauraFornecedor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static Integer restauraFornecedor(Integer count, String unid, String meioSgcp, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgcp + file + ext;
		Fornecedor forn = new Fornecedor();		
		FornecedorService fornService = new FornecedorService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regFornecedor = null;
		String campoFornecedor = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regFornecedor = str1.split(" FORNECEDOR ");
				for (int i = 1 ; i < regFornecedor.length ; i++) {
					campoFornecedor = regFornecedor[i];
					String[] campo = campoFornecedor.split(" , ");
					forn.setCodigo(Integer.parseInt(campo[0].replaceAll("\s", "")));
					forn.setRazaoSocial(campo[1]);
					forn.setRua(campo[2]);
					forn.setNumero(Integer.parseInt(campo[3]));
					forn.setComplemento(campo[4]);
					forn.setBairro(campo[5]);
					forn.setCidade(campo[6]);
					forn.setUf(campo[7]);
					forn.setCep(campo[8]);
					forn.setDdd01(Integer.parseInt(campo[9]));
					forn.setTelefone01(Integer.parseInt(campo[10]));
					forn.setDdd02(Integer.parseInt(campo[11]));
					forn.setTelefone02(Integer.parseInt(campo[12]));
					forn.setContato(campo[13]);
					forn.setDddContato(Integer.parseInt(campo[14]));
					forn.setTelefoneContato(Integer.parseInt(campo[15]));
					forn.setEmail(campo[16]);
					forn.setPix(campo[17]);
					forn.setObservacao(campo[18]);
					forn.setPrazo(Integer.parseInt(campo[19]));
					forn.setParcela(Integer.parseInt(campo[20]));
					fornService.insertBackUp(forn);
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

