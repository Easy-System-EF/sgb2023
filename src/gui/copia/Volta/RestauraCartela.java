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
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.service.CartelaService;
import gui.util.Cryptograf;

public class RestauraCartela implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraCartela(Integer count, String unid, String meioSgb, String file, String ext) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		String path = unid + meioSgb + file + ext;
		Cartela car = new Cartela();		
		CartelaService carService = new CartelaService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 	Calendar cal = Calendar.getInstance();

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
				regCar = str1.split(" CARTELA ");
				for (int i = 1 ; i < regCar.length ; i++) {
					campoCar = regCar[i];
					String[] campo = campoCar.split(" , ");
					car.setNumeroCar(Integer.parseInt(campo[0].replaceAll("\s", "")));
					Date dataCar = sdfAno.parse(campo[1]);
					cal.setTime(dataCar);
					car.setDataCar(cal.getTime());
					car.setLocalCar(campo[2]);
					car.setDescontoCar(Double.parseDouble(campo[3]));
					car.setTotalCar(Double.parseDouble(campo[4]));
					car.setSituacaoCar(campo[5]);
					car.setNumeroPaganteCar(Integer.parseInt(campo[6]));
					car.setValorPaganteCar(Double.parseDouble(campo[7]));
					car.setMesCar(Integer.parseInt(campo[8]));
					car.setAnoCar(Integer.parseInt(campo[9]));
					car.setObsCar(campo[10]);
					car.setServicoCar(campo[11]);
					car.setValorServicoCar(Double.parseDouble(campo[12]));
					car.setSubTotalCar(Double.parseDouble(campo[13]));
					car.setNomeSituacaoCar(campo[14]);
					car.setMesPagCar(Integer.parseInt(campo[15]));
					car.setAnoPagCar(Integer.parseInt(campo[16]));
					carService.insertBackUp(car);
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
