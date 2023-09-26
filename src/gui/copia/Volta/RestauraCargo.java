package gui.copia.Volta;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import db.DbException;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.service.CargoService;
import gui.util.Cryptograf;

public class RestauraCargo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Integer restauraCargo(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Cargo cargo = new Cargo();		
		CargoService cargoService = new CargoService();

		FileReader fr;
		Scanner sc = null;
		String str = null;
		String str1 = null;
		String[] regCar = null;
		String campoCar = null;

		try {
			fr = new FileReader(path);
			sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				str = sc.nextLine();		
				str1 = Cryptograf.desCriptografa(str);
				regCar = str1.split("CARGO");
				for (int i = 1 ; i < regCar.length ; i++) {
					campoCar = regCar[i];
					String[] campo = campoCar.split(" , ");
					cargo.setCodigoCargo(Integer.parseInt(campo[0].replace("\s", "")));
					cargo.setNomeCargo(campo[1]);
					cargo.setSalarioCargo(Double.parseDouble(campo[2]));
					cargo.setComissaoCargo(Double.parseDouble(campo[3].replace(";", "")));
					cargoService.insertBackUp(cargo);
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
