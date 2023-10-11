package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgbmodel.entities.Cliente;
import gui.sgbmodel.service.ClienteService;
import gui.util.Cryptograf;

public class RestauraCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraCliente(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Cliente cliente = new Cliente();		
		ClienteService clienteService = new ClienteService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regCli = null;
		String campoCli = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regCli = str1.split("CLIENTE");
				for (int i = 1 ; i < regCli.length ; i++) {
					campoCli = regCli[i];
					String[] campo = campoCli.split(" , ");
					cliente.setCodigoCli(Integer.parseInt(campo[0].replace("\s", "")));
					cliente.setNomeCli(campo[1]);
					cliente.setDddCli(Integer.parseInt(campo[2]));
					cliente.setTelefoneCli(Integer.parseInt(campo[3].replace(";", "")));
					cliente.setConvenioCli(campo[4]);
					clienteService.insertBackUp(cliente);
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
