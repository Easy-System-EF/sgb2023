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
import gui.sgbmodel.dao.ClienteDao;
import gui.sgbmodel.entities.Cliente;
  
public class ClienteDaoJDBC implements ClienteDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public ClienteDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Cliente JDBC ";
	
	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cliente " +
				      "(NomeCli, DddCli, TelefoneCli, ConvenioCli )" + 
  				      "VALUES " +
				      "(?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeCli());
 			st.setDouble(2, obj.getDddCli());
 			st.setDouble(3, obj.getTelefoneCli());
 			st.setString(4, obj.getConvenioCli());
			 
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setCodigoCli(codigo);;
//					System.out.println("New key inserted: " + obj.getCodigoCli());
				}
				else {
					throw new DbException(classe + "Erro!!! sem inclusão" );
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
	public void insertBackUp(Cliente obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cliente " +
				      "(CodigoCli, NomeCli, DddCli, TelefoneCli, ConvenioCli )" + 
  				      "VALUES " +
				      "(?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setInt(1, obj.getCodigoCli());
 			st.setString(2, obj.getNomeCli());
 			st.setDouble(3, obj.getDddCli());
 			st.setDouble(4, obj.getTelefoneCli());
 			st.setString(5, obj.getConvenioCli());
			 
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
	public void update(Cliente obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"UPDATE cliente " +  
							"SET NomeCli = ?, DddCli = ?, TelefoneCli = ?, ConvenioCli = ? " +
   					"WHERE (CodigoCli = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeCli());
 			st.setDouble(2, obj.getDddCli());
 			st.setDouble(3, obj.getTelefoneCli());
 			st.setString(4, obj.getConvenioCli());
 			st.setInt(5, obj.getCodigoCli());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigoCli) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM cliente WHERE CodigoCli = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigoCli);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! não excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Cliente findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM cliente " +
 				 "WHERE CodigoCli = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Cliente obj = instantiateClientes(rs);
 					return obj;
				}	
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
	public List<Cliente> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cliente " +
//						"WHERE ConvenioCli = 'S' " +
					"ORDER BY NomeCli");
			
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Cliente obj = instantiateClientes(rs);
 					list.add(obj);
				}	
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
	
	private Cliente instantiateClientes(ResultSet rs) throws SQLException {
 		Cliente cli = new Cliente(); 		
 		cli.setCodigoCli(rs.getInt("CodigoCli"));
 		cli.setNomeCli(rs.getString("NomeCli"));	
 		cli.setDddCli(rs.getInt("DddCli"));
 		cli.setTelefoneCli(rs.getInt("TelefoneCli"));
 		cli.setConvenioCli(rs.getString("ConvenioCli"));
        return cli;
	}
	
	@Override
	public List<Cliente> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " 
						+ "FROM Cliente " 
							+ "WHERE NomeCli like ? "
						+ "ORDER BY - Nomecli ");
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Cliente> list = new ArrayList<>();
			
			while (rs.next()) {
				Cliente obj = instantiateClientes(rs);
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
}
