package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Cryptograf;

public class RestauraVirtual implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraVirtual(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		CartelaVirtual vir = new CartelaVirtual();		
		CartelaVirtualService virService = new CartelaVirtualService();
		ProdutoService prodService = new ProdutoService();
		FuncionarioService funService = new FuncionarioService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regVir = null;
		String campoVir = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regVir = str1.split(" CARTELAVIRTUAL ");
				for (int i = 1 ; i < regVir.length ; i++) {
					campoVir = regVir[i];
					String[] campo = campoVir.split(" , ");
					vir.setNumeroVir(Integer.parseInt(campo[0].replaceAll("\s", "")));
					vir.setLocalVir(campo[1]);
					vir.setSituacaoVir(campo[2]);
					vir.setNomeFunVir(campo[3]);
					vir.setNomeProdVir(campo[4]);
					vir.setQuantidadeProdVir(Double.parseDouble(campo[5]));
					vir.setPrecoProdVir(Double.parseDouble(campo[6]));
					vir.setVendaProdVir(Double.parseDouble(campo[7]));
					vir.setTotalProdVir(Double.parseDouble(campo[8]));
					vir.setOrigemIdCarVir(Integer.parseInt(campo[9]));
					vir.setFuncionario(funService.findById(Integer.parseInt(campo[10])));
					vir.setProduto(prodService.findById(Integer.parseInt(campo[11])));					
					virService.saveOrUpdate(vir);
					count += 1;
				}
			}	
			return count;
		}	
		catch(IOException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
			sc.close();
		}
	}
}	
