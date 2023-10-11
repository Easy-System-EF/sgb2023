package gui.copia;

import db.DB;

public class DaoFactory {

/*
 * � obrigat�rio passar uma conex�o como argumento (db.getCon...)
 */
 	public static CopiaDao createBackUpDao() {
		return new CopiaDaoJDBC(DB.getConnection());
	}
 	
 	public static UnidadeDao createUnidadeDao() {
		return new UnidadeDaoJDBC(DB.getConnection());
	}
 	
 	public static RestauraDao createRestauraDao() {
		return new RestauraDaoJDBC(DB.getConnection());
	}
}
