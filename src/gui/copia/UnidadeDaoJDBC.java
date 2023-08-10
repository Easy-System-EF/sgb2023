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

public class UnidadeDaoJDBC implements UnidadeDao {

	private Connection conn;
	
	public UnidadeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Unidade JDBC ";
	
	@Override
	public void insert(Unidade obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO backUp " +
							"(LetraUnidade )" +
								"VALUES " + 
							"(? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getLetraUnid());

			st.executeUpdate();
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int cod = rs.getInt(1);
					obj.setIdUnid(cod);;
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
	public List<Unidade> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM Unidade " +
						"ORDER BY letraUnid ");

			rs = st.executeQuery();

			List<Unidade> list = new ArrayList<>();

			while (rs.next()) {
				Unidade obj = instantiateUnidade(rs);
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
					"DELETE FROM cargo WHERE IdUnid = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, cod);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
	
	private Unidade instantiateUnidade(ResultSet rs) throws SQLException {
		Unidade unid = new Unidade();
		unid.setIdUnid(rs.getInt("IdUnid"));
		unid.setLetraUnid(rs.getString("LetraUnid"));
		return unid;
	}
}
