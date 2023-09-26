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

public class RestauraDaoJDBC implements RestauraDao {

	private Connection conn;
	
	public RestauraDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Restaura JDBC ";
	
	@Override
	public void insert(Restaura obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO restaura " +
							"(fileUp, statusUp, countUp )" +
								"VALUES " + 
							"(?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getFileUp());
			st.setString(2, obj.getStatusUp());
			st.setInt(3, obj.getCountUp());

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int cod = rs.getInt(1);
					obj.setIdRestauraUp(cod);
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
	public List<Restaura> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM Restaura " +
						"ORDER BY fileUp ");

			rs = st.executeQuery();

			List<Restaura> list = new ArrayList<>();

			while (rs.next()) {
				Restaura obj = instantiateBackUp(rs);
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
					"DELETE FROM Restaura WHERE IdRestauraUp = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, cod);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
	
	@Override
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE Restaura " );

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
	
	private Restaura instantiateBackUp(ResultSet rs) throws SQLException {
		Restaura restaura = new Restaura();
		restaura.setIdRestauraUp(rs.getInt("IdRestauraUp"));
		restaura.setFileUp(rs.getString("fileUp"));
		restaura.setStatusUp(rs.getString("statusUp"));
		restaura.setCountUp(rs.getInt("countUp"));
		return restaura;
	}
}
