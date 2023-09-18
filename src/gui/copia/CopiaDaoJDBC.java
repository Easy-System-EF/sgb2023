package gui.copia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;

public class CopiaDaoJDBC implements CopiaDao {

	private Connection conn;
	
	public CopiaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "BackUp JDBC ";
	
	@Override
	public void insert(Copia obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO backUp " +
							"(DataIBackUp, UserBackUp, DataFBackUp, UnidadeBackUp) " +
								" VALUES " + 
							"(?, ?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDataIBackUp());
			st.setString(2, obj.getUserBackUp());
			st.setString(3, obj.getDataFBackUp());
			st.setString(4, obj.getUnidadeBackUp());

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int cod = rs.getInt(1);
					obj.setIdBackUp(cod);;
				} else {
					throw new DbException(classe + "Erro!!! sem inclusão" );
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Copia> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM BackUp " +
						"ORDER BY - dataIBackUp ");

			rs = st.executeQuery();

			List<Copia> list = new ArrayList<>();

			while (rs.next()) {
				Copia obj = instantiateBackUp(rs);
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
	public void deleteById(int cod) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM backUp WHERE IdBackUp = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, cod);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
	
	private Copia instantiateBackUp(ResultSet rs) throws SQLException {
		Copia backUp = new Copia();
		backUp.setIdBackUp(rs.getInt("IdBackUp"));
		backUp.setDataIBackUp(rs.getString("DataIBackUp"));
		backUp.setUserBackUp(rs.getString("UserBackUp"));
		backUp.setDataFBackUp(rs.getString("DataFBackUp"));
		backUp.setUnidadeBackUp(rs.getString("UnidadeBackUp"));
		return backUp;
	}
}
