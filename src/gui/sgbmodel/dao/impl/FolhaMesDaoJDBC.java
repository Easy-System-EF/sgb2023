package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.FolhaMesDao;
import gui.sgbmodel.entities.FolhaMes;
  
public class FolhaMesDaoJDBC implements FolhaMesDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FolhaMesDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	String classe = "Folha Mes JDBC";

	@Override
	public void insert(FolhaMes obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FolhaMes " +
				      "(FuncionarioFolha, CargoFolha, SituacaoFolha, SalarioFolha, " +
				      "ComissaoFolha, ValeFolha, ReceberFolha, TotalFolha , " +
				      "MesFolha, AnoFolha ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

 			st.setString(1, obj.getFuncionarioFolha());
 			st.setString(2, obj.getCargoFolha());
 			st.setString(3, obj.getSituacaoFolha());
 			st.setString(4, obj.getSalarioFolha());
 			st.setString(5, obj.getComissaoFolha());
 			st.setString(6, obj.getValeFolha());
 			st.setString(7, obj.getReceberFolha());
 			st.setString(8, obj.getTotalFolha());
			st.setInt(9,  obj.getMesFolha());
			st.setInt(10, obj.getAnoFolha());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroFolha(codigo);
				} else {
					throw new DbException("Erro!!! " + classe + " " + " sem inclusão" );
				}
			}	
  		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public List<FolhaMes> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From FolhaMes " +	
					"ORDER BY FuncionarioFolha");
			
			rs = st.executeQuery();
			
			List<FolhaMes> list = new ArrayList<>();
			
			while (rs.next()) {
				FolhaMes obj = instantiateFolhaMes(rs);
 					list.add(obj);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgb.FolhaMes " );

			st.executeUpdate();

		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private FolhaMes instantiateFolhaMes(ResultSet rs) throws SQLException {
		FolhaMes Folha = new FolhaMes();
 		Folha.setNumeroFolha(rs.getInt("NumeroFolha"));
 		Folha.setFuncionarioFolha(rs.getString("FuncionarioFolha"));	
 		Folha.setCargoFolha(rs.getString("CargoFolha"));
 		Folha.setSituacaoFolha(rs.getString("SituacaoFolha"));
 		Folha.setSalarioFolha(rs.getString("SalarioFolha"));
 		Folha.setComissaoFolha(rs.getString("ComissaoFolha"));
 		Folha.setValeFolha(rs.getString("ValeFolha"));
 		Folha.setReceberFolha(rs.getString("ReceberFolha"));
 		Folha.setTotalFolha(rs.getString("TotalFolha"));
 		Folha.setMesFolha(rs.getInt("MesFolha"));
 		Folha.setAnoFolha(rs.getInt("AnoFolha"));
        return Folha;
	}

}
