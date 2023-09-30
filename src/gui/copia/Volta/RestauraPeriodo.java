package gui.copia.Volta;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DbException;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.TipoConsumoService;

public class RestauraPeriodo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Integer restauraPeriodo(Integer count) {
		@SuppressWarnings("unused")
		String status = "Ok";
		count = 0;
		ParPeriodo per = new ParPeriodo();		
		ParPeriodoService perService = new ParPeriodoService();
		FornecedorService forService = new FornecedorService();
		TipoConsumoService tipoService = new TipoConsumoService();
	 	SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			per.setIdPeriodo(1);
			per.setDtiPeriodo(sdfAno.parse("2020-01-01 00:00"));
			per.setDtfPeriodo(sdfAno.parse("2049-01-01 00:00"));
			per.setFornecedor(forService.findById(1));
			per.setTipoConsumo(tipoService.findById(1));
			perService.update(per);
			count += 1;
			return count;
		}	
		catch(ParseException e2) {
			status = "Er";
			throw new DbException(e2.getMessage());	 			
		}
		finally {
		}
	}
}	
