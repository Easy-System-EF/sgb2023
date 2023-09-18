package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.TipoConsumidorDao;
import gui.sgcpmodel.entites.TipoConsumo;
 
public class TipoConsumidorDaoJDBC implements TipoConsumidorDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public TipoConsumidorDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(TipoConsumo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO tipoFornecedor " +
				      "(NomeTipo)" + 
  				      "VALUES " +
				      "(?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeTipo());
			
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoTipo(codigo);;
//					System.out.println("New key inserted: " + obj.getCodigoTipo());
				}
				else
				{	throw new DbException("Erro!!! sem inclus�o" );
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
	public void insertBackUp(TipoConsumo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO tipoFornecedor (CodigoTipo, NomeTipo)" + 
  				      "VALUES " +
				      "(?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeTipo());

 			st.executeUpdate();
			
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
	public void update(TipoConsumo obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE tipoFornecedor " +  
							"SET NomeTipo = ? " +
   					"WHERE (CodigoTipo = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeTipo());
 			st.setInt(2, obj.getCodigoTipo());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException ( "Erro!!! sem atualiza��o " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(int tipo) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM tipoFornecedor WHERE CodigoTipo = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, tipo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! na� exclu�do " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public TipoConsumo findById(int tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM tipoFornecedor " +
 				 "WHERE CodigoTipo = ?");
 			
			st.setInt(1, tipo);
			rs = st.executeQuery();
			if (rs.next())
			{	TipoConsumo obj = instantiateTipoFornecedor(rs);
				return obj;
			}
			return null;
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
	public List<TipoConsumo> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM tipoFornecedor " +
					"ORDER BY NomeTipo");
			
			rs = st.executeQuery();
			
			List<TipoConsumo> list = new ArrayList<>();
			
			while (rs.next())
			{	TipoConsumo obj = instantiateTipoFornecedor(rs);
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
	public Integer findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		@SuppressWarnings("unused")
		Integer cod = 0;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM tipoFornecedor " +
						"WHERE NomeTipo like ? " +	
					"ORDER BY NomeTipo ");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			while (rs.next()) {
				TipoConsumo obj = instantiateTipoFornecedor(rs);
				return cod = obj.getCodigoTipo();
 			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private TipoConsumo instantiateTipoFornecedor(ResultSet rs) throws SQLException {
 		TipoConsumo tipoForn = new TipoConsumo();
 		
 		tipoForn.setCodigoTipo(rs.getInt("CodigoTipo"));
 		tipoForn.setNomeTipo(rs.getString("NomeTipo"));	
         return tipoForn;
	}
}
