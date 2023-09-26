package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.SituacaoService;
import gui.util.Cryptograf;

public class RestauraFuncionario implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraFuncionario(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Funcionario fun = new Funcionario();		
		FuncionarioService funService = new FuncionarioService();
		CargoService carService = new CargoService();
		SituacaoService sitService = new SituacaoService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regFun = null;
		String campoFun = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();	
				str1 = Cryptograf.desCriptografa(str);
				regFun = str1.split(" FUNCIONARIO ");
				for (int i = 1 ; i < regFun.length ; i++) {
					campoFun = regFun[i];
					String[] campo = campoFun.split(" , ");
					fun.setCodigoFun(Integer.parseInt(campo[0].replaceAll("\s", "")));
					fun.setNomeFun(campo[1]);
					fun.setEnderecoFun(campo[2]);
					fun.setBairroFun(campo[3]);
					fun.setCidadeFun(campo[4]);
					fun.setUfFun(campo[5]);
					fun.setCepFun(campo[6]);
					fun.setDddFun(Integer.parseInt(campo[7]));
					fun.setTelefoneFun(Integer.parseInt(campo[8]));
					fun.setCpfFun(campo[9]);
					fun.setPixFun(campo[10]);
					fun.setComissaoFun(Double.parseDouble(campo[11]));
					fun.setAdiantamentoFun(Double.parseDouble(campo[12]));
					fun.setMesFun(Integer.parseInt(campo[13]));
					fun.setAnoFun(Integer.parseInt(campo[14]));
					fun.setCargoFun(campo[15]);
					fun.setSituacaoFun(campo[16]);
					fun.setSalarioFun(Double.parseDouble(campo[17]));
					fun.setCargo(carService.findById(Integer.parseInt(campo[18])));
					fun.setSituacao(sitService.findById(Integer.parseInt(campo[19])));
					funService.insertBackUp(fun);
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
